package com.worldchip.childpark;


import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.application.MyApplication;
import com.worldchip.childpark.util.Utils;
import com.worldchip.childpark.view.PasswordInputDialog;
import com.worldchip.childpark.view.PasswordInputDialog.PasswordValidateListener;

public class MySystemSetupActivity extends BaseActivity implements OnClickListener,
						OnCheckedChangeListener,CompoundButton.OnCheckedChangeListener{
	
	private RadioGroup mDisplaySetting;
	private RadioButton mEyeShieled, mStanderd, mHD;
	private ImageView mAppManager;
	private ImageView mPatriarchMode; 
	private ImageView mStartSoundImg;
	private ImageView mWifiImg;
	private ImageView mBltetoothImg;
	private CheckBox mWIFISetting;
	private CheckBox mBluetoothSetting;
	private CheckBox mSoundSetting;
	private ImageView mPasswordManager;
	private ImageView mEnterSystemSetting;
	private Button mSystemSettingBack;
	private WifiManager wifiManager;
	private BluetoothAdapter mBluetoothAdapter;
	private PasswordInputDialog mPasswordInputDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysystemsetup_2);
		
		initData();
		initView();
		
	}
	
	private void initView() {
		mDisplaySetting = (RadioGroup) findViewById(R.id.display_setting);
		mEyeShieled = (RadioButton) findViewById(R.id.eyeshield_mode);
		mStanderd = (RadioButton) findViewById(R.id.standard_mode);
		mHD = (RadioButton) findViewById(R.id.hd_mode);
		mWIFISetting = (CheckBox) findViewById(R.id.wifi_switch);
		mBluetoothSetting = (CheckBox) findViewById(R.id.bluetooth_switch);
		mSoundSetting = (CheckBox) findViewById(R.id.start_sound_switch);
		
		mDisplaySetting.setOnCheckedChangeListener(this);
		mWIFISetting.setOnCheckedChangeListener(this);
		mBluetoothSetting.setOnCheckedChangeListener(this);
		mSoundSetting.setOnCheckedChangeListener(this);
		mAppManager = (ImageView) findViewById(R.id.app_manager);
		mAppManager.setOnClickListener(this);
		mPasswordManager = (ImageView)findViewById(R.id.password_manager);
		mEnterSystemSetting = (ImageView)findViewById(R.id.enter_system_setting);
		mPasswordManager.setOnClickListener(this);
		mEnterSystemSetting.setOnClickListener(this);
		mPatriarchMode = (ImageView)findViewById(R.id.patriarch_mode);
		mPatriarchMode.setOnClickListener(this);
		mSystemSettingBack = (Button) findViewById(R.id.system_setting_backbtn);
		mSystemSettingBack.setOnClickListener(this);
		
		mStartSoundImg = (ImageView) findViewById(R.id.start_sound_imageview);
		mWifiImg = (ImageView) findViewById(R.id.wifi_imageview);
		mBltetoothImg = (ImageView) findViewById(R.id.bluetooth_imageview);
		
	//	RadioButton wifiOpen = (RadioButton) findViewById(R.id.wifi_open);
	//	RadioButton wifiClose = (RadioButton) findViewById(R.id.wifi_close);
		
		// bluetoothOpen = (RadioButton) findViewById(R.id.bluetooth_open);
		//RadioButton bluetoothClose = (RadioButton) findViewById(R.id.bluetooth_close);
	//
		
		
	}
	
	private void initData() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		/*mBluetoothAdapter.enable();
		mBluetoothAdapter.disable();*/
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initBaseActivity();
		refreshView();
	}
	
	private void refreshView() {
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			mWIFISetting.setChecked(true);
		} else {
			mWIFISetting.setChecked(false);
		}
		if (mBluetoothAdapter.isEnabled()) {
			mBluetoothSetting.setChecked(true);
		} else {
			mBluetoothSetting.setChecked(false); 
		}
		
		if (MySharePreData.GetBooleanTrueData(MyApplication.getApplicationContex(), 
				"child_park", "open_sound")) {
			mSoundSetting.setChecked(true);
			mStartSoundImg.setBackgroundResource(R.drawable.close);
		} else {
			mSoundSetting.setChecked(false);
			mStartSoundImg.setBackgroundResource(R.drawable.open_hover);
		}
		if (mWIFISetting.isChecked()) {
			mWifiImg.setBackgroundResource(R.drawable.close);
		} else {
			mWifiImg.setBackgroundResource(R.drawable.open_hover);
		}
		if (mBluetoothSetting.isChecked()) {
			mBltetoothImg.setBackgroundResource(R.drawable.close);
		} else {
			mBltetoothImg.setBackgroundResource(R.drawable.open_hover);
		}
		
		int mBrightnessVolume = Settings.System.getInt(
				MySystemSetupActivity.this.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, -1);
    	if (mBrightnessVolume >= 0 && mBrightnessVolume <= 100) {
    		mEyeShieled.setChecked(true);
    	} else if (mBrightnessVolume > 100 && mBrightnessVolume <= 200) {
    		mStanderd.setChecked(true);
    	} else if (mBrightnessVolume > 200 && mBrightnessVolume <= 255) {
    		mHD.setChecked(true);
    	}
		
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_manager:
			Log.i(TAG, "R.id.app_manager");
			 showPasswordInputView(0);
			//AllAppActivity.start(this);
			break;
		case R.id.patriarch_mode:
			Log.i(TAG, "R.id.patriarch_mode");
			 showPasswordInputView(1);
			break;
		case R.id.password_manager:
			PasswordManagerActivity.start(MySystemSetupActivity.this);
			break;
		case R.id.enter_system_setting:
			openSystemSetting();
			break;
		case R.id.system_setting_backbtn:
			finish();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.start_sound_switch:
			Log.i(TAG, "R.id.start_sound_switch");
			if (isChecked) {
				MySharePreData.SetBooleanData(this, "child_park", "open_sound", true);
				mStartSoundImg.setBackgroundResource(R.drawable.close);
			} else {
				MySharePreData.SetBooleanData(this, "child_park", "open_sound", false);
				mStartSoundImg.setBackgroundResource(R.drawable.open_hover);
			}
			
			break;
		case R.id.wifi_switch:
			Log.i(TAG, "R.id.wifi_switch");
			if (isChecked) {
				if (wifiManager != null) { 
					wifiManager.setWifiEnabled(true);
					mWifiImg.setBackgroundResource(R.drawable.close);
				}
			} else {
				if (wifiManager != null) {
					wifiManager.setWifiEnabled(false);
					mWifiImg.setBackgroundResource(R.drawable.open_hover);
				}
			}
			break;
		case R.id.bluetooth_switch:
			Log.i(TAG, "R.id.bluetooth_switch");
			if (isChecked) {
				if (mBluetoothAdapter != null) {
					Log.i(TAG, "R.id.bluetooth_switch enable");
					mBluetoothAdapter.enable();
					mBltetoothImg.setBackgroundResource(R.drawable.close);
				}
			} else {
				if (mBluetoothAdapter != null) {
					Log.i(TAG, "R.id.bluetooth_switch disable");
					mBluetoothAdapter.disable();
					mBltetoothImg.setBackgroundResource(R.drawable.open_hover);
				}
			}
			break;
		default:
			break;
		}
		
	}
	private void setBrightness(int index) {
		Uri uri = android.provider.Settings.System
				.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
		ContentResolver resolver = MySystemSetupActivity.this
				.getContentResolver();
		android.provider.Settings.System.putInt(resolver,
				Settings.System.SCREEN_BRIGHTNESS, index);
		resolver.notifyChange(uri, null);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.eyeshield_mode:
			setScreenMode(0);
			Log.i(TAG, "R.id.eyeshield_mode");
			MySharePreData.SetIntData(MyApplication.getApplicationContex(), "my_system_setting", "display_setting", 0);
			setBrightness(100);
			mTvMode.setText(this.getResources().getString(R.string.system_mode_eyeshield));
			break;
		case R.id.standard_mode:
			setScreenMode(0);
			Log.i(TAG, "R.id.standard_mode");
			MySharePreData.SetIntData(MyApplication.getApplicationContex(), "my_system_setting", "display_setting", 1);
			setBrightness(153);
			mTvMode.setText(MyApplication.getApplicationContex().getResources().getString(R.string.system_mode_standard));
			break;
		case R.id.hd_mode:
			setScreenMode(0);
			Log.i(TAG, "R.id.hd_mode");
			MySharePreData.SetIntData(MyApplication.getApplicationContex(), "my_system_setting", "display_setting", 2);
			setBrightness(255);
			mTvMode.setText(MyApplication.getApplicationContex().getResources().getString(R.string.system_mode_hd));
			break;
		/*case R.id.wifi_open:
			Log.i(TAG, "R.id.wifi_open");
			if (wifiManager != null) {
				wifiManager.setWifiEnabled(true);
			}
			break;
		case R.id.wifi_close:
			Log.i(TAG, "R.id.wifi_close");
			if (wifiManager != null) {
				wifiManager.setWifiEnabled(false);
			}
			break;
		case R.id.bluetooth_open:
			if (mBluetoothAdapter != null) {
				mBluetoothAdapter.enable();
			}
			break;
		case R.id.bluetooth_close:
			if (mBluetoothAdapter != null) {
				mBluetoothAdapter.disable();
			}
			break;
		case R.id.starting_up_sound_open:
			MySharePreData.SetBooleanData(this, "child_park", "open_sound", true);
			break;
		case R.id.starting_up_sound_close:
			MySharePreData.SetBooleanData(this, "child_park", "open_sound", false);
			break;*/
		default:
			break;
		}
	}
	/**
	* 保存当前的屏幕亮度值，并使之生效
	*/
	private void setScreenBrightness(int paramInt){
		Window localWindow = getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
		float f = paramInt / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}
	
	/**
	* 设置当前屏幕亮度的模式 
	* SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	* SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	*/
	private void setScreenMode(int paramInt){
		try{
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
	
	private void openSystemSetting() {
		Intent settingIntent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.Settings");
		settingIntent.setComponent(cm);
		settingIntent.setAction("android.intent.action.VIEW");
		startActivity(settingIntent);
	}
	/**
	 * 启动公版桌面
	 */
	
	/*private void launchAndroid() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ComponentName cn = new ComponentName("com.android.LauncherW2_haiway",
				"com.android.LauncherW2_haiway.Launcher");
		intent.setComponent(cn);
		this.startActivity(intent);
	}*/
	
    /**
     * 启动一个app
     */
	public void startAPP(String appPackageName){
	    try{
	        Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
	        startActivity(intent);
	    }catch(Exception e){
	    	Utils.showToastMessage(this, "没有安装");
	    }
    }
	/**
	 * 启动公版桌面
	 */
	
	private void launchAndroid() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ComponentName cn = new ComponentName("com.android.LauncherW2_haiway",
				"com.android.LauncherW2_haiway.Launcher");
		intent.setComponent(cn);
		this.startActivity(intent);
	}
	
	private void showPasswordInputView(final int modle) {
		if (mPasswordInputDialog != null) {
			if (mPasswordInputDialog.isShowing()) {
				mPasswordInputDialog.dismiss();
				mPasswordInputDialog = null;
			}
		}
		mPasswordInputDialog = PasswordInputDialog.createDialog(this);
		mPasswordInputDialog.setListener(new PasswordValidateListener() {
			@Override
			public void onValidateComplete(boolean success) {
				
					if (success) {
						if (modle == 0) {
							AllAppActivity.start(MySystemSetupActivity.this);
						} else if (modle == 1) {
							launchAndroid();
							//startAPP("com.android.settings");
							//startAPP("com.android.LauncherW2_haiway");
						}
						
					} 
				
			}
		});
		mPasswordInputDialog.show();
	}
	
}
