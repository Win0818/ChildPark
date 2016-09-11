package com.worldchip.childpark;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.util.Utils;

public class ForgetPasswordActivity extends BaseActivity implements OnClickListener{
	
	private CheckBox mCheckBox;
	private EditText mEditText;
	private Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.forget_password_activity);
		ctx = this;
		initView();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		initBaseActivity();
	}

	private void initView() {
		mCheckBox = (CheckBox) findViewById(R.id.recover_init_password);
		mEditText = (EditText) findViewById(R.id.leave_factory_check_code);
		findViewById(R.id.forget_password__confirm).setOnClickListener(this);
		findViewById(R.id.forget_password_back).setOnClickListener(this);
		
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
