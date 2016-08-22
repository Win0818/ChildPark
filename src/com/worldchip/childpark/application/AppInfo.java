package com.worldchip.childpark.application;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class AppInfo {

	public String TAG = "";
	private String appName;
	private String packageName;
	private Drawable icon;
	private Bitmap userBackground;

	public AppInfo() {

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

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable draIcon) {
		this.icon = draIcon;
	}

	public Bitmap getBitmap() {
		return userBackground;

	}

	public void setBitmap(Bitmap bitmap) {
		userBackground = bitmap;
	}

}