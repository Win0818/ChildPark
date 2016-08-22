package com.worldchip.childpark.util;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import com.worldchip.childpark.Comments.Comments;

public class ViewShowTime {
	  protected static final String TAG = "CHRIS";
	  private static ViewShowTime mInstance;
      private Timer  mViewShowTimer;
      private TimerTask  mViewShowTimerTask;
      private final static  int VIEW_SHOW_TIME = 1000*15;
      private  static boolean mIsTouch = true;
      private  static boolean mIsShow = true;
      
      
      public static ViewShowTime getInstance() {
  		if (mInstance == null) {
  			synchronized (ViewShowTime.class) {
  				mInstance = new ViewShowTime();
  			}
  		}
  		return mInstance;
  	}
	
      
      public void startTimerTask(final Handler handler){
    	  stopGoldTimer();
          if(mViewShowTimer == null){
        	  mViewShowTimer = new Timer();
          }
    	  if(mViewShowTimer != null){
    		  mViewShowTimerTask  = new TimerTask() {
				@Override
		       public void run() {
			   if(!mIsTouch){
				   handler.removeMessages(Comments.VIDEO_SEEKBAR_GONE);
				   handler.sendEmptyMessage(Comments.VIDEO_SEEKBAR_GONE);
				   mIsShow = false;
			   }else{
				   mIsTouch = false;
			   }
					
			 }
	      };
	      mViewShowTimer.schedule(mViewShowTimerTask, 0 , VIEW_SHOW_TIME);
    	 }
    }    	  
      
      public void stopGoldTimer() {
  		if (mViewShowTimer != null) {
  			if (mViewShowTimerTask != null) {
  				mViewShowTimerTask.cancel();
  			}
  		}
  	}
      
      public void btnClickShowView(Handler  handler){
    	  mIsTouch = true;
    	  mIsShow = true;
    	  handler.removeMessages(Comments.VIDEO_SEEKBAR_SHOW);
    	  handler.sendEmptyMessage(Comments.VIDEO_SEEKBAR_SHOW);
      }
      
      
     public void reset() {
  		stopGoldTimer();
  		mViewShowTimer.cancel();
  		mIsShow = true;
  		mIsTouch = true;
  		mInstance = null;
  	}


	public static boolean ismIsTouch() {
		return mIsTouch;
	}


	public static void setmIsTouch(boolean mIsTouch) {
		ViewShowTime.mIsTouch = mIsTouch;
	}


	public static boolean ismIsShow() {
		return mIsShow;
	}


	public static void setmIsShow(boolean mIsShow) {
		ViewShowTime.mIsShow = mIsShow;
	}
          

}
