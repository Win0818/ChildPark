package com.worldchip.childpark.entity;

public class MusicInfo {
	
	private String  mMusicName;
	private String  mMusicAddress;
	private Long id;
	
	

	public MusicInfo() {
		super();
	}

	public MusicInfo(String mMusicName) {
		super();
		this.mMusicName = mMusicName;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getmMusicName() {
		return mMusicName;
	}

	public void setmMusicName(String mMusicName) {
		this.mMusicName = mMusicName;
	}

	public String getmMusicAddress() {
		return mMusicAddress;
	}

	public void setmMusicAddress(String mMusicAddress) {
		this.mMusicAddress = mMusicAddress;
	}

   
	
	
	
	  
	
	

}
