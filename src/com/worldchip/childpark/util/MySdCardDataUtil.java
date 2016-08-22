package com.worldchip.childpark.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.worldchip.childpark.entity.MusicInfo;
import com.worldchip.childpark.entity.VideoInfo;

public class MySdCardDataUtil {

	/**
	 * 鑾峰彇sd鍗� 涓煇涓�璺緞涓嬬殑鍥剧墖鍦板潃鍒楄〃
	 * 
	 * @param strPath
	 * @return
	 */
	/*
	 * public static List<String> getPictures(final String strPath) {
	 * List<String> list = new ArrayList<String>(); File file = new
	 * File(strPath); File[] allfiles = file.listFiles(); if (allfiles == null)
	 * { return null; } for(int k = 0; k < allfiles.length; k++) { final File fi
	 * = allfiles[k]; if(fi.isFile()){ int idx = fi.getPath().lastIndexOf(".");
	 * if (idx <= 0) { continue; } String suffix = fi.getPath().substring(idx);
	 * if (suffix.toLowerCase().equals(".jpg") ||
	 * suffix.toLowerCase().equals(".jpeg") ||
	 * suffix.toLowerCase().equals(".bmp") ||
	 * suffix.toLowerCase().equals(".png") ||
	 * suffix.toLowerCase().equals(".gif") ) { list.add(fi.getAbsolutePath()); }
	 * } } return list; }
	 */

	private static final String TAG = "CHRIS";

	/**
	 * 鑾峰彇sd鍗� 涓煇涓�璺緞涓嬬殑鍥剧墖鍦板潃鍒楄〃
	 * 
	 * @param strPath
	 * @return
	 */
	public static List<String> getPictures(final String strPath) {
		List<String> list = new ArrayList<String>();
		File file = new File(strPath);
		File[] allfiles = file.listFiles();
		if (allfiles == null) {
			return null;
		}
		for (int k = 0; k < allfiles.length; k++) {
			final File fi = allfiles[k];
			if (fi.isFile()) {
				if (".jpg".equals(getFileEx(fi))
						|| ".jpeg".equals(getFileEx(fi))
						|| ".png".equals(getFileEx(fi))
						|| ".bmp".equals(getFileEx(fi))) {
					list.add(fi.getAbsolutePath());
				}
			}
		}
		return list;
	}

	public static void getMusices(File file, List<MusicInfo> musicInfos) {
		File[] files = file.listFiles();
		if (files != null && !file.getName().equals("Android")) {
			for (File f : files) {
				if (f.isFile()) {
					if (".mp3".equals(getFileEx(f))) {
						MusicInfo info = new MusicInfo();
						info.setmMusicAddress(f.getAbsolutePath());
						info.setmMusicName(f.getAbsolutePath());
						musicInfos.add(info);
					}
				} else {
					getMusices(f, musicInfos);
				}
			}
		}
	}

	// 閫氳繃濯掍綋鐨勬柟娉曟煡鎵緎d鍗￠煶涔愭枃浠�
	public static List<MusicInfo> getMusicFromSd(Context context) {
		List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();

		String str[] = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Video.Media.TITLE, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.SIZE };

		ContentResolver cr = context.getContentResolver();

		Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				str, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		
		cursor.moveToFirst();
		
		while (cursor.moveToNext()) {
			MusicInfo musicInfo = new MusicInfo();
			musicInfo.setId(cursor.getLong(0));
			musicInfo.setmMusicName(cursor.getString(2));
			musicInfo.setmMusicAddress(cursor.getString(3));
			musicInfos.add(musicInfo);
		}
		return musicInfos;
	}
	
	

/*	// 閫氳繃濯掍綋鐨勬柟娉曟煡鎵緎d鍗￠煶涔愭枃浠�
	public static List<VideoInfo> getVideo(Context context) {
		List<VideoInfo> musicInfos = new ArrayList<VideoInfo>();
		File[] files = file.listFiles();
		if (files != null && !file.getName().equals("Android")) {
			for (File f : files) {
				if (f.isFile()) {
					if (".mp3".equals(getFileEx(f))) {
						MusicInfo info = new MusicInfo();
						info.setmMusicAddress(f.getAbsolutePath());
						info.setmMusicName(f.getAbsolutePath());
						musicInfos.add(info);
					}
				} else {
					getMusices(f, musicInfos);
				}
			}
		}
	}*/


	// 閫氳繃濯掍綋鐨勬柟娉曟煡鎵緎d鍗¤棰戞枃浠�
	public static List<VideoInfo> getVideoFromSd(Context context) {
		List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
		String videoColmns[] = {
                MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.MIME_TYPE, 
				MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.DATA, 
				MediaStore.Video.Media.SIZE };
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				videoColmns, null, null,
				MediaStore.Video.Media.DEFAULT_SORT_ORDER);
		while (cursor.moveToNext()) {
			VideoInfo mVideoInfo = new VideoInfo();
			mVideoInfo.setmVideoName(cursor.getString(3));
			mVideoInfo.setmVideoAddress(cursor.getString(4));
			mVideoInfo.setmPicAddress(cursor.getString(4));
			videoInfos.add(mVideoInfo);
		}
		return videoInfos;
	}

	public static String getFileEx(File file) {
		String fileName = file.getName();
		int index = fileName.indexOf('.');
		if (index != -1) {
			int length = fileName.length();
			String str = fileName.substring(index, length);
			return str;
		}
		return "";
	}
	
	private static List<String> VideoExtens = null;

	public static List<String> getVideoExtens() 
	{
		if (VideoExtens == null) 
		{
			VideoExtens = new ArrayList<String>();
		}
		if (VideoExtens.size() < 1)
		{
			VideoExtens.add(".MP4");
			VideoExtens.add(".AVI");
			VideoExtens.add(".RMB");
			VideoExtens.add(".RMVB");
			VideoExtens.add(".FLV");
			VideoExtens.add(".MKV");
			VideoExtens.add(".MOV");
			VideoExtens.add(".TS");
			VideoExtens.add(".MPG");
			VideoExtens.add(".VOB");
			VideoExtens.add(".WMV");
			VideoExtens.add(".TP");
		}
		return VideoExtens;
	}
	
	public static void getVideoListInfo(final List<VideoInfo> files, File file){
       if(file == null){
    	   return;
       }
       file.listFiles(new FileFilter() {
			public boolean accept(File file){
				String name = file.getName();
				int i = name.lastIndexOf('.');
				if (i > -1) {
					String fileName = name.substring(0, i);
					String extend = name.substring(i);
					Log.e(TAG, "fileName="+fileName+"; extend="+extend);
					if(getVideoExtens().contains(extend.toUpperCase())){
				   	   VideoInfo info = new VideoInfo();
				   	   info.setmVideoAddress(file.getAbsolutePath());
				   	   info.setmVideoName(file.getName());
				   	   info.setmPicAddress(file.getAbsolutePath());
				   	   info.setFileSize(file.length());
				   	   files.add(info);
				   	   
					}
				}else if (file.isDirectory()) {
					getVideoListInfo(files, file);
				}
				return false;
			}
       });
	}
	  /**
     * 杞崲鏂囦欢澶у皬
     * @param fileS
     * @return
     */ 
    public static String FormetFileSize(long fileS) { 
        DecimalFormat df = new DecimalFormat("#.00"); 
        String fileSizeString = ""; 
        if (fileS < 1024) { 
            fileSizeString = df.format((double) fileS) + "B"; 
        } else if (fileS < 1048576) { 
            fileSizeString = df.format((double) fileS / 1024) + "K"; 
        } else if (fileS < 1073741824) { 
            fileSizeString = df.format((double) fileS / 1048576) + "M"; 
        } else { 
            fileSizeString = df.format((double) fileS / 1073741824) + "G"; 
        } 
        return fileSizeString; 
    }  
    
   /**
    * Java鏂囦欢鎿嶄綔 鑾峰彇鏂囦欢鎵╁睍鍚� 
    *  
    */   
       public static String getExtensionName(String filename) {    
           if ((filename != null) && (filename.length() > 0)) {    
               int dot = filename.lastIndexOf('.');    
               if ((dot >-1) && (dot < (filename.length() - 1))) {    
                   return filename.substring(dot + 1);    
               }    
           }    
           return filename;    
       }    
	
	
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height,int kind) {
        // 鑾峰彇瑙嗛鐨勭缉鐣ュ浘
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
       //extractThumbnail 鏂规硶浜屾澶勭悊,浠ユ寚瀹氱殑澶у皬鎻愬彇灞呬腑鐨勫浘鐗�,鑾峰彇鏈�缁堟垜浠兂瑕佺殑鍥剧墖
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
	
	  /**
     * 鑾峰彇澶栫疆SD鍗¤矾寰�
     * @return  搴旇灏变竴鏉¤褰曟垨绌�
     */
    public static List<String>  getExtSDCardPath()
    {
         List<String> mountLists= new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("secure")) continue;
                if (line.contains("asec")) continue;
                
                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        String mount =  columns[1] ;
                        mountLists.add(mount);
                        System.out.println("--sdcard瀛樺偍鍗�--fat"+mount);
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                    	String mount =  columns[1] ;
                        mountLists.add(mount);
                        System.out.println("--sdcard瀛樺偍鍗�--fuse"+mount);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return mountLists;
    }
	

}
