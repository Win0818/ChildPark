package com.worldchip.childpark.application;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class AppInfo {

	public String TAG = "";
	private int id;
	private String appName;
	private String packageName;
	private String icon;
	private Bitmap userBackground;
	public boolean isSelected = false;

	public AppInfo() {

	}
	public AppInfo(int id, String packageName,String icon, String appName) 
	{
		super();
		this.id = id;
		this.packageName = packageName;
		this.icon = icon;
		this.appName = appName;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String draIcon) {
		this.icon = draIcon;
	}

	public Bitmap getBitmap() {
		return userBackground;

	}

	public void setBitmap(Bitmap bitmap) {
		userBackground = bitmap;
	}

}