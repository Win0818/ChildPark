package com.worldchip.childpark.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.worldchip.childpark.R;
import com.worldchip.childpark.Comments.MySharePreData;


public class PasswordInputDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context mContext = null;
	private static PasswordInputDialog passwordInputDialog = null;
	private static final String TAG = "--PasswordInputDialog--";
	private PasswordValidateListener mListener = null;
	private EditText mPwdEditText;
	private PasswordInputDialog mPasswordInputDialog = null;
	public PasswordInputDialog(Context context) {
		super(context);
		mContext = context;
	}

	public PasswordInputDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public static PasswordInputDialog createDialog(Context context) {
		passwordInputDialog = new PasswordInputDialog(context,
				R.style.password_dialog_style);
		passwordInputDialog.setContentView(R.layout.enter_password_layout);
		passwordInputDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		passwordInputDialog.setCancelable(false);

		return passwordInputDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		initView();
	}

	private void initView() {
		if (passwordInputDialog == null) {
			return;
		}
		

		passwordInputDialog.findViewById(R.id.password_cancel_btn)
				.setOnClickListener(this);
		passwordInputDialog.findViewById(R.id.password_enter_btn)
		.setOnClickListener(this);
		passwordInputDialog.findViewById(R.id.modify_password_tv)
				.setOnClickListener(this);
		mPwdEditText = (EditText)passwordInputDialog.findViewById(R.id.password_edittext);

		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
	
		case R.id.password_cancel_btn:
			dismissDialog(false);
			break;
		case R.id.password_enter_btn:
			validatePassword();
			break;
		case R.id.modify_password_tv:
			try {
				Intent modifyPswIntent = new Intent();
				modifyPswIntent.setComponent(new ComponentName(
						"com.worldchip.bbp.ect",
						"com.worldchip.bbp.ect.activity.PassLockActivity"));
				modifyPswIntent.putExtra("isChangePsw", true);
				mContext.startActivity(modifyPswIntent);
				dismissDialog(false);
			} catch (Exception e) {
				//Toast.makeText(mContext, R.string.start_app_error,
				//		Toast.LENGTH_SHORT).show();
				Log.e(TAG, e.getStackTrace().toString());
			}
			break;
		}
	}

	@SuppressLint("NewApi")
	private void validatePassword() {
			String pwd = MySharePreData.GetData(getContext(), "password_manager", "password");
			Log.e(TAG, "validatePassword..pwd=" + pwd);

			if (pwd == null || pwd.equals("")) {
				pwd = "000000";
			}
			if (mPwdEditText != null && mPwdEditText.getText().toString().trim().equals(pwd)) {
				dismissDialog(true);
			} else {
				Toast.makeText(mContext, R.string.input_psw_error,
						Toast.LENGTH_SHORT).show();
				mPwdEditText.setText("");
			}
	}

	private void dismissDialog(boolean success) {
		if (passwordInputDialog != null) {
			passwordInputDialog.dismiss();
			passwordInputDialog = null;
		}
		if (mListener != null) {
			mListener.onValidateComplete(true);
		}
	}
	public void setListener(PasswordValidateListener listener) {
		mListener = listener;
	}
	
	public interface PasswordValidateListener {
		public void onValidateComplete(boolean success);
	}
	
}