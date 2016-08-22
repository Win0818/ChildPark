package com.worldchip.childpark.service;

import com.worldchip.childpark.AdplayerActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class StartAdPlayerBootRecivr  extends  BroadcastReceiver{
	private static final String TAG = "CHRIS";
	@Override
	public void onReceive(final Context context, Intent intent) {
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			Log.e(TAG, "-----CommitDataServiceBootRecivr---onReceive---");
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					Intent  intentBoot = new Intent();
					intentBoot.setClass(context, AdplayerActivity.class);
				    context.startActivity(intentBoot);
				}
			}, 5*1000);
			
		}
	}
}
