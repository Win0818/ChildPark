package com.worldchip.childpark.application;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.worldchip.childpark.database.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

public class AppInfoData {

	private static final String TAG = "AppInfoData";
	private static final boolean DEBUG = false;
	
	private static List<AppInfo> shareApps = null;

	/**
	 * ���
	 */
	public static void clearShareAppList() {
		if (shareApps != null) {
			shareApps.clear();
		}
	}
	
	/**
	 * ���APP
	 * 
	 * @param context
	 * @param values
	 */
	public static void addShareApp(Context context, ContentValues values) {
		DBHelper helper = new DBHelper(context);
		helper.insertValues(DBHelper.APP_TABLE, values);
		Log.e("addShareApp", "success");
	}
	
	/**
	 * ȡ������
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static boolean delShareAppData(Context context, String packageName) {
		DBHelper helper = new DBHelper(context);
		boolean falg = helper.deleteRow(DBHelper.APP_TABLE,
				"packageName='" + packageName + "'");
		Log.e("delShareAppData", "falg��" + falg);
		return falg;
	}

	public static List<AppInfo> getUserAppInfoList(Context context,
			List<String> pkgList) {

		List<AppInfo> userAppsList = new ArrayList<AppInfo>();

		List<AppInfo> systemList = getSystemAppInfoList(context);
		if (systemList == null || systemList.size() < 1) {
			return userAppsList;
		}

		for (AppInfo app : systemList) {
			if (pkgList.contains(app.getPackageName())) {
				userAppsList.add(app);
			}
		}

		return userAppsList;
	}
	
	/**
	 * ��ȡ��APP
	 */
	public static List<AppInfo> getLocalShareAppDatas(Context context) {
		if (shareApps != null && shareApps.size() > 0) {
			return shareApps;
		}
		shareApps = new ArrayList<AppInfo>();
		DBHelper helper = new DBHelper(context);
		Cursor cursor = helper.getCursor(DBHelper.APP_TABLE,
				"order by _id desc");
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String packageName = cursor.getString(cursor
					.getColumnIndex("packageName"));
			String icon = cursor.getString(cursor.getColumnIndex("icon"));
			String appName = cursor.getString(cursor.getColumnIndex("appName"));
			AppInfo appInfo = null;
			if (checkBrowser(packageName, context)) {
				appInfo = new AppInfo(id, packageName, icon, appName);
				shareApps.add(appInfo);
				Log.i("Wing", "getLocalShareAppDatas---->>>" + "packageName" + 
				packageName + "::::" + "appName:  " + appName);
			} else {
				delShareAppId(context, id);
			}
		}
		cursor.close();
		return shareApps;
	}


	/**
	 * @param context
	 * @return
	 */
	public static List<AppInfo> getSystemAppInfoList(Context context) {

		List<AppInfo> systemAppsList = new ArrayList<AppInfo>();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> mApps = context.getPackageManager()
				.queryIntentActivities(mainIntent, 0);

		AppInfo appInfo = null;
		for (ResolveInfo resolveInfo : mApps) {

			appInfo = new AppInfo();

			appInfo.setPackageName(resolveInfo.activityInfo.packageName);
			appInfo.setAppName(resolveInfo.activityInfo.loadLabel(
					context.getPackageManager()).toString());
			appInfo.setIcon(drawableToByte(resolveInfo.activityInfo.loadIcon(context
					.getPackageManager())));

			systemAppsList.add(appInfo);
		}

		return systemAppsList;
	}

	/**
	 * @param pInfo
	 * @return
	 */
	public static boolean isSystemApp(String packageName, Context context) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES
							| PackageManager.GET_ACTIVITIES);
			if ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<String> getMyAppPackages() {
		List<String> myAppList = new ArrayList<String>();

		myAppList.add("com.worldchip.childpark");
		return myAppList;
	}
	
	/**
	 * ��ȡϵͳ����APP
	 * 
	 * @param context
	 * @return
	 */
	public static List<AppInfo> getLocalAppDatas(Context context) {
		List<AppInfo> items = new ArrayList<AppInfo>();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> mApps = context.getPackageManager()
				.queryIntentActivities(mainIntent, 0);
		AppInfo appInfo = null;
		List<String> pkgs = getMyAppPackages();

		for (ResolveInfo app : mApps) {
			System.out.println("packageName" + app.activityInfo.packageName);
			appInfo = new AppInfo();
			appInfo.setPackageName(app.activityInfo.packageName);
			appInfo.setIcon(drawableToByte(app.activityInfo.loadIcon(context
					.getPackageManager())));
			appInfo.setAppName(app.activityInfo.loadLabel(
					context.getPackageManager()).toString());
			appInfo.isSelected = AppInfoData.getShareAppByData(context,
					app.activityInfo.packageName);
			if (pkgs.toString().contains(app.activityInfo.packageName)) {
				continue;
			}
			items.add(appInfo);
		}
		return items;
	}

	public static String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        // ---get the package info---  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;  
	       // versioncode = pi.versionCode;
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        Log.e("VersionInfo", "Exception", e);  
	    }  
	    return versionName;  
	}  
	
	public static List<AppInfo> getSystemAppDatas(Context context) {
		List<AppInfo> systemAppList = new ArrayList<AppInfo>();
		List<String> myAppList = getMyAppPackages();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> mApps = context.getPackageManager()
				.queryIntentActivities(mainIntent, 0);
		AppInfo appInfo = null;
		// Log.e(TAG, "getSystemAppDatas...mApps.count="+mApps.size());
		for (ResolveInfo app : mApps) {
			// Log.e(TAG, "packageName="+app.activityInfo.packageName);
			if (myAppList.contains(app.activityInfo.packageName)) {
				continue;
			}

			appInfo = new AppInfo();
			appInfo.setPackageName(app.activityInfo.packageName);
			appInfo.setAppName(app.activityInfo.loadLabel(
					context.getPackageManager()).toString());
			appInfo.setIcon(drawableToByte(app.activityInfo.loadIcon(context
					.getPackageManager())));

			systemAppList.add(appInfo);
		}
		return systemAppList;
	}
	
	/**
	 * ��Drawableת��ΪString
	 */
	public synchronized static String drawableToByte(Drawable drawable) {
		if (drawable != null) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			int size = bitmap.getWidth() * bitmap.getHeight() * 4;

			// ����һ���ֽ����������,���Ĵ�СΪsize
			ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
			// ����λͼ��ѹ����ʽ������Ϊ100%���������ֽ������������
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			// ���ֽ����������ת��Ϊ�ֽ�����byte[]
			byte[] imagedata = baos.toByteArray();

			String icon = Base64.encodeToString(imagedata, Base64.DEFAULT);
			return icon;
		}
		return null;
	}
	
	/**
	 * ��Stringת��ΪDrawable
	 */
	public synchronized static Drawable byteToDrawable(String icon) {
		byte[] img = Base64.decode(icon.getBytes(), Base64.DEFAULT);
		Bitmap bitmap;
		if (img != null) {
			bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bitmap);
			return drawable;
		}
		return null;
	}
	
	/**
	 * �ж�ָ��Ӧ���Ƿ����
	 */
	public static boolean checkBrowser(String packageName, Context context) {
		try {
			if (packageName == null || "".equals(packageName)) {
				return false;
			}
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			Log.e("ApplicationInfo", "info:" + info);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * ȡ������
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static void delShareAppId(Context context, long id) {
		DBHelper helper = new DBHelper(context);
		boolean falg = helper.deleteRow(DBHelper.APP_TABLE, id);
		Log.e("delShareAppId", "falg��" + falg);
	}
	
	/**
	 * �жϸ�APP�Ƿ��Ѿ�����
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static boolean getShareAppByData(Context context, String packageName) {
		DBHelper helper = new DBHelper(context);
		Cursor cursor = helper.getCursorBySql("select count(*) from '"
				+ DBHelper.APP_TABLE + "' where packageName = '"
				+ packageName + "' order by _id desc");
		if (cursor != null) {
			cursor.moveToFirst();
			int count = cursor.getInt(0);
			if (count > 0) {
				cursor.close();
				return true;
			}
		}
		return false;
	}
	
	
}