package com.worldchip.childpark.Comments;

import java.io.File;
import java.util.UUID;

import android.graphics.Bitmap;
import android.os.Environment;

public class Comments {
	
	public final  static String  SP_FIlE_NAMW = "LoveBeam";//sharePrefrence
	public final static String SP_AD_TYPES = "ad_types";
	
	
	public static boolean IS_FIRST_LOGIN = false;//绗竴娆＄櫥褰� 鐨勬爣蹇�
	
	public static String  MY_TOKEN  = null; //myTokenZHi
	
	public static final String filepath = Environment.getExternalStorageDirectory()+
			File.separator+"OHP";
	
	public static final String absolutePath = Environment.getExternalStorageDirectory()+
			File.separator+"OHP"+File.separator+"ad";
	
	public final static  String  GET_TOKEN_VALUE = "http://121.199.77.215/index.php/admin/Restinfo/token";
	public final static  String  NET_LOGIN_URL = "http://121.199.77.215/index.php/admin/reg/doinstrlogin";
	public final static  String  NET_MUSIC_URL ="http://121.199.77.215/index.php/admin/Restinfo/restmusic";
	public final static  String  NET_VIDEO_URL ="http://121.37.32.37:8005/index.php/admin/Restinfo/restmovie";
	//public final static  String  NET_VIDEO_URL ="http://121.199.77.215/index.php/admin/Restinfo/restmovie";
	public final static  String  VIDEO_WATCH_LOG = "http://121.199.77.215/index.php/admin/Communicate/waRecorder";
	public final static  String  DEVICE_STATUES_INFO = "http://121.199.77.215/index.php/admin/Communicate/savestatus";
	public final static  String  GET_HUAN_XIN = "http://121.199.77.215/index.php/admin/Restinfo/gethuanxi";
	public final static  String  CHANGE_DIVICE_STATUES = "http://121.199.77.215/index.php/admin/ChatInfo/changstatus";
	
	
	
	public final static String  DEVICE_SYSTEM_FROM = "android"; //璁惧绯荤粺
	
	public final static  int WIFI_IS_CONNECT = 0x0001;
	public final static  int WIFI_NOT_CONNECT = 0x0002;
	public final static  int WIFI_REFRESH = 0x0003; 
	public final static  int WIFI_LOOP_TIME = 30 * 1000;
	public static boolean IS_CONNECT_WIFI = false;
	
	public static final int SECURITY_NONE = 0x0008;  
	public static final int SECURITY_WEP = 0x0005;  
	public static final int SECURITY_PSK = 0x0006;  
	public static final int SECURITY_EAP = 0x0007;  
	
	public static boolean ISSERVER = true;
	
	/**
	 * 鐢ㄤ簬钃濈墮涔嬮棿閫氫俊鐨勮〃绀篣UID
	 **/
	public final static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public final static String serverName = "fiveChess";
	public final static int CONNECTING = 0x0008;
	public final static int CONNECT_OK = 0x0009;
	public final static int CONNECT_ERROR = 0x0010;
	public static boolean  BLUETOOTH_OPEN = false;
	
	/**
	 * 瑙嗛瑙傜湅鐣岄潰鐨凷eekBar鏄剧ず鎺у埗
	 */
	
	public final static int VIDEO_SEEKBAR_SHOW= 0x0011;
	public final static int VIDEO_SEEKBAR_GONE= 0x0012;
	
	public static int MAIN_POSITION = 0;//璁板綍icon涓荤晫闈㈢殑鐐瑰嚮浣嶇疆
	
	public static boolean HAVE_WATCH_VIDEO = false;
	public static String MOV_NAME = "iii";//瑙嗛鐨勫悕瀛�
	public static String MOV_TYPE = "MP4";//瑙嗛鐨勬牸寮�
	public static Long   MOV_TIME = 0L;  //瑙嗛鐨勬椂闀�
	public static int MOVE_WATCH_TIME = 0; //瑙嗛瑙傜湅鐨勬椂闀�
	public static String DEVICE_ID = "1"; //璁惧鐨処D鍙�
	
	
	public final static int  BETTERY_CHANGE_INFO = 0x0013;//鐢垫睜鐘舵�佹敼鍙�
	public final static int  BETTERY_CHARGE_NOW  = 0x0014;//鐢垫睜鐘舵�佹敼鍙�
	public final static int  BETTERY_DISCHARGE_NOW = 0x0015;//鐢垫睜鐘舵�佹敼鍙�
	public static String     BETTERY_LEVER_INFO = "";//鐢垫睜鐘舵�佹敼鍙�
	
	public final static int GET_SYSTEM_TIME = 0x0016;//绯荤粺鏃堕棿鏀瑰彉
	
	public final static int VOLUME_UP = 0x0017;//闊抽噺鍔�
	public final static int VOLUME_DOWN = 0x0018;//闊抽噺鍑�
	public static boolean VOLUME_DIALOG_SHOW = false;//闊抽噺璋冭妭鐨刣ialog鏄剧ず鏍囧織
	
	
	public final static int HUANXIN_RECEIVE_VOICE= 0x0019;
	public final static int HUANXIN_RECEIVE_ORDER= 0x0020;
	
	public static boolean HUANXIN_LOGIN_INFO = false;
	
	public static int DEVICE_SYSTEM_LANGUAGE = 0;
	
	
	public static int SHUT_DOWN_TIME = 0;
	//ad bitmap
	public  static Bitmap  adBitMap;
	//USB is updata
	public static  boolean USB_IS_MONT = false;
	
	public static final int TAG_MEDIA_IN = 1000;
	public static final int TAG_USB_IN = 1001;
	public static final int TAG_SDCARD_IN = 1002;
	
	public static String EXTSDPATH = "/mnt/external_sd/";
	public static String EXTUSBPATH = "/mnt/usb_storate/USB_DISK0/";
}
