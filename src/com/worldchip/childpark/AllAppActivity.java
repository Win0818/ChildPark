package com.worldchip.childpark;

import java.util.List;

import com.worldchip.childpark.adapter.AllAppAdapter;
import com.worldchip.childpark.application.AppInfo;
import com.worldchip.childpark.application.AppInfoData;
import com.worldchip.childpark.view.PasswordInputDialog;
import com.worldchip.childpark.view.PasswordInputDialog.PasswordValidateListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;


/**
 * ����Ӧ��
 * 
 * @author guofq
 */
public class AllAppActivity extends Activity {

	private static final String TAG = "AllAppActivity";
	private GridView mGridApps;

	private List<AppInfo> mAllApps;
	private AllAppAdapter mAllAppAdapter;

	private AdapterView<?> mParent;
	private int mPosition;
	SharedPreferences mPrefs;

	private Context mCtx;
	private PasswordInputDialog mPasswordInputDialog = null;
	

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mAllAppAdapter = new AllAppAdapter(AllAppActivity.this,
						mAllApps, mShareHandler);
				mGridApps.setAdapter(mAllAppAdapter);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_app_layout);

		mCtx = AllAppActivity.this;
		initView();
		showPasswordInputView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		mGridApps = (GridView) findViewById(R.id.grid_apps);
		mGridApps.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				mParent = parent;
				mPosition = position;
				AllAppActivity.this.startTargetActivity(mParent, mPosition);
			}
		});
	}


	private void startTargetActivity(AdapterView<?> parent, int position) {
		try {
			AppInfo appInfo = (AppInfo) parent.getAdapter().getItem(position);
			if (appInfo != null) {
				Intent intent = getPackageManager().getLaunchIntentForPackage(
						appInfo.getPackageName());
				if (intent != null) {
					startActivity(intent);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.e(TAG, "initData start...");
					mAllApps = AppInfoData.getSystemAppDatas(mCtx);
					mHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//		case KeyEvent.KEYCODE_MENU:
//		case KeyEvent.KEYCODE_HOME:
//			return true;
//		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private Handler mShareHandler = new Handler() {
		
	};
	
	private void showPasswordInputView() {
		if (mPasswordInputDialog != null) {
			if (mPasswordInputDialog.isShowing()) {
				mPasswordInputDialog.dismiss();
				mPasswordInputDialog = null;
			}
		}
		mPasswordInputDialog = PasswordInputDialog.createDialog(mCtx);
		mPasswordInputDialog.setListener(new PasswordValidateListener() {
			@Override
			public void onValidateComplete(boolean success) {
				if (!success) {
					AllAppActivity.this.finish();
				}
			}
		});
		mPasswordInputDialog.show();
	}
}