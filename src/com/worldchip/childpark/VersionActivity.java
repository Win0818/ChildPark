package com.worldchip.childpark;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.worldchip.childpark.application.AppInfoData;

public class VersionActivity extends BaseActivity{

	private TextView mNowVersionName;
	private Button mBackBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		initView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initBaseActivity();
	}
	
	private void initView() {
		mNowVersionName = (TextView) findViewById(R.id.now_version_name);
		mBackBtn = (Button) findViewById(R.id.version_back_btn);
		mNowVersionName.setText("V" + AppInfoData.getAppVersionName(VersionActivity.this));
		mNowVersionName.setFocusable(true);
		mBackBtn.setFocusable(true);
		//mBackBtn.requestFocus();
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	private void initData() {
	}
	
	
}
