package com.worldchip.childpark.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.worldchip.childpark.R;

public class PlayPauseAdDialog  extends  Dialog {
	//private static  PlayPauseAdDialog  mPlayPauseAdDialog = null;
	private  AdDialogDimissListen   adDialogDimissListen = null;
	private  ImageView  mAdIv;
	private  ImageView  mAdDimiss;
	
	public  PlayPauseAdDialog(Context context,Bitmap bm) {
		super(context , R.style.password_dialog_style);
		View view  = LayoutInflater.from(context).inflate(R.layout.dialog_image_ad, null);
		mAdIv = (ImageView) view.findViewById(R.id.iv_dialog_ad);
		mAdDimiss = (ImageView) view.findViewById(R.id.iv_dialog_ad_dimiss);
		mAdIv.setImageBitmap(bm);
		mAdIv.setOnClickListener(new MyClickListen());
		mAdDimiss.setAlpha(0x22);
		this.setContentView(view);
		this.setCanceledOnTouchOutside(false);
	}
	
	
	public interface AdDialogDimissListen{
		 public void dismissDialog();
	}
	
	
	public void setAdDialogDimissListen(AdDialogDimissListen dimissListen){
		adDialogDimissListen = dimissListen;
	}


	public void setPauseIconInvisable(){
		mAdDimiss.setVisibility(View.GONE);
	}
	
	
	private  class MyClickListen implements  android.view.View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_dialog_ad:
				if(adDialogDimissListen != null)
					adDialogDimissListen.dismissDialog();
				break;
		}
		
	  } 
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == event.KEYCODE_BACK) {
				return false;
			}
		return super.onKeyDown(keyCode, event);
	}
	
	
	

}
