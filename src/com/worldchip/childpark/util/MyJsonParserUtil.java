package com.worldchip.childpark.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.util.Log;

import com.worldchip.childpark.entity.MusicInfo;
import com.worldchip.childpark.entity.VideoFolderInfo;
import com.worldchip.childpark.entity.VideoInfo;

public class MyJsonParserUtil {
	
	
	private static final String TAG = "CHRIS";


	public static List<MusicInfo>  parserMyMusicjson(String jsonStr){
		List<MusicInfo>  musicitems = new ArrayList<MusicInfo>();
		try {
			JSONArray  jsonArray = new JSONArray(jsonStr);
			System.out.println(jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				MusicInfo item = new MusicInfo();
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				item.setmMusicAddress(jsonObject.get("attr_music").toString());
				item.setmMusicName(jsonObject.getString("m_name"));
				musicitems.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  musicitems;
		
	}
	
     
    /**
     *获取视频文件的Json解析	
     * @param jsonStr
     * @return
     */
	@SuppressWarnings("deprecation")
	public static List<VideoInfo>  parserMyVideojson(String jsonStr){
		List<VideoInfo>  videoItems = new ArrayList<VideoInfo>();
		try {
			JSONArray  jsonArray = new JSONArray(jsonStr);
			System.out.println(jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				VideoInfo item = new VideoInfo();
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				item.setmPicAddress(jsonObject.getString("attr_mov_pic"));
				item.setmVideoName(jsonObject.getString("p_name"));
                item.setmVideoAddress(jsonObject.getString("attr_mov"));
				videoItems.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return  videoItems;
	}
	
	
	/**
	 * 获取Token值的Json解析
	 * @param jsonStr
	 * @return
	 */
	public static List<String> parserMyToken(String jsonStr){
   		    List<String>  mLoginDatas = new ArrayList<String>();
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
			    String token = jsonObject.getString("token");
			    mLoginDatas.add(token);
			    String formName = jsonObject.getString("from_name");
			    mLoginDatas.add(formName);
			    String formPass = jsonObject.getString("from_pass");
			    mLoginDatas.add(formPass);
				return  mLoginDatas;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return  null;
	}
	
	/**
	 * 获取Token值的Json解析
	 * @param jsonStr
	 * @return
	 */
	public static List<String> parserMyCloseTime(String jsonStr){
   		    List<String>  closeTimeDatas = new ArrayList<String>();
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
			    String type = jsonObject.getString("type");
			    closeTimeDatas.add(type);
			    String closeTime = jsonObject.getString("off_time");
			    closeTimeDatas.add(closeTime);
			    String mobile_status = jsonObject.getString("mobile_set_time");
			    closeTimeDatas.add(mobile_status);
				return  closeTimeDatas;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return  null;
	}
	
	
    /**
     *获取视频文件的Json解析	
     * @param jsonStr
     * @return
     */
	public static List<VideoFolderInfo>  parserMyVideoFolderjson(String jsonStr){
		List<VideoFolderInfo>  videoFolders = new ArrayList<VideoFolderInfo>();
		try {
			JSONArray  jsonArray = new JSONArray(jsonStr);
			for (int i = 0; i < jsonArray.length(); i++) {
				VideoFolderInfo item = new VideoFolderInfo();
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				item.setCateId(jsonObject.getInt("id"));
			    item.setFolderName(jsonObject.getString("cate_name"));
			    item.setCreateTime(jsonObject.getString("add_time"));
			    item.setTopId(jsonObject.getInt("top_id"));
			    videoFolders.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  videoFolders;
	}
	
	/**
	 * 解析出广告视频的下载地址和名字
	 * @param jsonStr
	 * @return
	 */
	public static  VideoInfo  parserMyAdVideojson(String jsonStr){
		      VideoInfo   adVideoInfo =  new   VideoInfo();
			  JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonStr);
					adVideoInfo.setmVideoName(jsonObject.getString("p_name"));
					adVideoInfo.setmVideoAddress(jsonObject.getString("p_attr"));
					adVideoInfo.setCateId(jsonObject.getInt("types"));
					return  adVideoInfo;
				} catch (JSONException e) {
					e.printStackTrace();
				}
		return null;
	}
	
	
	/**
	 * 解析出广告视频的下载地址和名字
	 * @param jsonStr
	 * @return
	 */
	public  static  String  parserMyAdImagejson(String jsonStr){
		       String  adImageAttrs = null;
				try {
					JSONObject jsonObject = new JSONObject(jsonStr);
					adImageAttrs = jsonObject.getString("p_attr");
					return  adImageAttrs;
				} catch (JSONException e) {
					e.printStackTrace();
				}
		return null;
	}
	
	

}
