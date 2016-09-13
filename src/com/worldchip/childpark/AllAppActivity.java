package com.worldchip.childpark;

import java.util.List;

import com.worldchip.childpark.adapter.AllAppAdapter;
import com.worldchip.childpark.application.AppInfo;
import com.worldchip.childpark.application.AppInfoData;
import com.worldchip.childpark.util.Utils;
import com.worldchip.childpark.view.PasswordInputDialog;
import com.worldchip.childpark.view.PasswordInputDialog.PasswordValidateListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * ����Ӧ��
 * 
 * @author guofq
 * 
 * apk的包名是：com.android.LauncherW2_haiway
 */
public class AllAppActivity extends Activity {

	private static final String TAG = "AllAppActivity";
	private GridView mGridApps;

	private List<AppInfo> mAllApps;
	private AllAppAdapter mAllAppAdapter;
	private boolean isDbClick = false;
	private AdapterView<?> mParent;
	private int mPosition;
	SharedPreferences mPrefs;
	private UninstallReceiver mUninstallReceiver;
	private Context mCtx;
	private PasswordInputDialog mPasswordInputDialog = null;
	private String  uninstallPackage;

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
		mUninstallReceiver = new UninstallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		this.registerReceiver(mUninstallReceiver, filter);
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
				//View view = View.inflate(AllAppActivity.this, R.layout.all_app_item, null);
				ImageView mSelected = (ImageView) view.findViewById(R.id.appisselected);
				//AppInfo appInfo = (AppInfo) parent.getAdapter().getItem(position);
				AppInfo appInfo = mAllApps.get(position);
				if (!isDbClick) {
					
					if (appInfo.isSelected) {
						boolean a = AppInfoData.delShareAppData(mCtx,
								appInfo.getPackageName());
						if (a) {
							appInfo.isSelected = false;
							mSelected.setImageResource(-1);
							Message message = Message.obtain(mHandler, 3);
							message.sendToTarget();
							//shareList.remove(info);
						}
					} else {
						appInfo.isSelected = true;
						ContentValues values = new ContentValues();
						values.put("packageName", appInfo.getPackageName());
						values.put("icon", appInfo.getIcon());
						values.put("appName", appInfo.getAppName());
						AppInfoData.addShareApp(mCtx, values);
						mSelected.setImageResource(R.drawable.share_apk_right);
					}
					
				} else {
					isDbClick = false;
				}
				mParent = parent;
				mPosition = position;
			}
		});
		
		mGridApps.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final AppInfo appInfo = mAllApps.get(position);
				if (!isDbClick) {
					isDbClick = true;
					final Dialog dialog = new Dialog(mCtx);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					View v = View.inflate(mCtx,
							R.layout.patriarch_control_uninstall, null);
					dialog.setContentView(v);
					Window dialogWindow = dialog.getWindow();
					dialogWindow
					.setBackgroundDrawableResource(R.drawable.parent_model_bg);
					WindowManager.LayoutParams lp = dialogWindow
					.getAttributes();
					lp.width = 407;
					lp.height = 225;
					dialogWindow.setAttributes(lp);
					
					TextView cancel = (TextView) v
							.findViewById(R.id.uninstall_cancel);
					TextView confirm = (TextView) v
							.findViewById(R.id.uninstall_confirm);
					dialog.show();
					
					cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.hide();
							
						}
					});
					confirm.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							boolean flag = AppInfoData.isSystemApp(appInfo.getPackageName(),
									mCtx);
							if (flag) {
								Utils.showToastMessage(mCtx,mCtx.getResources().
										getString(R.string.system_application_cannot_deleted) );
							} else {
								Uri packageURI = Uri.parse("package:"
										+ appInfo.getPackageName());
								Intent intent = new Intent(
										Intent.ACTION_DELETE, packageURI);
								startActivity(intent);
								
								boolean b = AppInfoData.getShareAppByData(mCtx,
										appInfo.getPackageName());
								if (b) {
									boolean c = AppInfoData.delShareAppData(mCtx,
											appInfo.getPackageName());
								}
								dialog.hide();
							}
							
						}
					});
					return true;
				}
				return false;
				
			}
			
		});
	}
	
	private class UninstallReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (uninstallPackage != null
					&& ("package:" + uninstallPackage).equals(intent
							.getDataString())) {
				Log.i(TAG, "UninstallReceiver--------appAdapter----->>>>>>>>");
				mAllAppAdapter.delItem(uninstallPackage);
				mAllAppAdapter.notifyDataSetChanged();
			}
		}
	}

	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.e(TAG, "initData start...");
					mAllApps = AppInfoData.getLocalAppDatas(mCtx);
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
	
	public static void start(Context context) {
		Intent intent = new Intent(context, AllAppActivity.class);
		context.startActivity(intent);
	}
}