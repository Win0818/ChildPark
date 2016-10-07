package com.worldchip.childpark;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.application.MyApplication;
import com.worldchip.childpark.util.Utils;

public class PasswordManagerActivity extends BaseActivity implements OnClickListener{
	
	private EditText mOriginPassword;
	private EditText mChangPassword;
	private EditText mConfirmPassword;
	private Button mConfirm;
	private Button mBack;
	private Typeface mTypeface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_manager_2);
		initView();
		
	}
	private void  initView() {
		mOriginPassword = (EditText) findViewById(R.id.original_manager_password);
		mChangPassword = (EditText) findViewById(R.id.change_manager_password);
		mConfirmPassword = (EditText) findViewById(R.id.update_manager_password);
		mConfirm = (Button) findViewById(R.id.password_manager__confirm);
		mBack = (Button) findViewById(R.id.password_manager_back);
		TextView title = (TextView) findViewById(R.id.password_manager_title);
		mTypeface = Typeface.createFromAsset(MyApplication.getApplicationContex().getAssets(), "Droidhuakangbaoti.TTF");
		title.setTypeface(mTypeface);
		mConfirm.setOnClickListener(this);
		mBack.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initBaseActivity();
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.change_manager_password:
			mChangPassword.requestFocus();
			break;
		case R.id.update_manager_password:
			mConfirmPassword.requestFocus();
			break;
		case R.id.password_manager__confirm:
			Log.i(TAG, "validatePassword..pwd=");
			setPassword();
			break;
		case R.id.password_manager_back:
			finish();
			break;
		}
	}
	
	
	public static void start(Context context){
		Intent intent = new Intent(context, PasswordManagerActivity.class);
		context.startActivity(intent);
	}
	
	private void setPassword() {
		String originPassword = mOriginPassword.getText().toString().trim();
		String changPassword = mChangPassword.getText().toString().trim();
		String confirmPassword = mConfirmPassword.getText().toString().trim();
		
		String originPwd = MySharePreData.GetData(this, "password_manager", "password");
		Log.i(TAG, "validatePassword..pwd=" + originPwd);

		if (originPwd == null || originPwd.equals("")) {
			originPwd = "000000";
		}
		if (!TextUtils.isEmpty(originPassword)) {
			if (!TextUtils.isEmpty(changPassword)) {
				if (!TextUtils.isEmpty(confirmPassword)) {
					if (originPassword.equalsIgnoreCase(originPwd)) {
						if (changPassword.length() >= 6) {
							if (changPassword.equalsIgnoreCase(confirmPassword)) {
								MySharePreData.SetData(this, "password_manager", "password", changPassword);
								showToast(R.string.chang_password_success);
							} else {
								showToast(R.string.new_password_and_confirm_password_error);
							}
						} else {
							showToast(R.string.new_password_less_six);
						}
					}else {
						showToast(R.string.original_password_error);
					}
				}else {
					showToast(R.string.confirm_password_not_empty);
				}
			}else {
				showToast(R.string.new_password_not_empty);
			}
		} else {
			showToast(R.string.original_password_not_empty);
		}
	}
	
	private void showToast(int id) {
		Utils.showToastMessage(this, getResources().getString(id));
	}
	
	

}
