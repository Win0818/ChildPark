package com.worldchip.childpark.util.image;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.worldchip.childpark.util.Utils;
import com.worldchip.childpark.util.image.ImageSizeUtil.ImageSize;


/**
 * 图片加载�?
 * 
 */
public class MyThreadImageLoader
{
	private static MyThreadImageLoader mInstance;
	/**
	 * 图片缓存的核心对�?
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * 线程池
	 */
	private ExecutorService mThreadPool;
	
	private static final int DEAFULT_THREAD_COUNT = 1;
	/**
	 * 队列的调度方�?
	 */
	private Type mType = Type.LIFO;
	
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTaskQueue;
	/**
	 * 后台轮询线程
	 */
	private Thread mCheckThread;
	
	private Handler mPoolThreadHandler;
	/**
	 * UI线程中的Handler
	 */
	private Handler mUIHandler;

	private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
	private Semaphore mSemaphoreThreadPool;

	private boolean isDiskCacheEnable = true;

	private static final String TAG = "ImageLoader";
	
	/**
	 * FIFO 先入先出队列
	 * LIFO 后入先出队列
	 *
	 */
	public enum Type
	{
		FIFO, LIFO;
	}
	
	

	private MyThreadImageLoader(int threadCount, Type type)
	{
		init(threadCount, type);
	}

	/**
	 * 初始�?
	 * 
	 * @param threadCount
	 * @param type
	 */
	private void init(int threadCount, Type type)
	{
		initBackThread();

		// 获取我们应用的最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheMemory)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight();
			}

		};

		// 创建线程�?
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mType = type;
		mSemaphoreThreadPool = new Semaphore(threadCount);
		
		if (mUIHandler == null)
		{
			mUIHandler = new Handler()
			{
				public void handleMessage(Message msg)
				{
					// 获取得到图片，为imageview回调设置图片
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView imageview = holder.imageView;
					String path = holder.path;
					// 将path与getTag存储路径进行比较
					if (imageview.getTag().toString().equals(path))
					{
						imageview.setImageBitmap(bm);
					}
				};
			};
		}
	}

	/**
	 * 初始化后台轮询线程
	 */
	private void initBackThread()
	{
		// 后台轮询线程
		mCheckThread = new Thread()
		{
			@Override
			public void run()
			{
				Looper.prepare();
				mPoolThreadHandler = new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						// 线程池去取出�?个任务进行执�?
						mThreadPool.execute(getTask());
						try
						{
							mSemaphoreThreadPool.acquire();
							
						} catch (InterruptedException e)
						{
						}
					}
				};
				// 释放�?个信号量
				mSemaphorePoolThreadHandler.release();
				Looper.loop();
			};
		};

		mCheckThread.start();
	}

	public static MyThreadImageLoader getInstance()
	{
		if (mInstance == null)
		{
			synchronized (MyThreadImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new MyThreadImageLoader(DEAFULT_THREAD_COUNT, Type.LIFO);
				}
			}
		}
		return mInstance;
	}

	public static MyThreadImageLoader getInstance(int threadCount, Type type)
	{
		if (mInstance == null)
		{
			synchronized (MyThreadImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new MyThreadImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 根据path为imageview设置图片
	 * 
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final String path, final ImageView imageView,
			final boolean isFromNet)
	{
		imageView.setTag(path);
		// 根据path在缓存中获取bitmap
		Bitmap bm = getBitmapFromLruCache(path);
        Log.i(TAG, "--------从缓存中获取图片----------");
		if (bm != null)
		{
			 refreashBitmap(path, imageView, bm);
			 Log.i(TAG, "--------设置图片----------");
		} else
		{
			 addTask(buildTask(path, imageView, isFromNet));
			 Log.i(TAG, "--------开起异步去加载图片----------");
		}

	}

	/**
	 * 根据资源id为imageview设置图片
	 * 
	 * @param resId
	 * @param imageView
	 */
	public void loadImageFromRes(final Resources resources, final int resId, final ImageView imageView)
	{
		imageView.setTag(String.valueOf(resId));

		// 根据path在缓存中获取bitmap
		Bitmap bm = getBitmapFromLruCache(String.valueOf(resId));

		if (bm != null)
		{
			refreashBitmap(String.valueOf(resId), imageView, bm);
		} else
		{
			addTask(buildTask(resources, resId, imageView));
		}

	}
	
	private Runnable buildTask(final Resources resources,final int resId, final ImageView imageView)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				Bitmap bm = null;
				
				bm = loadImageFromResId(resources, resId, imageView);
				
				// 3、把图片加入到缓�?
				addBitmapToLruCache(String.valueOf(resId), bm);
				refreashBitmap(String.valueOf(resId), imageView, bm);
				mSemaphoreThreadPool.release();
			}
		};
	}
	
	
	private Bitmap loadImageFromResId(final Resources resources, final int resId,
			final ImageView imageView)
	{
		Bitmap bm = null;
		// 加载图片
		// 图片的压�?
		// 1、获得图片需要显示的大小
		ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		// 2、压缩图�?
		bm = decodeSampledBitmapFromRes(resources, resId, imageSize.width, imageSize.height);
		return bm;
	}
	
	
	/**
	 * 根据传入的参数，新建�?个任�?
	 * 
	 * @param path
	 * @param imageView
	 * @param isFromNet
	 * @return
	 */
	private Runnable buildTask(final String path, final ImageView imageView,
			final boolean isFromNet)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				Bitmap bm = null;
				if (isFromNet)
				{
					File file = getDiskCacheDir(imageView.getContext(),
							md5(path));
					
					if (file.exists())// 如果在缓存文件中发现
					{
						Log.e(TAG, "find image :" + path + " in disk cache .");
						bm = loadImageFromLocal(file.getAbsolutePath(),
								imageView);
					} else
					{
						if (isDiskCacheEnable)// �?测是否开启硬盘缓�?
						{
							boolean downloadState = DownloadImgUtils
									.downloadImgByUrl(path, file);
							if (downloadState)// 如果下载成功
							{
								Log.e(TAG,
										"download image :" + path
												+ " to disk cache . path is "
												+ file.getAbsolutePath());
								bm = loadImageFromLocal(file.getAbsolutePath(),
										imageView);
							}
						} else
						// 直接从网络加�?
						{
							Log.e(TAG, "load image :" + path + " to memory.");
							bm = DownloadImgUtils.downloadImgByUrl(path,
									imageView);
						}
					}
				} else
				{
					bm = loadImageFromLocal(path, imageView);
				}
				// 3、把图片加入到缓�?
				addBitmapToLruCache(path, bm);
				refreashBitmap(path, imageView, bm);
				mSemaphoreThreadPool.release();
			}

			
		};
	}
	
	private Bitmap loadImageFromLocal(final String path,
			final ImageView imageView)
	{
		Bitmap bm = null;
		// 加载图片
		// 图片的压�?
		// 1、获得图片需要显示的大小
		ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		// 2、压缩图�?
		if (Utils.isVideo(path)) {
			bm = ImageTool.getVideoThumb(path,imageSize.width, imageSize.height);
		} else {
			bm = decodeSampledBitmapFromPath(path, imageSize.width,
					imageSize.height);
		} 
		return bm;
	}

	/**
	 * 从任务队列取出一个方�?
	 * 
	 * @return
	 */
	private Runnable getTask()
	{
		if (mType == Type.FIFO)
		{
			return mTaskQueue.removeFirst();//移除并返回此列表的第�?个元素�??
		} else if (mType == Type.LIFO)
		{
			return mTaskQueue.removeLast();
		}
		return null;
	}

	/**
	 * 利用签名辅助类，将字符串字节数组
	 * @param str
	 * @return
	 */
	public String md5(String str)
	{
		byte[] digest = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("md5");
			digest = md.digest(str.getBytes());
			return bytes2hex02(digest);

		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 方式�?
	 * 
	 * @param bytes
	 * @return
	 */
	public String bytes2hex02(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		for (byte b : bytes)
		{
			// 将每个字节与0xFF进行与运算，然后转化�?10进制，然后�?�助于Integer再转化为16进制
			tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() == 1)// 每个字节8为，转为16进制标志�?2�?16进制�?
			{
				tmp = "0" + tmp;
			}
			sb.append(tmp);
		}

		return sb.toString();

	}

	private void refreashBitmap(final String path, final ImageView imageView,
			Bitmap bm)
	{
		Message message = Message.obtain();
		ImgBeanHolder holder = new ImgBeanHolder();
		holder.bitmap = bm;
		holder.path = path;
		holder.imageView = imageView;
		message.obj = holder;
		mUIHandler.sendMessage(message);
	}

	/**
	 * 将图片加入LruCache
	 * 
	 * @param path
	 * @param bm
	 */
	protected void addBitmapToLruCache(String path, Bitmap bm)
	{
		if (getBitmapFromLruCache(path) == null)
		{
			if (bm != null)
				mLruCache.put(path, bm);
		}
	}

	
	/**
	 * 根据图片�?要显示的宽和高对图片进行压缩
	 * 
	 * @param resId
	 * @param width
	 * @param height
	 * @return
	 */
	protected Bitmap decodeSampledBitmapFromRes(Resources resources, int resId, int width,
			int height)
	{
		// 获得图片的宽和高，并不把图片加载到内存中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, options);

		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
				width, height);

		// 使用获得到的InSampleSize再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);
		return bitmap;
	}
	
	/**
	 * 根据图片�?要显示的宽和高对图片进行压缩
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	protected Bitmap decodeSampledBitmapFromPath(String path, int width,
			int height)
	{
		// 获得图片的宽和高，并不把图片加载到内存中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
				width, height);

		// 使用获得到的InSampleSize再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	/**
	 * 根据图片�?要显示的宽和高对图片进行压缩
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	/*protected Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int width,
			int height)
	{
		if (bitmap == null) {
			return null;
		}
		// 获得图片的宽和高，并不把图片加载到内存中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
				width, height);

		// 使用获得到的InSampleSize再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	*/
	
	
	private synchronized void addTask(Runnable runnable)
	{
		mTaskQueue.add(runnable);
		// if(mPoolThreadHandler==null)wait();
		try
		{
			if (mPoolThreadHandler == null)
				mSemaphorePoolThreadHandler.acquire();
		} catch (InterruptedException e)
		{
		}
		mPoolThreadHandler.sendEmptyMessage(0x110);
	}

	/**
	 * 获得缓存图片的地�?
	 * 
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public File getDiskCacheDir(Context context, String uniqueName)
	{
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
		{
			cachePath = context.getExternalCacheDir().getPath();
		} else
		{
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}

	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}
	
	
	   @SuppressLint("NewApi") 
	public  Bitmap createVideoThumbnail(String url, int width, int height) {
		  Bitmap bitmap = null;
		  MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		  int kind = MediaStore.Video.Thumbnails.MINI_KIND;
		  try {
		   if (Build.VERSION.SDK_INT >= 14) {
		    retriever.setDataSource(url, new HashMap<String, String>());
		   } else {
		    retriever.setDataSource(url);
		   }
		   bitmap = retriever.getFrameAtTime();
		  } catch (IllegalArgumentException ex) {
		   // Assume this is a corrupt video file
		  } catch (RuntimeException ex) {
		   // Assume this is a corrupt video file.
		  } finally {
		   try {
		    retriever.release();
		   } catch (RuntimeException ex) {
		   }
		  }
		  if (kind == Images.Thumbnails.MICRO_KIND && bitmap != null) {
		   bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		     ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		  }
		  return bitmap;
		 }
		
	
	
}
