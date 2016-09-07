package com.worldchip.childpark;

import java.util.List;

import com.worldchip.childpark.adapter.AllAppAdapter;
import com.worldchip.childpark.adapter.ChildAppAdapter;
import com.worldchip.childpark.application.AppInfo;
import com.worldchip.childpark.application.AppInfoData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class ChildAppActivity extends Activity{

	private static final String TAG = "ChildAppActivity";
	
	private GridView mGridApps;

	private List<AppInfo> mAllApps;
	private ChildAppAdapter mAllAppAdapter;

	private AdapterView<?> mParent;
	private int mPosition;
	SharedPreferences mPrefs;

	private Context mCtx;
	

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mAllAppAdapter = new ChildAppAdapter(ChildAppActivity.this,
						mAllApps);
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

		mCtx = ChildAppActivity.this;
		initView();
		
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
				ChildAppActivity.this.startTargetActivity(mParent, mPosition);
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
					mAllApps = AppInfoData.getLocalShareAppDatas(mCtx);
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
	
}
