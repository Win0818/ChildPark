package com.worldchip.childpark;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.application.MyApplication;
import com.worldchip.childpark.util.Utils;

public class ForgetPasswordActivity extends BaseActivity implements OnClickListener{
	
	private CheckBox mCheckBox;
	private EditText mEditText;
	private Context ctx;
	private Typeface mTypeface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_forget_password);
		ctx = this;
		initView();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		initBaseActivity();
		
	}

	private void initView() {
		MySharePreData.GetIntData(MyApplication.getApplicationContex(), "my_system_setting", "display_setting");
		mCheckBox = (CheckBox) findViewById(R.id.recover_init_password);
		mEditText = (EditText) findViewById(R.id.leave_factory_check_code);
		findViewById(R.id.forget_password__confirm).setOnClickListener(this);
		findViewById(R.id.forget_password_back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.forget_password_title);
		mTypeface = Typeface.createFromAsset(MyApplication.getApplicationContex().getAssets(), "Droidhuakangbaoti.TTF");
		title.setTypeface(mTypeface);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.forget_password__confirm:
			String checkCode = mEditText.getText().toString().trim();
			if (mCheckBox != null && mCheckBox.isChecked()) {
				if (!TextUtils.isEmpty(checkCode) && checkCode.equals("1246688688")) {
					MySharePreData.SetData(ctx, "password_manager", "password", "000000");
					showToast(R.string.forget_password_recover_init_success);
					finish();
				} else {
					showToast(R.string.forget_password_check_code_error);
				}
				
			} else {
				showToast(R.string.forget_password_select_checkbox);
			}
			
			break;
		case R.id.forget_password_back:
			finish();
			break;
		}
	}
	
	private void showToast(int id) {
		Utils.showToastMessage(this, getResources().getString(id));
	}
}
