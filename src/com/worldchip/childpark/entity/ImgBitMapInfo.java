package com.worldchip.childpark.entity;

import android.graphics.Bitmap;

public class ImgBitMapInfo {

	private  String imagePath;//bitmap图片
	
	private  String mItemName;//文件夹的名字
	
	private  String mFolderPath; //文件夹的绝对路径
	
	public ImgBitMapInfo() {
		
	}

	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String mItemBitmap) {
		this.imagePath = mItemBitmap;
	}



	public String getmItemName() {
		return mItemName;
	}

	public void setmItemName(String mItemName) {
		this.mItemName = mItemName;
	}



	public String getmFolderPath() {
		return mFolderPath;
	}



	public void setmFolderPath(String mFolderPath) {
		this.mFolderPath = mFolderPath;
	}
		
	
	
	
	
	

}
