package com.worldchip.childpark.service;

import java.io.File;
import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.worldchip.childpark.R;
import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.entity.VideoInfo;
import com.worldchip.childpark.util.MyJsonParserUtil;
import com.worldchip.childpark.util.Utils;
import com.worldchip.childpark.util.net.HttpUtils;

public class DownLoadAdVideoService  extends  IntentService {
	 private final static String DOWNLOAD_VIDEO_PATH = "http://121.37.32.37:8005/index.php/admin/restinfo/getdanmov";
     private final static String DOWNLOAD_IMAGE_PATH = "http://121.37.32.37:8005/index.php/admin/restinfo/restpic";
     private final static String SP_AD_VIDEO_NAME = "ad_name";
     private VideoInfo  mAdVideoInfo;
          
     
    public DownLoadAdVideoService() {
    	super("DownLoadAdVideoService");
	}
   

	@Override
	protected void onHandleIntent(Intent intent) {
		    downLoadAdImageSaveData();
		    downLoadAdVideoSaveData();
	}

	private void downLoadAdImageSaveData() {
		try {
			 String result = HttpUtils.getRequest(DOWNLOAD_IMAGE_PATH, DownLoadAdVideoService.this);
			 if(result  !=null && !result.equals("")){
			  String imageAdAttrs = MyJsonParserUtil.parserMyAdImagejson(result);
			  System.out.println("----DownLoadAdVideoService--imageAdAttrs ---"+imageAdAttrs);
			    if(imageAdAttrs != null && !imageAdAttrs.equals("")){
			    	 Comments.adBitMap = Utils.GetLocalOrNetBitmap(imageAdAttrs);
			    	 System.out.println("----DownLoadAdVideoService-- Comments.adBitMap ---"+Comments.adBitMap.getWidth());
			    }
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void downLoadAdVideoSaveData() {
		try {
			   String result = HttpUtils.getRequest(DOWNLOAD_VIDEO_PATH, DownLoadAdVideoService.this);
		   if(result != null &&  !result.equals("")){
			   mAdVideoInfo = MyJsonParserUtil.parserMyAdVideojson(result);
			   String adName = MySharePreData.GetData(DownLoadAdVideoService.this, Comments.SP_FIlE_NAMW, SP_AD_VIDEO_NAME);
			   File file = new File(Comments.filepath+File.separator+"ad");
			if(mAdVideoInfo != null){
				if(!adName.equals(mAdVideoInfo.getmVideoName()) || !file.exists()){
					downloadVideo(mAdVideoInfo.getmVideoAddress());
					MySharePreData.SetData(DownLoadAdVideoService.this, Comments.SP_FIlE_NAMW, SP_AD_VIDEO_NAME, mAdVideoInfo.getmVideoName());
					MySharePreData.SetIntData(DownLoadAdVideoService.this, Comments.SP_FIlE_NAMW, Comments.SP_AD_TYPES, mAdVideoInfo.getCateId());
				}
			  }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//下载视频广告
	private void downloadVideo(String downLoadVieoPath){
		File dir = new File(Comments.filepath);
		if (!dir.exists()) {
			dir.mkdirs();
		} 
		 String filepath = dir.getAbsolutePath()+File.separator+"ad";	
		 File file = null;
		try {
			file = Utils.downloadVideoFile(downLoadVieoPath, filepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
