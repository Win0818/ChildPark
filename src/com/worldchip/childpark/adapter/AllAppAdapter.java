package com.worldchip.childpark.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.worldchip.childpark.R;
import com.worldchip.childpark.application.AppInfo;
import com.worldchip.childpark.application.AppInfoData;
import com.worldchip.childpark.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * ����APP
 * 
 * @author Administrator
 * 
 */
public class AllAppAdapter extends BaseAdapter {

	private List<AppInfo> mDataList;
	private Activity mContext;
	private boolean mDeleteState = false;
	private String packageName = "";
	Animation mReverseAnim = null;
	private boolean isDbClick = false;
	private Handler mHandler;
	private List<AppInfo> shareList = new ArrayList<AppInfo>();

	/**
	 * ��ȡѡ�з������APP
	 * 
	 * @return
	 */
	public List<AppInfo> getShareAppData() {
		return shareList;
	}
	/**
	 * ��ȡɾ���APP����
	 */
	public String getPackageName() {
		return packageName;
	}
	public AllAppAdapter(Activity act, List<AppInfo> mApps, Handler mHandler) {
		this.mContext = act;
		this.mDataList = mApps;
		this.mHandler = mHandler;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (mDataList != null) {
			count = mDataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public boolean getAllAppearImage() {
		return this.mDeleteState;
	}

	class Holder {
		private ImageView mItemType;
		private TextView mItemName;
		private ImageView mSelected;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mContext, R.layout.all_app_item, null);
			holder.mItemType = (ImageView) convertView
					.findViewById(R.id.item_image);
			holder.mItemName = (TextView) convertView
					.findViewById(R.id.item_name);
			holder.mSelected = (ImageView) convertView.findViewById(R.id.appisselected);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final AppInfo info = mDataList.get(position);
		holder.mItemType.setImageDrawable(AppInfoData.byteToDrawable(info.getIcon()));
		holder.mItemName.setText(info.getAppName());
		holder.mItemName.setSelected(true);
		if (info.isSelected) {
			holder.mSelected.setImageResource(R.drawable.share_apk_right);
		} else {
			holder.mSelected.setImageResource(-1);
		}
		/*holder.mItemType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean flag = AppInfoData.isSystemApp(info.getPackageName(),
						mContext);
				
				if (flag) {
					Utils.showToastMessage(mContext,mContext.getResources().getString(R.string.system_application_cannot_deleted) );
				} else {
					final Dialog dialog = new Dialog(mContext);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					View view = View.inflate(mContext,
							R.layout.all_app_control_dialog, null);
					dialog.setContentView(view);
					Window dialogWindow = dialog.getWindow();
					
					TextView uninstallApp = (TextView) view
							.findViewById(R.id.uninstall_cancel);
					TextView addToChildApp = (TextView) view
							.findViewById(R.id.uninstall_confirm);
					dialog.show();
					
					uninstallApp.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							packageName = info.getPackageName();
							Uri packageURI = Uri.parse("package:"
									+ info.getPackageName());
							// ����Intent��ͼ
							Intent intent = new Intent(
									Intent.ACTION_DELETE, packageURI);
							// ִ��ж�س���
							mContext.startActivity(intent);
							boolean b = AppInfoData.getShareAppByData(mContext,
									info.getPackageName());
							if (b) {
								boolean c = AppInfoData.delShareAppData(mContext,
										info.getPackageName());
								if (c) {
									shareList.remove(info);
								}
							}
							dialog.hide();
							Message message = Message.obtain(mHandler, 3);
							message.sendToTarget();
							
						}
					});*/
					
					/*addToChildApp.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ContentValues values = new ContentValues();
							values.put("packageName", info.getPackageName());
							values.put("icon", info.getIcon());
							values.put("appName", info.getAppName());
							AppInfoData.addShareApp(mContext, values);
							shareList.add(info);
						}
					});*/
					
				//}
				
				
			//}
		//});
		return convertView;
	}
	
	/**
	 * ɾ���б���
	 */
	public void delItem(String packageName) {
		Iterator<AppInfo> ite = mDataList.iterator();
		while (ite.hasNext()) {
			AppInfo appInfo = ite.next();
			if (appInfo.getPackageName().equals(packageName)) {
				ite.remove();
				notifyDataSetChanged();
				break;
			}
		}
	}
}