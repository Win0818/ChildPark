package com.worldchip.childpark.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	private static final String TAG = "CHRIS";


	@SuppressLint("DefaultLocale") 
	public static boolean isVideo(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			String lowerCase = filePath.toLowerCase();
			if (lowerCase.endsWith(".mp4") || lowerCase.endsWith(".3gp")
					|| lowerCase.endsWith(".avi") || lowerCase.endsWith(".rmvb")
					|| lowerCase.endsWith(".rm") || lowerCase.endsWith(".flv")) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean isImage(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			String lowerCase = filePath.toLowerCase();
			if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".png")
					|| lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".bmp")) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public static String timeFormat(Long timeLong){
		String time = new String();
		SimpleDateFormat  dateFormat = new SimpleDateFormat("mm:ss");
		time = dateFormat.format(new Date(timeLong));
		return time;
	}
	
	
	public  static  String  dayFormat(){
		SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date  currentDate = new Date(System.currentTimeMillis());
		String time = dateFormat.format(currentDate);
		if(time != null){
			return  time;
		}
		return null;
	}
	
	public static boolean IsConnnectWifi(Context context) {
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifiNetWorkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			  if(mWifiNetWorkInfo != null){
				  return mWifiNetWorkInfo.isConnected();
			  }
		}
          return false;
	}
	
	
	

	public static int getLanguageInfo(Context context) {
		String language = Locale.getDefault().getLanguage();
		Log.e("CHRIS", "language == "+language);
		if (language.contains("zh")) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
	public static File downloadVideoFile(String path,String filepath) throws Exception{
		URL url = new URL(path);//
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();//从网络获得链接
		connection.setRequestMethod("GET");//获得
		connection.setConnectTimeout(5000);//设置超时时间为5s
		if(connection.getResponseCode()==200){
			//检测是否正常返回数据请求 详情参照http协议
		    InputStream is = connection.getInputStream();//获得输入流
		    File file  = new File(filepath);//新建一个file文件
		if(file.exists()){
			file.delete();
		}
		file.createNewFile();
		Log.e(TAG, "--Utils--downloadVideoFile-"+file.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(file);//对应文件建立输出流
		byte[] buffer = new byte[1024];//新建缓存  用来存储 从网络读取数据 再写入文件
		int len = 0;
		while((len=is.read(buffer)) != -1){//当没有读到最后的时候 
		fos.write(buffer, 0, len);//将缓存中的存储的文件流秀娥问file文件
		Log.e(TAG, "--Utils--downloadVideoFile--read--"+len);
		}
		fos.flush();//将缓存中的写入file
		fos.close();
		is.close();//将输入流 输出流关闭
		return file;
		}
		return null;
		}
	
	
	public static Bitmap GetLocalOrNetBitmap(String url)
	{
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try
		{
			in = new BufferedInputStream(new URL(url).openStream(), 1024);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, 1024);
			copy(in, out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
	
	public static void showToastMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

}
