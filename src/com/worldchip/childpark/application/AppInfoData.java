package com.worldchip.childpark.application;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class AppInfoData {

	private static final String TAG = "AppInfoData";
	private static final boolean DEBUG = false;

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
			appInfo.setIcon(resolveInfo.activityInfo.loadIcon(context
					.getPackageManager()));

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
			appInfo.setIcon(app.activityInfo.loadIcon(context
					.getPackageManager()));

			systemAppList.add(appInfo);
		}
		return systemAppList;
	}
}