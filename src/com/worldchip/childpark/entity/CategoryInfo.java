package com.worldchip.childpark.entity;

import android.graphics.drawable.Drawable;

public class CategoryInfo {

	private  Drawable mItemIcon;
	private  String mItemName;
	
	public CategoryInfo() {
	}

	public CategoryInfo(Drawable mItemIcon, String mItemName) {
		super();
		this.mItemIcon = mItemIcon;
		this.mItemName = mItemName;
	}

	public Drawable getmItemIcon() {
		return mItemIcon;
	}

	public void setmItemIcon(Drawable mItemIcon) {
		this.mItemIcon = mItemIcon;
	}

	public String getmItemName() {
		return mItemName;
	}

	public void setmItemName(String mItemName) {
		this.mItemName = mItemName;
	}
		
	
	
	
	
	

}
