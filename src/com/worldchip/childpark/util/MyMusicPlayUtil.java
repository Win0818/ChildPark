package com.worldchip.childpark.util;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyMusicPlayUtil implements OnBufferingUpdateListener
   , OnCompletionListener, MediaPlayer.OnPreparedListener{
	public  MediaPlayer  mPlayer;
	private SeekBar skbProgress ;
	private Timer mTimer = new Timer();
	private static MyMusicPlayUtil mInstance = null;
	private String mMideoUrl="";
	private int mPosition;
	private Long  mMusicLong;
	private TextView  mMusicPlayTv, mMusicLongTv;
	
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
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mPlayer != null && skbProgress != null) {
				mPosition = mPlayer.getCurrentPosition();
				mMusicLong = (long) mPlayer.getDuration();
				if (mMusicLong > 0 && mMusicLongTv  != null ) {
					int pos = (int) (skbProgress.getMax() * mPosition / mMusicLong);
					mMusicPlayTv.setText(Utils.timeFormat(mMusicLong - mPosition));
					mMusicLongTv.setText(Utils.timeFormat(mMusicLong));
					skbProgress.setProgress(pos);
				}
			}
		};
	};
	
	
	
	public static MyMusicPlayUtil getInstance() {
		if (mInstance == null) {
			mInstance = new MyMusicPlayUtil();
		}
		return mInstance;
	}
	
	public MyMusicPlayUtil() {
		mTimer.schedule(mTimerTask,0,500);
	}
	
	
	public void setSeekBar(SeekBar skbProgress) {
		this.skbProgress = skbProgress;
	}
	
	public int  getPlayPosition(){
         return  mPosition;		
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
		}
		
	}
	
	public void playMusicUrl(String videoUrl ,int playPosition){
		if (videoUrl!= null && videoUrl.equals(mMideoUrl)) {
			return;
		}
		stop();
		try {
			mPlayer = new  MediaPlayer();
		    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    mPlayer.setOnBufferingUpdateListener(this);
		    mPlayer.setOnPreparedListener(this);
			mPlayer.setDataSource(videoUrl);
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
		
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		player.start();
		if(mPosition > 0 ){
			player.seekTo(mPosition);
		}
	}
	
	
	public void reset() {
		stop();
		mMideoUrl = "";
		mPlayer = null;
		skbProgress = null;
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mInstance = null;
	}

	public void setTextView(TextView mTvTimePaly, TextView mTvTimeAllLong) {
		mMusicPlayTv = mTvTimePaly;
		mMusicLongTv = mTvTimeAllLong;
		
	}

}
