package com.worldchip.childpark.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.worldchip.childpark.R;
import com.worldchip.childpark.entity.VideoInfo;
import com.worldchip.childpark.util.image.MyThreadImageLoader;
import com.worldchip.childpark.util.image.MyThreadImageLoader.Type;

public class MyVideoAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<VideoInfo> mItems;
	private MyThreadImageLoader mImageLoader;
	private boolean mIsNet;
	private int mPosition;
	
	public MyVideoAdapter(Context context, List<VideoInfo> Items, boolean isNet) {
		this.mContext = context;
		this.mItems = Items;
		mImageLoader = MyThreadImageLoader.getInstance(3, Type.LIFO);
		mInflater = LayoutInflater.from(mContext);
		this.mIsNet = isNet;
	}

	public void setPosition(int position){
		mPosition = position;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold viewHold = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_video_listview, null);
			convertView.setBackgroundColor(Color.TRANSPARENT);
			viewHold = new ViewHold();
			viewHold.mItemImg = (ImageView) convertView
					.findViewById(R.id.iv_gridview_video_image);
			viewHold.mItemText = (TextView) convertView
					.findViewById(R.id.tv_griditem_video_name);
			viewHold.mMain = (RelativeLayout) convertView
					.findViewById(R.id.main);
			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}
		mImageLoader.loadImage(mItems.get(position).getmPicAddress(),viewHold.mItemImg, mIsNet);
		viewHold.mItemText.setText(mItems.get(position).getmVideoName());
		return convertView;
	}

	class ViewHold {
		private ImageView mItemImg;
		private TextView mItemText;
		private RelativeLayout mMain;
	}


}
