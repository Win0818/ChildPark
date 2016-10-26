package com.worldchip.childpark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.adapter.MyVideoAdapter;
import com.worldchip.childpark.entity.VideoInfo;
import com.worldchip.childpark.util.MySdCardDataUtil;

public class LocalVideoShowActivity  extends BaseActivity implements OnItemClickListener, OnItemSelectedListener{
	private ImageView mCategroyImgIv;
	private TextView  mVideoCountTv;
	private TextView  mVideoPropertyTv;
	private TextView  mVideoTypeTv;
	private TextView  mViedeoSizeTV;
	private TextView  mNoteList;
	private ListView  mVideoListView;
	private List<VideoInfo>  mInfos;
	private int  mCategoryInt = 0;
	private int mPosition = 0;
	private MyVideoAdapter mVideoAdapter;
	private ArrayList<String>  mVideoAddressList;
	private ArrayList<String>  mVideoNameList;
	
	
	private String exSdcardPath ="";
	StringBuilder log = new StringBuilder();
	MyVideoDataAsynTask mAsynTask =null;
	
	private int[] mImgIcons = {
			                   R.drawable.video_category_0,
			                   R.drawable.video_category_1,
							   R.drawable.video_category_2,
							   R.drawable.video_category_3,
							   R.drawable.video_category_4,
							   R.drawable.video_category_5,
							   };
	private List<String> pathString = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_listshow);
        initView();
        initData();

        Log.e(TAG, "onCreate..mCategoryInt="+mCategoryInt);
        
        mAsynTask = new MyVideoDataAsynTask();
        mAsynTask.execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        initBaseActivity();
	}
	
    private void initView(){
    	mCategroyImgIv = (ImageView) findViewById(R.id.iv_video_category_icon);
    	mVideoCountTv = (TextView) findViewById(R.id.tv_video_count_show);	
    	mVideoPropertyTv = (TextView) findViewById(R.id.tv_video_property);
    	mVideoTypeTv = (TextView) findViewById(R.id.tv_video_type);
    	mViedeoSizeTV = (TextView) findViewById(R.id.tv_video_size);
    	mVideoListView = (ListView) findViewById(R.id.lv_video_show_list);
    	mNoteList = (TextView) findViewById(R.id.note_list);
    	
    	mVideoListView.setOnItemClickListener(this);
    	mVideoListView.setOnItemSelectedListener(this);
    	
    	pathString.add(getResources().getString(R.string.category_item1));
    	pathString.add(getResources().getString(R.string.category_item2));
    	pathString.add(getResources().getString(R.string.category_item3));
    	pathString.add(getResources().getString(R.string.category_item4));
    	pathString.add(getResources().getString(R.string.category_item5));
    	pathString.add(getResources().getString(R.string.category_item6));
    }
    
    private void initData(){
    	mCategoryInt = getIntent().getExtras().getInt("category", 0);
    	mVideoAddressList = new ArrayList<String>();
	    mVideoNameList   = new ArrayList<String>();
    	setCategoryImg(mCategoryInt);
    	getexSdcardPath();
    }
    
    
    private void getexSdcardPath(){
    	 if (mCategoryInt==-1){ //usb
    		 exSdcardPath = Comments.EXTUSBPATH;
    		 if(exSdcardPath == null || exSdcardPath.equals("")){
    			 Toast.makeText(this, getString(R.string.extusb_no), Toast.LENGTH_LONG).show();
    			 finish();
    			 return;
    		 }
    		 //Toast.makeText(this, getString(R.string.extsdcard_path)+exSdcardPath, Toast.LENGTH_LONG).show();
    		 Log.e(TAG, getString(R.string.extusb_path)+exSdcardPath);
    	 }else{
	    	 if(Comments.EXTSDPATH == null || Comments.EXTSDPATH=="" ){
		    	 List<String> extPaths = MySdCardDataUtil.getExtSDCardPath();
		    	 if(extPaths.size()>0){
		    		 exSdcardPath = extPaths.get(0);
		    	 }
		         Log.e(TAG, "getexSdcardPath: "  +exSdcardPath);
	    	 }else{
	    		 exSdcardPath = Comments.EXTSDPATH;
	    	 }
	    	 Log.e(TAG, getString(R.string.extsdcard_path)+exSdcardPath);
	    	// Toast.makeText(this, getString(R.string.extsdcard_path)+exSdcardPath, Toast.LENGTH_LONG).show();
    	 }
         
    }
    
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
    	mVideoAdapter.setPosition(position);
    	mPosition = position;
    	if(mInfos!=null){
    	    setVideoCount(this.mInfos, (mPosition+1));
    	    setVideoSize(mInfos, mPosition);
    	}
		//selectOneVideo();  
    	 selectOneVideo_2(position);
	} 
   
    
    private void setCategoryImg(int category){
    	if (category == -1){
    		mCategroyImgIv.setImageResource(R.drawable.video_category_6);
    		return;
    	}
    	
	    if(category <= 6 && category>=0){
	    	mCategroyImgIv.setImageResource(mImgIcons[category]);
	    }
	}
    
    private void setListVideodata(List<VideoInfo> infos, boolean isNet){
         	mVideoAdapter = new MyVideoAdapter(LocalVideoShowActivity.this,infos,isNet);
         	if(mVideoListView != null){
         	   mVideoListView.setAdapter(mVideoAdapter);
         	}
        }
    
    private void setVideoCount(List<VideoInfo> infos, int selectInt){
    	if(infos != null ){
    		if(mVideoCountTv != null)
    		  mVideoCountTv.setText(selectInt+"/"+infos.size());
    	}else{
    		if(mVideoCountTv != null)
    			 mVideoCountTv.setText("0/0");
    	}
    }
    
    private void setVideoProperty(List<VideoInfo> infos){
    	if(infos != null){
    		mVideoPropertyTv.setText(getString(R.string.video_property)+"Video");
    	}
    }
    
    private void setVideoType(List<VideoInfo> infos,int selectInt){
    	if(infos != null && infos.size()>0 && infos.size()>= selectInt){
    		String fileName = infos.get(selectInt).getmVideoName();
    		String type = MySdCardDataUtil.getExtensionName(fileName);
    		mVideoTypeTv.setText(getString(R.string.video_type)+type);
    	}
    }
    
    private void setVideoSize(List<VideoInfo> infos,int selectInt){
    	if(infos != null && infos.size()>0 && infos.size()>= selectInt){
    		String size = MySdCardDataUtil.FormetFileSize(infos.get(selectInt).getFileSize());
    		mViedeoSizeTV.setText(getString(R.string.video_size)+size);
    	}
    }
    
    private void selectOneVideo_2(int index) {
    	Comments.MOV_NAME = mInfos.get(mPosition).getmVideoName();
		Comments.HAVE_WATCH_VIDEO = true;
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        File file = new File(mVideoAddressList.get(index) /*+ "/" + mVideoNameList.get(index)*/);
        Log.d("Wing", "file::: " + file);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/*");
        
        Bundle  bundle = new Bundle();
		  bundle.putInt("position", mPosition);
		  bundle.putStringArrayList("video_address", mVideoAddressList);
		  bundle.putStringArrayList("video_name", mVideoNameList);
		  intent.putExtras(bundle);
        startActivity(intent);
       // return intent;
    }
    
    
    private void selectOneVideo() {
		  Comments.MOV_NAME = mInfos.get(mPosition).getmVideoName();
		  Comments.HAVE_WATCH_VIDEO = true;
		  Intent intentPlayVideo = new Intent(LocalVideoShowActivity.this, VideoPlayerActivity.class);
		  Bundle  bundle = new Bundle();
		  bundle.putInt("position", mPosition);
		  bundle.putStringArrayList("video_address", mVideoAddressList);
		  bundle.putStringArrayList("video_name", mVideoNameList);
		  intentPlayVideo.putExtras(bundle);
		  startActivity(intentPlayVideo);
	}
   
    
    class MyVideoDataAsynTask  extends  AsyncTask<Void, Integer, String>{
		@Override
		protected String doInBackground(Void... params) {

			String path = ""; 
			if(mCategoryInt == -1){
				path = Comments.EXTUSBPATH; // exSdcardPath+File.separator;
			}else{
				path = exSdcardPath+File.separator+pathString.get(mCategoryInt)+File.separator;
			}
			System.out.println("----path----"+path);
			
			File file = new File(path);
			if(mInfos!=null){
				mInfos.clear();
			}else{
				mInfos = new ArrayList<VideoInfo>();
			}
		    MySdCardDataUtil.getVideoListInfo(mInfos, file);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(mInfos.size() != 0){
				setListVideodata(mInfos,false);
				setVideoCount(mInfos,1);
				setVideoProperty(mInfos);
				setVideoSize(mInfos, 0);
				setVideoType(mInfos, 0);
				mVideoListView.setVisibility(View.VISIBLE);
				mNoteList.setVisibility(View.GONE);
				for (int i = 0; i < mInfos.size() ; i++) {
	                 mVideoAddressList.add(mInfos.get(i).getmVideoAddress());	
	                 mVideoNameList.add(mInfos.get(i).getmVideoName());
				}
			}else{
				mVideoListView.setVisibility(View.GONE);
				mNoteList.setVisibility(View.VISIBLE);
			}
		}
    }


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		mPosition = position;
    	if(mInfos!=null){
    	    setVideoCount(this.mInfos, (mPosition+1));
    	    setVideoSize(mInfos, mPosition);
    	    setVideoType(mInfos,mPosition);
    	}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
    
   /* @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.e(TAG, "dispatchKeyEvent...keyCode=" + event.getKeyCode());
		if (event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_UP:
				if(mVideoAddressList != null && mVideoAddressList.size()>0){
					if(mPosition == 0){
				       mPosition = mVideoAddressList.size()-1;
					}else{
						mPosition--;
					}
				   setVideoCount(mInfos,mPosition+1);
				   setVideoSize(mInfos, mPosition);
				   setVideoType(mInfos, mPosition);
				}
				return true;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if(mVideoAddressList != null && mVideoAddressList.size()>0){
					if(mPosition == mVideoAddressList.size()-1){
				       mPosition = 0;
					}else{
						mPosition++;
					}
				   setVideoCount(mInfos,mPosition+1);
				   setVideoSize(mInfos, mPosition);
				   setVideoType(mInfos, mPosition);
				}
				return true;
			case 66:
				selectOneVideo(); 
				return  false;
			}
		}
		return super.dispatchKeyEvent(event);
	};*/
   
 }
