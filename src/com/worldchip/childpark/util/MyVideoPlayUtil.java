package com.worldchip.childpark.util;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.SeekBar;
import android.widget.TextView;

import com.worldchip.childpark.Comments.Comments;

public class MyVideoPlayUtil implements OnBufferingUpdateListener
   , OnCompletionListener, MediaPlayer.OnPreparedListener{
	
	public  MediaPlayer  mPlayer;
	private SeekBar skbProgress ;
	private Timer mTimer = new Timer();
	private static MyVideoPlayUtil mInstance = null;
	private String mMideoUrl="";
    private Long mVideoLong = 0L;
    private TextView mTVTimeLong;
    private TextView mTvTimeAllLong;
    private int mCurPosition;
    
    private boolean mIsAdplayFlag = false; 
    
    private AdPlayFinshListen adPlayFinshListen;
    
	private TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if(mPlayer == null){
				return ;
			}
			if (skbProgress != null) {
				if(mPlayer.isPlaying() && skbProgress.isPressed()== false){
					mHandler.sendEmptyMessage(0);
				}			
			}
		}
	};
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (mPlayer != null && skbProgress != null) {
				mCurPosition = mPlayer.getCurrentPosition();
				if (mVideoLong > 0 && mTVTimeLong != null){
					int pos = (int) (skbProgress.getMax() * mCurPosition / mVideoLong);
					mTVTimeLong.setText(Utils.timeFormat(mVideoLong - mCurPosition));
					mTvTimeAllLong.setText(Utils.timeFormat(mVideoLong));
					skbProgress.setProgress(pos);
				}
			}
		};
	};
	
	public static MyVideoPlayUtil getInstance() {
		if (mInstance == null) {
			mInstance = new MyVideoPlayUtil();
		}
		return mInstance;
	}
	
	public MyVideoPlayUtil() {
		mTimer.schedule(mTimerTask,0,500);
	}
	
	
	public void setSeekBar(SeekBar skbProgress) {
		this.skbProgress = skbProgress;
	}
	
	public void setTextView(TextView tv1,TextView tv2) {
		this.mTVTimeLong = tv1;
		this.mTvTimeAllLong  = tv2;
	}
	
	public void play(){
		if(mPlayer != null)
		mPlayer.start();
	}
	
	
	public void pause(){
		if(mPlayer != null){
			mPlayer.pause();
		}
	}
	
	public void stop(){
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
			Comments.MOV_TIME  = mVideoLong;
			Comments.MOVE_WATCH_TIME  = mCurPosition;
		}
		
	}
	
	public void playVideoUrl(String videoUrl ,SurfaceHolder  holder){
		if (videoUrl!= null && videoUrl.equals(mMideoUrl)) {
			return;
		}
		stop();
		try {
			mPlayer = new  MediaPlayer();
		    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    mPlayer.setOnBufferingUpdateListener(this);
		    mPlayer.setOnCompletionListener(this);
		    mPlayer.setOnPreparedListener(this);
			mPlayer.setDataSource(videoUrl);
			System.out.println("====playVideoUrl====videoUrl=="+videoUrl);
			mPlayer.setDisplay(holder);
			mPlayer.prepare();
			mMideoUrl= videoUrl;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void onBufferingUpdate(MediaPlayer player, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		if(mIsAdplayFlag){
			adPlayFinshListen.adIsPlayFinsh();
		}
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		player.start();
		mVideoLong = (long) player.getDuration();
	}
	
	
	public void reset() {
		stop();
		mMideoUrl = "";
		mPlayer = null;
		skbProgress = null;
		mVideoLong = 0L;
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mInstance = null;
	}
	
	public void isAdplay(boolean isAdplay){
		mIsAdplayFlag = isAdplay;
	}
	
	public void setAdPlayFinshListen(AdPlayFinshListen finshListen){
		this.adPlayFinshListen = finshListen;
	}
	
	public interface AdPlayFinshListen{
          public boolean adIsPlayFinsh();		
	}

}
