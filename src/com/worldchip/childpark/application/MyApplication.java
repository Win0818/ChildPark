package com.worldchip.childpark.application;

import android.app.Application;
import android.content.Context;

import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.util.Utils;

public class MyApplication  extends Application{
	
	public  static  Context  applicationContext;
	
	private static MyApplication instance;
	
	private String TAG  = "CHRIS";
	
	@Override
	public void onCreate() {
		super.onCreate();
		 applicationContext = getApplicationContext();
	     instance = this;
		 Comments.DEVICE_SYSTEM_LANGUAGE = Utils.getLanguageInfo(applicationContext);
	}
	
	
	
   public static Context  getApplicationContex(){
		if(applicationContext != null){
		return applicationContext;
		}
		return null;
	}


}
