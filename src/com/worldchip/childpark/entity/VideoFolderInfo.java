package com.worldchip.childpark.entity;

public class VideoFolderInfo {

	private int cateId;

	private String folderName;

	private String createTime;
	
	private int topId;
	
	public VideoFolderInfo() {
		
	}
	
	
	public VideoFolderInfo(int cateId, String folderName, String createTime,
			int topId) {
		super();
		this.cateId = cateId;
		this.folderName = folderName;
		this.createTime = createTime;
		this.topId = topId;
	}
	
	public int getTopId() {
		return topId;
	}

	public void setTopId(int topId) {
		this.topId = topId;
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

}
