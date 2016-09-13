package com.worldchip.childpark.application;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;

import com.worldchip.childpark.R;
import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.util.Utils;

public class MyApplication  extends Application{
	
	public  static  Context  applicationContext;
	
	private static MyApplication instance;
	
	private String TAG  = "CHRIS";
	private MediaPlayer mMediaPlayer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		 applicationContext = getApplicationContext();
	     instance = this;
		 Comments.DEVICE_SYSTEM_LANGUAGE = Utils.getLanguageInfo(applicationContext);
		 if (MySharePreData.GetBooleanTrueData(applicationContext, "child_park", "open_sound")) {
			 play();
		 }
		 
	}
	
	
	
   public static Context  getApplicationContex(){
		if(applicationContext != null){
		return applicationContext;
		}
		return null;
	}
   
   private void play() {
		mMediaPlayer = MediaPlayer.create(this, R.raw.open_music);
		mMediaPlayer.start();
	}


}
