package com.worldchip.childpark;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.dialog.PlayPauseAdDialog;
import com.worldchip.childpark.util.MyVideoPlayUtil;
import com.worldchip.childpark.util.MyVideoPlayUtil.AdPlayFinshListen;

public class AdplayerActivity  extends  Activity{
	private final static int DELAY_TIME_LONG = 5000;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private File  adFile ;
	private MyVideoPlayUtil myVideoPlayUtil;
	private PlayPauseAdDialog pauseAdDialog;
	private TextView mTvAdPlayVideoTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_player);
		adFile = new File(Comments.filepath+File.separator+"ad");
		int dw = getWindow().getDecorView().getWidth();
		int dh = getWindow().getDecorView().getHeight();
		if(!adFile.exists()){	
        	startMainActivity();
        }else{
            int adType = MySharePreData.GetIntData(this, Comments.SP_FIlE_NAMW, Comments.SP_AD_TYPES);        	
        	if(adType == 1){
        		playVideoAd(dw, dh);
        	}else if(adType == 2){
        	    Bitmap  bm = BitmapFactory.decodeFile(adFile.getAbsolutePath());
        		pauseAdDialog = new PlayPauseAdDialog(this, bm);
        		pauseAdDialog.setPauseIconInvisable();
        		pauseAdDialog.show();
        		new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						startMainActivity();
					}
				}, DELAY_TIME_LONG);
        	}
        }	
	}

	private void playVideoAd(int dw, int dh) {
		myVideoPlayUtil = MyVideoPlayUtil.getInstance();
		myVideoPlayUtil.isAdplay(true);
		myVideoPlayUtil.setAdPlayFinshListen(new AdPlayFinshListen() {
			@Override
			public boolean adIsPlayFinsh() {
				startMainActivity();
				return false;
			}
		});
		mTvAdPlayVideoTime = (TextView) findViewById(R.id.tv_video_ad_play_time);
		myVideoPlayUtil.setTextView(mTvAdPlayVideoTime,null);
		mSurfaceView = (SurfaceView) this
				.findViewById(R.id.ad_player_surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setKeepScreenOn(true);
		mSurfaceHolder.setFixedSize(dw, dh);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
		mSurfaceHolder.addCallback(new SurceCallBack());
	}
		
	@Override
	protected void onStart() {
		super.onStart();
	

	}
	

	private final class SurceCallBack implements SurfaceHolder.Callback {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			 if(adFile.exists()){
				 myVideoPlayUtil.playVideoUrl(adFile.getAbsolutePath(), mSurfaceHolder);
			 }
		        
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			myVideoPlayUtil.reset();
		}
	}

	

	private void startMainActivity() {
		Intent  intent = new Intent(AdplayerActivity.this, MainActivity.class);
		startActivity(intent);
		AdplayerActivity.this.finish();
	}
}
