package com.worldchip.childpark;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MySystemSetupActivity extends BaseActivity implements OnClickListener,
						OnCheckedChangeListener{
	
	private RadioGroup mDisplaySetting;
	private RadioGroup mDesktopSetting;
	private RadioGroup mWIFISetting;
	private RadioGroup mBluetoothSetting;
	private RadioGroup mTimeSetting;
	private RadioGroup mSoundSetting;
	private TextView mPasswordManager;
	private TextView mEnterSystemSetting;
	private Button mSystemSettingBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysystemsetup);
		
		initView();
	}
	
	private void initView() {
		mDisplaySetting = (RadioGroup) findViewById(R.id.display_setting);
		mDesktopSetting = (RadioGroup) findViewById(R.id.desktop_setting);
		mWIFISetting = (RadioGroup) findViewById(R.id.wifi_setting);
		mBluetoothSetting = (RadioGroup) findViewById(R.id.bluetooth_setting);
		mTimeSetting = (RadioGroup) findViewById(R.id.time_setting);
		mSoundSetting = (RadioGroup) findViewById(R.id.starting_up_sound_setting);
		
		mDisplaySetting.setOnCheckedChangeListener(this);
		mDesktopSetting.setOnCheckedChangeListener(this);
		mWIFISetting.setOnCheckedChangeListener(this);
		mBluetoothSetting.setOnCheckedChangeListener(this);
		mTimeSetting.setOnCheckedChangeListener(this);
		mSoundSetting.setOnCheckedChangeListener(this);
		
		mPasswordManager = (TextView)findViewById(R.id.password_manager);
		mEnterSystemSetting = (TextView)findViewById(R.id.enter_system_setting);
		mPasswordManager.setOnClickListener(this);
		mEnterSystemSetting.setOnClickListener(this);
		
		mSystemSettingBack = (Button) findViewById(R.id.system_setting_backbtn);
		mSystemSettingBack.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initBaseActivity();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.password_manager:
			
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
			
			break;
		case R.id.standard_mode:
			break;
		case R.id.hd_mode:
			
			break;
		case R.id.child_mode:
			break;
		case R.id.patriarch_mode:
			
			break;
		case R.id.wifi_open:
			break;
		case R.id.wifi_close:
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
