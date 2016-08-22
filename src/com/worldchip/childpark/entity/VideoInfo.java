package com.worldchip.childpark.entity;

public class VideoInfo {

	private String mPicAddress;

	private String mVideoName;

	private String mVideoAddress;

	private int cateId;
	
	private long fileSize;
	
	private String folderName;

	private String createTime;
	
	
	public VideoInfo() {

	}

	public int getCateId() {
		return cateId;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}



	public String getmPicAddress() {
		return mPicAddress;
	}

	public void setmPicAddress(String mPicAddress) {
		this.mPicAddress = mPicAddress;
	}

	public String getmVideoName() {
		return mVideoName;
	}

	public void setmVideoName(String mVideoName) {
		this.mVideoName = mVideoName;
	}

	public String getmVideoAddress() {
		return mVideoAddress;
	}

	public void setmVideoAddress(String mVideoAddress) {
		this.mVideoAddress = mVideoAddress;
	}
	
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
