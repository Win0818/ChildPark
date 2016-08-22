package com.worldchip.childpark;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.dialog.PlayPauseAdDialog;
import com.worldchip.childpark.dialog.PlayPauseAdDialog.AdDialogDimissListen;
import com.worldchip.childpark.entity.VideoInfo;
import com.worldchip.childpark.util.MyVideoPlayUtil;
import com.worldchip.childpark.util.ViewShowTime;

public class VideoPlayerActivity extends BaseActivity implements OnClickListener,
		OnLongClickListener {

	private static final String TAG = "CHRIS";
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;

	private int mPosition = 0;
	private int mVedioId = 0;

	private MyVideoPlayUtil myVideoPlayUtil;
	private ViewShowTime mViewShowControl;

	private ArrayList<String> mVideoAddress;
	private ArrayList<String> mVideoName;
	private List<VideoInfo> mVideoInfos;

	private AudioManager mAudioManager;
	private int mCurrentVolume;

	private SeekBar mVideoPlayProBar;

	private LinearLayout mPlayLayout;

	private RelativeLayout mPlayRlLayout;

	private TextView mTvTimeLong, mTvTimeAllLong , mTvVideoName;

	private ImageButton mIbVideoPlay, mIbVideoLast, mIbVideoNext;
	
	private String mTimeLongStr = new String();
	private ProgressDialog dialog;

	private boolean mIsPlay = true;
	private int mMaxVolume = 20;
	
	private PlayPauseAdDialog  adDialog;
	
	private boolean mSeekBarIsShow = true;

	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Comments.VIDEO_SEEKBAR_GONE:
				if(mSeekBarIsShow){
				seekBarUnVisible(mPlayRlLayout,mPlayLayout);
				}
				break;

			case Comments.VIDEO_SEEKBAR_SHOW:
				if(!mSeekBarIsShow){
				seekBarVisible(mPlayRlLayout,mPlayLayout);
				}
				break;
			default:
				break;
			}

		};

	};

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_video_player);

		mVideoPlayProBar = (SeekBar) findViewById(R.id.seekbar_video_play);
		mPlayRlLayout = (RelativeLayout) findViewById(R.id.Llayout_seekbar_play);

		mPlayLayout = (LinearLayout) findViewById(R.id.Llayout_video_play);

		mTvTimeLong = (TextView) findViewById(R.id.tv_video_time_long);
		mTvTimeAllLong = (TextView) findViewById(R.id.tv_video_time_play);
		mTvVideoName = (TextView) findViewById(R.id.tv_video_name);
		
		mIbVideoLast = (ImageButton) findViewById(R.id.ib_video_play_last);
		mIbVideoNext = (ImageButton) findViewById(R.id.ib_video_play_next);
		mIbVideoPlay = (ImageButton) findViewById(R.id.ib_video_play);

		mIbVideoLast.setOnClickListener(this);
		mIbVideoLast.setOnLongClickListener(this);
		mIbVideoNext.setOnClickListener(this);
		mIbVideoNext.setOnLongClickListener(this);
		mIbVideoPlay.setOnClickListener(this);

		mVideoPlayProBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());

		myVideoPlayUtil = MyVideoPlayUtil.getInstance();
		myVideoPlayUtil.isAdplay(false);

		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mCurrentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		mVideoInfos = new ArrayList<VideoInfo>();

		dialog = new ProgressDialog(this);
		dialog.setMessage(getResources().getString(R.string.add_video));
		dialog.show();
		
		/*if(Comments.adBitMap == null){
		     Comments.adBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.album0);
		   }
		bm = Comments.adBitMap;*/
		
		myVideoPlayUtil.setSeekBar(mVideoPlayProBar);
		myVideoPlayUtil.setTextView(mTvTimeLong, mTvTimeAllLong);
		setPlayActivity(true);
		
		Bundle  bundle = getIntent().getExtras();
		mVideoAddress = bundle.getStringArrayList("video_address");
		mVideoName = bundle.getStringArrayList("video_name");
		mVedioId = bundle.getInt("position", 0);
		
		//System.out.println(mVideoAddress.size()+"---------mVideoAddress.get(mVedioId)----"+mVideoAddress.get(mVedioId));
		
		mTvVideoName.setText("");
		mViewShowControl = ViewShowTime.getInstance();
		mViewShowControl.startTimerTask(mHandler);
		
		initSelected();
		
	}
	
	private void initSelected() {
		mIbVideoPlay.setFocusable(true);
		mIbVideoPlay.setFocusableInTouchMode(true);
		mIbVideoPlay.requestFocus();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initBaseActivity();
		initSurfaceView();
		dialog.dismiss();
	}

	@SuppressWarnings("deprecation")
	private void initSurfaceView() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);

		int dw = getWindow().getDecorView().getWidth();
		int dh = getWindow().getDecorView().getHeight();

		mSurfaceView = (SurfaceView) this
				.findViewById(R.id.video_player_surface);
		mTvTimeLong.setText(mTimeLongStr);

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setKeepScreenOn(true);

		mSurfaceHolder.setFixedSize(dw, dh);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
		mSurfaceHolder.addCallback(new SurceCallBack());

	}

	private final class SurceCallBack implements SurfaceHolder.Callback {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if(mVideoAddress != null && mVideoAddress.size() > mVedioId){
				 myVideoPlayUtil.playVideoUrl(mVideoAddress.get(mVedioId), mSurfaceHolder);
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			myVideoPlayUtil.reset();
		}
	}

	
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			this.progress = progress * myVideoPlayUtil.mPlayer.getDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			myVideoPlayUtil.mPlayer.seekTo(progress);
		}

	}

	@Override
	public boolean onLongClick(View arg0) {
        Log.e(TAG, "----VideoPlayerActivity---onLongClick-------"+arg0.getId());
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_video_play_next:
			mViewShowControl.btnClickShowView(mHandler);
			Log.i(TAG, mVideoInfos.size()+"----ib_video_play_next--选择的视屏id号-----"+mVedioId);
			if (mVedioId < mVideoAddress.size() - 1) {
				mVedioId++;
				myVideoPlayUtil.reset();
				myVideoPlayUtil = MyVideoPlayUtil.getInstance();
				myVideoPlayUtil.playVideoUrl(mVideoAddress.get(mVedioId), mSurfaceHolder);
				myVideoPlayUtil.setSeekBar(mVideoPlayProBar);
				myVideoPlayUtil.setTextView(mTvTimeLong, mTvTimeAllLong);
				mTvVideoName.setText(mVideoName.get(mVedioId));
			}else{
				mVedioId = 0;
				myVideoPlayUtil.reset();
				myVideoPlayUtil = MyVideoPlayUtil.getInstance();
				myVideoPlayUtil.playVideoUrl(mVideoAddress.get(mVedioId), mSurfaceHolder);
				myVideoPlayUtil.setSeekBar(mVideoPlayProBar);
				myVideoPlayUtil.setTextView(mTvTimeLong, mTvTimeAllLong);
				mTvVideoName.setText(mVideoName.get(mVedioId));
				Log.i(TAG, "**********返回第一个视频**************");
			}
			break;

		case R.id.ib_video_play_last:
			Log.i(TAG, mVideoInfos.size()+"----ib_video_play_last--选择的视屏id号-----"+mVedioId);
			mViewShowControl.btnClickShowView(mHandler);
			if (mVedioId > 0) {
				mVedioId--;
				myVideoPlayUtil.reset();
				myVideoPlayUtil = MyVideoPlayUtil.getInstance();
				myVideoPlayUtil.playVideoUrl(mVideoAddress.get(mVedioId), mSurfaceHolder);
				myVideoPlayUtil.setSeekBar(mVideoPlayProBar);
				myVideoPlayUtil.setTextView(mTvTimeLong, mTvTimeAllLong);
				mTvVideoName.setText(mVideoName.get(mVedioId));
				Log.i(TAG, "**********上一个视屏**************");
			}else{
				mVedioId =  mVideoAddress.size() - 1;
				myVideoPlayUtil.reset();
				myVideoPlayUtil = MyVideoPlayUtil.getInstance();
				myVideoPlayUtil.playVideoUrl(mVideoAddress.get(mVedioId), mSurfaceHolder);
				myVideoPlayUtil.setSeekBar(mVideoPlayProBar);
				myVideoPlayUtil.setTextView(mTvTimeLong, mTvTimeAllLong);
				mTvVideoName.setText(mVideoName.get(mVedioId));
				Log.i(TAG, "**********返回最后一个视频**************");
			}
			break;

		case R.id.ib_video_play:
			mViewShowControl.btnClickShowView(mHandler);
			controlVideo();
			Log.i(TAG, "**********视屏播放**************");
			break;

		default:
			break;
		}
	}

	private void controlVideo() {
		if (mIsPlay) {
			mIsPlay = false;
			myVideoPlayUtil.pause();
			mIbVideoPlay.setBackgroundResource(R.drawable.music_pause_btn_selected);
		}else{
			mIsPlay = true;
			myVideoPlayUtil.play();
			mIbVideoPlay.setBackgroundResource(R.drawable.music_play_btn_selected);
		}
	}
	
	
/*	class MyAsyncTaskGetVideo extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			try {
				String str = HttpUtils.getRequest(Comments.NET_VIDEO_URL,
						VideoPlayerActivity.this);
				mVideoInfos = MyJsonParserUtil.parserMyVideojson(str);
				Log.i(TAG, str+"****************"+mVideoInfos.size());
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialog != null)
				dialog.dismiss();
			    //mTvVideoName.setText(mVideoInfos.get(mVedioId).getmVideoName());
		}
	}
*/
	@Override
	protected void onStop() {
		super.onStop();
		setPlayActivity(false);
		mViewShowControl.reset();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		Log.e(TAG, "event.getCode="+event.getKeyCode()+"; time="+event.getDownTime()+"; repeat="+event.getRepeatCount());
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			Log.i(TAG, "---VideoPlayerActivity--KEYCODE_VOLUME_DOWN------");
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			Log.i(TAG, "---VideoPlayerActivity---KEYCODE_DPAD_UP-------");
			return true;
		}
		
		switch (keyCode) {
		case 66:
			mViewShowControl.btnClickShowView(mHandler);
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	private void seekBarVisible(RelativeLayout rl,LinearLayout ly) {
		Animation animation = AnimationUtils.loadAnimation(
			VideoPlayerActivity.this, R.anim.anim_seekbar_alpha_visible);
	    rl.startAnimation(animation);
	    ly.startAnimation(animation);
	    mSeekBarIsShow = true;
	}
   
	
	private void seekBarUnVisible(RelativeLayout rl,LinearLayout ly) {
		Animation animation = AnimationUtils.loadAnimation(
			VideoPlayerActivity.this, R.anim.anim_seekbar_alpha_invisible);
	    rl.startAnimation(animation);
	    ly.startAnimation(animation);
	    mSeekBarIsShow  = false;
	}
	
   
	
}
