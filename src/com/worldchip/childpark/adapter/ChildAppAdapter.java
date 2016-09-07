package com.worldchip.childpark.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.worldchip.childpark.R;
import com.worldchip.childpark.application.AppInfo;
import com.worldchip.childpark.application.AppInfoData;

public class ChildAppAdapter extends BaseAdapter{

	private List<AppInfo> mDataList;
	private Activity mContext;
	private boolean mDeleteState = false;

	Animation mReverseAnim = null;

	public ChildAppAdapter(Activity act, List<AppInfo> mApps) {
		this.mContext = act;
		this.mDataList = mApps;
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
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final AppInfo info = mDataList.get(position);
		holder.mItemType.setImageDrawable(AppInfoData.byteToDrawable(info.getIcon()));
		holder.mItemName.setText(info.getAppName());
		holder.mItemName.setSelected(true);
		return convertView;
	}
}
