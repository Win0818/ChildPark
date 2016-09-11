package com.worldchip.childpark;


import com.worldchip.childpark.Comments.MySharePreData;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MySystemSetupActivity extends BaseActivity implements OnClickListener,
						OnCheckedChangeListener{
	
	private RadioGroup mDisplaySetting;
	private TextView mAppManager;
	private TextView mPatriarchMode; 
	private RadioGroup mWIFISetting;
	private RadioGroup mBluetoothSetting;
	private RadioGroup mTimeSetting;
	private RadioGroup mSoundSetting;
	private TextView mPasswordManager;
	private TextView mEnterSystemSetting;
	private Button mSystemSettingBack;
	private WifiManager wifiManager;
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
		mTimeSetting = (RadioGroup) findViewById(R.id.time_setting);
		mSoundSetting = (RadioGroup) findViewById(R.id.starting_up_sound_setting);
		
		mDisplaySetting.setOnCheckedChangeListener(this);
		mWIFISetting.setOnCheckedChangeListener(this);
		mBluetoothSetting.setOnCheckedChangeListener(this);
		mTimeSetting.setOnCheckedChangeListener(this);
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
		
	}
	
	private void initData() {
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
			Log.i(TAG, "R.id.app_manager");
			AllAppActivity.start(this);
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
			Log.i(TAG, "R.id.eyeshield_mode");
			MySharePreData.SetIntData(this, "my_system_setting", "display_setting", 0);
			break;
		case R.id.standard_mode:
			Log.i(TAG, "R.id.standard_mode");
			MySharePreData.SetIntData(this, "my_system_setting", "display_setting", 1);
			break;
		case R.id.hd_mode:
			Log.i(TAG, "R.id.hd_mode");
			MySharePreData.SetIntData(this, "my_system_setting", "display_setting", 2);
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
			break;
		case R.id.bluetooth_close:
			break;
		case R.id.time_auto_setting:
			break;
		case R.id.time_manual_setting:
			break;
		case R.id.starting_up_sound_open:
			break;
		case R.id.starting_up_sound_close:
			break;
		default:
			break;
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
	
}
