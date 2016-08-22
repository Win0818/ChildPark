package com.worldchip.childpark.dialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.worldchip.childpark.R;
import com.worldchip.childpark.Comments.Comments;

public class VolumeControlDialog extends Dialog {

	public static final String TAG = "CHRIS";
	private Timer mTimer;
	private VolumeShowTimerTask mTimeTask;
	private static VolumeControlDialog mVolumeDialog = null;

	private SeekBar mVolumeSeekBar;
	private int mMaxVolume;
	private AudioManager mAudioManager;
	private int mCurrentVolume;
	private static Context mContext;

	public interface PasswordValidateListener {
		public void onValidateComplete(boolean success);
	}

	public VolumeControlDialog(Context context) {
		super(context);
		createDialog(context);
	}

	public VolumeControlDialog(Context context, int theme) {
		super(context, theme);
	}

	public static VolumeControlDialog createDialog(Context context) {
		mContext = context;
		mVolumeDialog = new VolumeControlDialog(context,
				R.style.global_gold_dialog);
		mVolumeDialog.setContentView(R.layout.dialog_volume_control);
		Window dialogWindow = mVolumeDialog.getWindow();
		// dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
	    layoutParams.y = 50;
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(layoutParams);
		mVolumeDialog.setCancelable(false);
		return mVolumeDialog;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		initView();
	}

	private void initView() {
		if (mVolumeDialog == null) {
			return;
		}
		mVolumeSeekBar = (SeekBar) mVolumeDialog
				.findViewById(R.id.seekbar_volume_play);

		mAudioManager = (AudioManager) mContext
				.getSystemService(android.content.Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mVolumeSeekBar.setMax(mMaxVolume);
		mCurrentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mVolumeSeekBar.setProgress(mCurrentVolume);
		Log.i(TAG, "----------当前的音量大小---------" + mCurrentVolume);
	}

	public void showVolumeDialog() {
		if (mVolumeDialog != null) {
			mVolumeDialog.show();
			startVolumeDialogTime();
		}
	}

	public void dismissVolumeDialog() {
		if (mVolumeDialog != null && mVolumeDialog.isShowing()) {
			mVolumeDialog.dismiss();
			mVolumeDialog = null;
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		stopVolumeDialog();
		Comments.VOLUME_DIALOG_SHOW = false;
		Log.i(TAG, "----音量调节dialog是否显示------" + Comments.VOLUME_DIALOG_SHOW);
	}

	/**
	 * 开始记录弹出dialog的时间
	 */
	public void startVolumeDialogTime() {
		stopVolumeDialog();
		if (mTimer == null) {
			mTimer = new Timer(true);
		}
		if (mTimer != null && mTimeTask == null) {
			mTimeTask = new VolumeShowTimerTask();
			mTimer.schedule(mTimeTask, 10000);// 5秒后以delayTime为周期重复执行
		}
	}

	public void stopVolumeDialog() {
		if (mTimer != null) {
			if (mTimeTask != null) {
				mTimeTask.cancel();
				mTimer.cancel();
			}
		}
	}

	public class VolumeShowTimerTask extends TimerTask {
		@Override
		public void run() {
			Log.i(TAG, "----VolumeShowTimerTask-----");
			dismissVolumeDialog();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			Log.i(TAG, "---VolumeControlDialog--KEYCODE_VOLUME_DOWN--增大音量--------");
			mCurrentVolume--;
			mVolumeSeekBar.setProgress(mCurrentVolume);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					mCurrentVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			return true;

		case KeyEvent.KEYCODE_VOLUME_UP:
			Log.i(TAG, "---VolumeControlDialog-KEYCODE_VOLUME_UP---增大音量--------");
			mCurrentVolume++;
			mVolumeSeekBar.setProgress(mCurrentVolume);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					mCurrentVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			return true;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			Log.i(TAG, "---VolumeControlDialog-KEYCODE_DPAD_UP---增大音量--------");
			mCurrentVolume++;
			mVolumeSeekBar.setProgress(mCurrentVolume);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					mCurrentVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			return true;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			Log.i(TAG, "--VolumeControlDialog-KEYCODE_DPAD_DOWN----增大音量--------");
			mCurrentVolume--;
			mVolumeSeekBar.setProgress(mCurrentVolume);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					mCurrentVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			return true;
			
		case KeyEvent.KEYCODE_BACK:
			dismissVolumeDialog();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

}