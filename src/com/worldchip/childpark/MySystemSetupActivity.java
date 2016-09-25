package com.worldchip.childpark;


import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.application.MyApplication;
import com.worldchip.childpark.util.Utils;
import com.worldchip.childpark.view.PasswordInputDialog;
import com.worldchip.childpark.view.PasswordInputDialog.PasswordValidateListener;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MySystemSetupActivity extends BaseActivity implements OnClickListener,
						OnCheckedChangeListener{
	
	private RadioGroup mDisplaySetting;
	private TextView mAppManager;
	private TextView mPatriarchMode; 
	private RadioGroup mWIFISetting;
	private RadioGroup mBluetoothSetting;
	private RadioGroup mSoundSetting;
	private TextView mPasswordManager;
	private TextView mEnterSystemSetting;
	private Button mSystemSettingBack;
	private WifiManager wifiManager;
	private BluetoothAdapter mBluetoothAdapter;
	private PasswordInputDialog mPasswordInputDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysystemsetup);
		
		initData();
		initView();
		
	}
	
	private void initView() {
		mDisplaySetting = (RadioGroup) findViewById(R.id.display_setting);
		mWIFISetting = (RadioGroup) findViewById(R.id.wifi_setting);
		mBluetoothSetting = (RadioGroup) findViewById(R.id.bluetooth_setting);
		mSoundSetting = (RadioGroup) findViewById(R.id.starting_up_sound_setting);
		
		mDisplaySetting.setOnCheckedChangeListener(this);
		mWIFISetting.setOnCheckedChangeListener(this);
		mBluetoothSetting.setOnCheckedChangeListener(this);
		mSoundSetting.setOnCheckedChangeListener(this);
		mAppManager = (TextView) findViewById(R.id.app_manager);
		mAppManager.setOnClickListener(this);
		mPasswordManager = (TextView)findViewById(R.id.password_manager);
		mEnterSystemSetting = (TextView)findViewById(R.id.enter_system_setting);
		mPasswordManager.setOnClickListener(this);
		mEnterSystemSetting.setOnClickListener(this);
		mPatriarchMode = (TextView)findViewById(R.id.patriarch_mode);
		mPatriarchMode.setOnClickListener(this);
		mSystemSettingBack = (Button) findViewById(R.id.system_setting_backbtn);
		mSystemSettingBack.setOnClickListener(this);
		
		RadioButton wifiOpen = (RadioButton) findViewById(R.id.wifi_open);
		RadioButton wifiClose = (RadioButton) findViewById(R.id.wifi_close);
		
		RadioButton bluetoothOpen = (RadioButton) findViewById(R.id.bluetooth_open);
		RadioButton bluetoothClose = (RadioButton) findViewById(R.id.bluetooth_close);
		
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		if (wifiManager.isWifiEnabled()) {
			wifiOpen.setChecked(true);
		} else {
			wifiClose.setChecked(true);
		}
		if (mBluetoothAdapter.isEnabled()) {
			bluetoothOpen.setChecked(true);
		} else {
			bluetoothClose.setChecked(true);
		}
		
	}
	
	private void initData() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothAdapter.enable();
		mBluetoothAdapter.disable();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initBaseActivity();
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_manager:
			Log.i(TAG, "R.id.app_manager");
			AllAppActivity.start(this);
			break;
		case R.id.patriarch_mode:
			Log.i(TAG, "R.id.patriarch_mode");
			 showPasswordInputView();
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.eyeshield_mode:
			setScreenMode(0);
			Log.i(TAG, "R.id.eyeshield_mode");
			MySharePreData.SetIntData(this, "my_system_setting", "display_setting", 0);
			setScreenBrightness(30);
			mTvMode.setText(this.getResources().getString(R.string.system_mode_eyeshield));
			break;
		case R.id.standard_mode:
			setScreenMode(0);
			Log.i(TAG, "R.id.standard_mode");
			MySharePreData.SetIntData(this, "my_system_setting", "display_setting", 1);
			setScreenBrightness(100);
			mTvMode.setText(this.getResources().getString(R.string.system_mode_standard));
			break;
		case R.id.hd_mode:
			setScreenMode(0);
			Log.i(TAG, "R.id.hd_mode");
			MySharePreData.SetIntData(this, "my_system_setting", "display_setting", 2);
			setScreenBrightness(180);
			mTvMode.setText(this.getResources().getString(R.string.system_mode_hd));
			break;
		case R.id.wifi_open:
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
			break;
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
	
	private void showPasswordInputView() {
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
					startAPP("com.android.LauncherW2_haiway");
				}
			}
		});
		mPasswordInputDialog.show();
	}
	
}
