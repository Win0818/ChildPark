package com.worldchip.childpark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Comment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.application.AppInfoData;
import com.worldchip.childpark.service.DownLoadAdVideoService;

public class MainActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "CHRIS";

	static final String VST_ACTION_FRONT = "myvst.intent.action."; // actionǰ׺
	static final String VST_URL_FRONT = "vst://myvst.v2/"; // urlǰ׺

	private ImageView mStoryHouseIv, mNurseryRhymeIv, mClassicsSinologyIv,
			mEnglishLearnIv, mAnimalWorldIv, mthinkLearnIv, mAppStoreIv,
			mPlayOnlineIv, mExtendsStoreIv, mSystemSettingIv;

	private RelativeLayout mStoryHouseRl, mNurseryRhymeRl, mClassicsSinologyRl,
			mEnglishLearnRl, mAnimalWorldRl, mthinkLearnRl, mAppStoreRl,
			mPlayOnlineRl, mExtendsStoreRl, mSystemSettingRl;

	private List<ImageView> mAllIcon = null;
	private List<RelativeLayout> mAllRelaLayout;
	private int mPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.e(TAG, "---MainActivity----onCreate-");

		startMediaUnMountedReceiver();

		initMainActivity();
		// downLoadPlayAd();

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mMediaMountedReceiver != null) {
			try {
				this.unregisterReceiver(mMediaMountedReceiver);
			} catch (IllegalArgumentException err) {
				return;
			} catch (Exception err) {
				return;
			}
		}
	}

	private void initMainActivity() {
		mAllIcon = new ArrayList<ImageView>();
		mAllRelaLayout = new ArrayList<RelativeLayout>();

		mStoryHouseIv = (ImageView) findViewById(R.id.iv_mian_story_house);
		mStoryHouseRl = (RelativeLayout) findViewById(R.id.Rl_main_story_house);
		addMyAllIcon(mStoryHouseIv);
		mAllRelaLayout.add(mStoryHouseRl);

		mNurseryRhymeIv = (ImageView) findViewById(R.id.iv_main_nursery_rhyme);
		mNurseryRhymeRl = (RelativeLayout) findViewById(R.id.Rl_main_nursery_rhyme);
		addMyAllIcon(mNurseryRhymeIv);
		mAllRelaLayout.add(mNurseryRhymeRl);

		mClassicsSinologyIv = (ImageView) findViewById(R.id.iv_main_classics_sinology);
		mClassicsSinologyRl = (RelativeLayout) findViewById(R.id.Rl_main_classics_sinology);
		addMyAllIcon(mClassicsSinologyIv);
		mAllRelaLayout.add(mClassicsSinologyRl);

		mEnglishLearnIv = (ImageView) findViewById(R.id.iv_main_english_learn);
		mEnglishLearnRl = (RelativeLayout) findViewById(R.id.Rl_main_english_learn);
		addMyAllIcon(mEnglishLearnIv);
		mAllRelaLayout.add(mEnglishLearnRl);

		mAnimalWorldIv = (ImageView) findViewById(R.id.iv_main_animal_world);
		mAnimalWorldRl = (RelativeLayout) findViewById(R.id.Rl_main_animal_world);
		addMyAllIcon(mAnimalWorldIv);
		mAllRelaLayout.add(mAnimalWorldRl);

		mthinkLearnIv = (ImageView) findViewById(R.id.iv_main_thinking_learn);
		mthinkLearnRl = (RelativeLayout) findViewById(R.id.Rl_main_thinking_learn);
		addMyAllIcon(mthinkLearnIv);
		mAllRelaLayout.add(mthinkLearnRl);

		mAppStoreIv = (ImageView) findViewById(R.id.iv_main_app_store);
		mAppStoreRl = (RelativeLayout) findViewById(R.id.Rl_main_app_store);
		addMyAllIcon(mAppStoreIv);
		mAllRelaLayout.add(mAppStoreRl);

		mPlayOnlineIv = (ImageView) findViewById(R.id.iv_main_play_online);
		mPlayOnlineRl = (RelativeLayout) findViewById(R.id.Rl_main_play_online);
		addMyAllIcon(mPlayOnlineIv);
		mAllRelaLayout.add(mPlayOnlineRl);

		mExtendsStoreIv = (ImageView) findViewById(R.id.iv_main_extends_store);
		mExtendsStoreRl = (RelativeLayout) findViewById(R.id.Rl_main_extends_store);
		addMyAllIcon(mExtendsStoreIv);
		mAllRelaLayout.add(mExtendsStoreRl);

		mSystemSettingIv = (ImageView) findViewById(R.id.iv_main_system_setting);
		mSystemSettingRl = (RelativeLayout) findViewById(R.id.Rl_main_system_setting);
		addMyAllIcon(mSystemSettingIv);
		mAllRelaLayout.add(mSystemSettingRl);

		mStoryHouseRl.setOnClickListener(this);
		mNurseryRhymeRl.setOnClickListener(this);
		mClassicsSinologyRl.setOnClickListener(this);
		mEnglishLearnRl.setOnClickListener(this);
		mAnimalWorldRl.setOnClickListener(this);
		mthinkLearnRl.setOnClickListener(this);
		mAppStoreRl.setOnClickListener(this);
		mExtendsStoreRl.setOnClickListener(this);
		mPlayOnlineRl.setOnClickListener(this);
		mSystemSettingRl.setOnClickListener(this);

		/*
		 * int count = MySharePreData.GetIntData(this,Comments.SP_FIlE_NAMW,
		 * "count");
		 * 
		 * System.out.println("-----MainActivity------count = ------"+count);
		 * 
		 * if(count <= 20){ count++; MySharePreData.SetIntData(this,
		 * Comments.SP_FIlE_NAMW, "count", count); }else{
		 * mDreamRl.setClickable(false); mMusicRl.setClickable(false);
		 * mAblumRl.setClickable(false); mEmotionRl.setClickable(false);
		 * mStudyRl.setClickable(false); mSettingRl.setClickable(false); }
		 */
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPosition = Comments.MAIN_POSITION;
	}

	private void downLoadPlayAd() {
		Intent intent = new Intent(MainActivity.this,
				DownLoadAdVideoService.class);
		startService(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initBaseActivity();
		initSelected();
		if(Comments.MAIN_POSITION < 0 || Comments.MAIN_POSITION >= mAllIcon.size()){
			Comments.MAIN_POSITION = 0;
		}
		setSlectedIv(Comments.MAIN_POSITION);
	}
	
	@Override
	protected void onDestroy() {
		Log.i(TAG, "----MainActivity--onDestroy----");
		MySharePreData.SetBooleanData(this, Comments.SP_FIlE_NAMW, "play_ad",
				true);
		super.onDestroy();
	}

	private void initSelected() {
		mAllIcon.get(mPosition).setSelected(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Rl_main_story_house:
			setSlectedIv(0);
			skipLocalVideoListActivity(0);
			break;
		case R.id.Rl_main_nursery_rhyme:
			setSlectedIv(1);
			skipLocalVideoListActivity(1);
			break;
		case R.id.Rl_main_classics_sinology:
			setSlectedIv(2);
			skipLocalVideoListActivity(2);
			break;
		case R.id.Rl_main_english_learn:
			setSlectedIv(3);
			skipLocalVideoListActivity(3);
			break;
		case R.id.Rl_main_animal_world:
			setSlectedIv(4);
			skipLocalVideoListActivity(4);
			break;
		case R.id.Rl_main_thinking_learn: //���߶�����VST
			setSlectedIv(5);
			//skipLocalVideoListActivity(5);
			openVstapp(getApplicationContext());
			break;
		case R.id.Rl_main_app_store: //����Ӧ��
			setSlectedIv(6);
			//openVstapp(getApplicationContext());
			startChildAppActivity(6);
			break;
		case R.id.Rl_main_play_online:  //�ⲿ�洢
			setSlectedIv(7);
			//openChildContent();
			skipLocalVideoListActivity(-1);
			break;
		case R.id.Rl_main_extends_store: //�������
			setSlectedIv(8);
			// openUsbContent();
			//skipLocalVideoListActivity(-1);
			//showVersion();
			startVersionActivity();
			break;
		case R.id.Rl_main_system_setting:
			setSlectedIv(9);
			//openSystemSetting();
			startMySystemSetupActivity();
			break;
		default:
			break;
		}
	}

	private void showVersion() {
		
		new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.app_version))  
	     .setMessage("Version: "+AppInfoData.getAppVersionName(MainActivity.this))	  
	     .setPositiveButton("OK",new DialogInterface.OnClickListener() {//���ȷ����ť  
	         @Override  
	         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
	        	 dialog.dismiss();
	         }  
	     }).show();
	 }  


	private void addMyAllIcon(ImageView v) {
		if (mAllIcon != null) {
			mAllIcon.add(v);
		}
	}

	private void setSlectedIv(int position) {
		Comments.MAIN_POSITION = mPosition;
		if (mAllIcon != null && mAllIcon.size() > 0) {
			for (int i = 0; i < mAllIcon.size(); i++) {
				if (position == i) {
					mAllIcon.get(i).setSelected(true);
				} else {
					mAllIcon.get(i).setSelected(false);
				}
			}
		}
	}
	 private void startVersionActivity() {
	    	Intent intent = new Intent(MainActivity.this,
					VersionActivity.class);
			try{
			    startActivity(intent);
			}catch(Exception err){
				Toast.makeText(MainActivity.this,
						getString(R.string.all_app_error) + err.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}

    private void startChildAppActivity(int i) {
    	 Intent	intent = new Intent(MainActivity.this,
    				ChildAppActivity.class);
		intent.putExtra("category", i);
		try{
		    startActivity(intent);
		}catch(Exception err){
			Toast.makeText(MainActivity.this,
					getString(R.string.all_app_error) + err.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}


	private void skipLocalVideoListActivity(int i) {
		
		if (i == -1) {
			//com.android.rockchip/.RockExplorer
		    ComponentName component = new ComponentName("com.android.rockchip", 
		    		"com.android.rockchip.RockExplorer");
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(component);
			startActivity(intent);
		} else {
			Intent intent = new Intent(MainActivity.this,
					LocalVideoShowActivity.class);
			intent.putExtra("category", i);
			startActivity(intent);
		}
	}

	private void openVstapp(Context context) {
		try {
			LaunchBean tempBean1 = new LaunchBean("����VST���", VST_ACTION_FRONT
					+ "LancherActivity", VST_URL_FRONT + "recmmend/lancher");
			Intent intent = launchByAction(tempBean1);
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(MainActivity.this,
					getString(R.string.vst_open_error) + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void openChildContent() {
		try {
			Intent intent = new Intent(); 
			intent.setAction("android.intent.action.vst.children"); 
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(MainActivity.this,
					getString(R.string.vst_open_error) + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void openUsbContent() {
		if (Comments.USB_IS_MONT) {
			Toast.makeText(MainActivity.this, getString(R.string.usb_mount),
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(MainActivity.this, getString(R.string.usb_unmount),
					Toast.LENGTH_LONG).show();
		}
	}
	private void startMySystemSetupActivity() {
		Intent intent = new Intent(MainActivity.this,
				MySystemSetupActivity.class);
		try{
		    startActivity(intent);
		}catch(Exception err){
			Toast.makeText(MainActivity.this,
					getString(R.string.all_app_error) + err.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void openSystemSetting() {
		Intent settingIntent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.Settings");
		settingIntent.setComponent(cm);
		settingIntent.setAction("android.intent.action.VIEW");
		startActivity(settingIntent);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.e(TAG, "dispatchKeyEvent...keyCode=" + event.getKeyCode());
		if (event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mPosition--;
				if (mPosition == -1) {
					mPosition = 9;
				}
				setSlectedIv(mPosition);
				return false;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				mPosition++;
				if (mPosition == 10) {
					mPosition = 0;
				}
				setSlectedIv(mPosition);
				return false;
			case KeyEvent.KEYCODE_DPAD_UP:
				if (mPosition >= 5 && mPosition < 10) {
					mPosition = mPosition - 5;
				}
				setSlectedIv(mPosition);
				return false;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (mPosition >= 0 && mPosition < 5) {
					mPosition = mPosition + 5;
				}
				setSlectedIv(mPosition);
				return false;
			case 66:
				if (0 <= mPosition && mPosition <= 5) {
					skipLocalVideoListActivity(mPosition);
					//openVstapp(getApplicationContext());
				} else {
					switch (mPosition) {
					case 6:
						setSlectedIv(6);
						//openVstapp(getApplicationContext());
						startChildAppActivity(6);
						break;
					case 7:
						setSlectedIv(7);
						//openChildContent();
						skipLocalVideoListActivity(-1);
						break;
					case 8:
						setSlectedIv(8);
						// openUsbContent();
						//skipLocalVideoListActivity(-1);
						//showVersion();
						startVersionActivity();
						break;
					case 9:
						setSlectedIv(9);
						//openSystemSetting();
						startMySystemSetupActivity();
						break;
					default:
						break;
					}
				}
				return false;
			}
		}
		return super.dispatchKeyEvent(event);
	};

	private Intent launchByAction(LaunchBean bean) {
		Intent intent = new Intent(bean.action);
		return intent;
	}

	private Intent launchByUrl(LaunchBean bean) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(bean.url));
		return intent;
	}

	class LaunchBean {
		public String target;
		public String action;
		public String url;
		public Map<String, Object> extras;

		public LaunchBean(String target, String action, String url) {
			this.target = target;
			this.action = action;
			this.url = url;
		}
	}
	
	 /*@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    		moveTaskToBack(false);
	    		return true;
	    	}
	    	return super.onKeyDown(keyCode, event);
	   }
*/
	private Handler mMyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Comments.TAG_MEDIA_IN:
				// test.setText(msg.obj.toString());
				break;
			case Comments.TAG_USB_IN:
				Comments.EXTUSBPATH = msg.obj.toString();
				// test.setText(Comments.EXTUSBPATH);
				break;
			case Comments.TAG_SDCARD_IN:
				Comments.EXTSDPATH = msg.obj.toString();
				// test.setText(Comments.EXTSDPATH);
				break;
			}
		}
	};

	private void startMediaUnMountedReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.setPriority(2147483647);
		intentFilter.addDataScheme("file");
		registerReceiver(mMediaMountedReceiver, intentFilter);
	}

	private BroadcastReceiver mMediaMountedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {

				String path = intent.getData().getPath();

				// Toast.makeText(MainActivity.this, "Media Mounted: "+path,
				// Toast.LENGTH_LONG).show();
				Message message1 = mMyHandler.obtainMessage();
				message1.what = Comments.TAG_MEDIA_IN;
				message1.obj = path;
				mMyHandler.sendMessage(message1);

				if (path.contains("usb")) {
					Toast.makeText(
							MainActivity.this,
							getResources().getString(R.string.extusb_path)
									+ " " + path, Toast.LENGTH_LONG).show();
					Log.e(TAG, "Media mounted! path = " + path);
					Message message = mMyHandler.obtainMessage();
					message.what = Comments.TAG_USB_IN;
					message.obj = path;
					mMyHandler.sendMessageDelayed(message, 100);
				}
				if (path.contains("sd")) {
					Toast.makeText(
							MainActivity.this,
							getResources().getString(R.string.extsdcard_path)
									+ " " + path, Toast.LENGTH_LONG).show();
					Log.e(TAG, "Media mounted! path = " + path);
					Message message = mMyHandler.obtainMessage();
					message.what = Comments.TAG_SDCARD_IN;
					message.obj = path;
					mMyHandler.sendMessageDelayed(message, 100);
				}
			}
		}
	};
}
