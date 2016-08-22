package com.worldchip.childpark.util.setting;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

public class MyScreenLightUtil{
	
	
	/**
     * 判断是否开启了自动亮度调节
     * 
     * @param activity
     * @return
     */ 
    public static boolean isAutoBrightness(Activity activity) { 
        boolean isAutoAdjustBright = false; 
        try { 
            isAutoAdjustBright = Settings.System.getInt( 
                    activity.getContentResolver(), 
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC; 
        } catch (SettingNotFoundException e) { 
            e.printStackTrace(); 
        } 
        return isAutoAdjustBright; 
    } 
   
    /**
     * 获取屏幕的亮度
     * 
     * @param activity
     * @return
     */ 
    public static int getScreenBrightness(Activity activity) { 
        int brightnessValue = 0; 
        try { 
            brightnessValue = android.provider.Settings.System.getInt( 
                    activity.getContentResolver(), 
                    Settings.System.SCREEN_BRIGHTNESS); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return brightnessValue; 
    } 
   
    /**
     * 设置屏幕亮度
     * 
     * @param activity
     * @param brightness
     */ 
    public static void setBrightness(Activity activity, int brightness) { 
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes(); 
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f); 
        activity.getWindow().setAttributes(lp); 
    } 
   
    /**
     * 关闭亮度自动调节
     * 
     * @param activity
     */ 
    public static void stopAutoBrightness(Activity activity) { 
        Settings.System.putInt(activity.getContentResolver(), 
                Settings.System.SCREEN_BRIGHTNESS_MODE, 
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL); 
    } 
   
    /**
     * 开启亮度自动调节
     * 
     * @param activity
     */ 
   
    public static void startAutoBrightness(Activity activity) { 
        Settings.System.putInt(activity.getContentResolver(), 
                Settings.System.SCREEN_BRIGHTNESS_MODE, 
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC); 
    } 
   
    /**
     * 保存亮度设置状态
     * 
     * @param activity
     * @param brightness
     */ 
    public static void saveBrightness(Activity activity, int brightness) { 
        Uri uri = android.provider.Settings.System 
                .getUriFor("screen_brightness"); 
        ContentResolver resolver = activity.getContentResolver(); 
        android.provider.Settings.System.putInt(resolver, "screen_brightness", 
                brightness); 
        resolver.notifyChange(uri, null); 
    } 

}
