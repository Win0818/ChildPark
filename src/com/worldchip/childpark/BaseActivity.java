package com.worldchip.childpark;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldchip.childpark.Comments.Comments;
import com.worldchip.childpark.Comments.MySharePreData;
import com.worldchip.childpark.dialog.VolumeControlDialog;
import com.worldchip.childpark.util.net.HttpUtils;

public class BaseActivity extends Activity {

	protected static final String TAG = "CHRIS";

	private VolumeControlDialog mControlDialog;

	public LinearLayout mLinearLayout;

	private SimpleDateFormat mDateFormat = null;
	private Timer mTimer = null;

	private ImageView mIvWifi, mIvBlueTooth, mBetteryIcon;
	private TextView mTvTime, mTvBettery;
	public TextView mTvMode;
	private String mBetteryInfo;
	private int mBetteryLevel = 50;
	private int mBetteryScale = 100;
	private int mBetteryStatus = 0;
	private AnimationDrawable mBetteryCharge;
	private HashMap<String, String> mStatuesMap = new HashMap<String, String>();

	public String mPlayMsgId;
    protected Ringtone ringtone;
    private boolean isAnswered = false;

	private MediaPlayer mediaPlayer = null;
	
	private int  mcloseTime = 0;
	private String mMessageTxt;

	private boolean isPlayActivity = false;
	
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Comments.WIFI_IS_CONNECT:
				if (mIvWifi != null) {
					mIvWifi.setVisibility(View.VISIBLE);
				}
				Comments.IS_CONNECT_WIFI = true;
				break;

			case Comments.WIFI_NOT_CONNECT:
				if (mIvWifi != null) {
					mIvWifi.setVisibility(View.GONE);
				}
				Comments.IS_CONNECT_WIFI = false;
				break;
				
			case Comments.BLUETOOTH_IS_CONNECT:
				if (mIvBlueTooth != null) {
					mIvBlueTooth.setVisibility(View.VISIBLE);
				}
				Comments.BLUETOOTH_OPEN = true;

			case Comments.BLUETOOTH_NOT_CONNECT:
				if (mIvBlueTooth != null) {
					mIvBlueTooth.setVisibility(View.GONE);
				}
				Comments.BLUETOOTH_OPEN = false;
				break;

			case Comments.GET_SYSTEM_TIME:
				updateSystemTimer();
				break;

			case Comments.BETTERY_CHANGE_INFO:
				initBetteryIcon();
				break;

			case Comments.BETTERY_CHARGE_NOW:
				BetteryCharge();
				break;

			case Comments.BETTERY_DISCHARGE_NOW:
				initBetteryIcon();
				break;

			case Comments.VOLUME_DOWN:
				if (!Comments.VOLUME_DIALOG_SHOW) {
					showVolumedialog();
				}
				break;
				
			case Comments.VOLUME_UP:
				if (!Comments.VOLUME_DIALOG_SHOW)
					showVolumedialog();
				break;
			default:
				break;
			}
		}

	};

	private void initBetteryIcon() {
		moveBetteryAnim();
		setBetteryText();
		if (mBetteryLevel > 90) {
			mBetteryIcon.setImageResource(R.drawable.top_bettery_icon4_new);
		} else if (mBetteryLevel > 60 && mBetteryLevel <= 90) {
			mBetteryIcon.setImageResource(R.drawable.top_bettery_icon3_new);
		} else if (mBetteryLevel > 20 && mBetteryLevel <= 60) {
			mBetteryIcon.setImageResource(R.drawable.top_bettery_icon2_new);
		} else {
			mBetteryIcon.setImageResource(R.drawable.top_bettery_icon0_new);
		}
	}

	private void BetteryCharge() {
		setBetteryText();
		mBetteryIcon.setImageResource(R.drawable.gif_bettery_charg_anim_list);
		mBetteryCharge = (AnimationDrawable) mBetteryIcon.getDrawable();
		mBetteryCharge.start();
	}

	private void moveBetteryAnim() {
		if (mBetteryCharge != null && mBetteryIcon != null) {
			mBetteryIcon.clearAnimation();
		}
	}

	private void setBetteryText() {
		if (mTvBettery != null) {
			mBetteryInfo = (mBetteryLevel * 100 / mBetteryScale) + "%";
			Comments.BETTERY_LEVER_INFO = mBetteryInfo;
			mTvBettery.setText(Comments.BETTERY_LEVER_INFO);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void initBaseActivity() {
		mLinearLayout = (LinearLayout) findViewById(R.id.Llayout_top_bar);
		mIvWifi = (ImageView) findViewById(R.id.iv_topbar_wifi);
		mIvBlueTooth = (ImageView) findViewById(R.id.iv_topbar_bluetooth);
		mBetteryIcon = (ImageView) findViewById(R.id.iv_topBar_bettery);
		mTvTime = (TextView) findViewById(R.id.tv_topBar_date);
		mTvBettery = (TextView) findViewById(R.id.tv_topBar_bettery_number);
		mTvMode = (TextView) findViewById(R.id.tv_topBar_model);
		mDateFormat = new SimpleDateFormat("yyyy年MM月dd日  E HH:mm");
		mTvBettery.setText(Comments.BETTERY_LEVER_INFO);
		
		//showBlueToothIcon();
		registerBluetooth();
		registerWifi();
		registerBettery();
		startGetSystemTime();
		registerUsbReciver();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mBatteryInfoReceiver);
		unregisterReceiver(MyWifiBroadcastRecever);
	    unregisterReceiver(usbBroadCastReceiver);
	    if (MyBluetoothBroadcastReceiver != null) {
	    	unregisterReceiver(MyBluetoothBroadcastReceiver);
	    }
	}
	
	
	
	
	
	private void registerBluetooth() {
		IntentFilter bluetoothFilter = new IntentFilter();
		bluetoothFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		bluetoothFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		bluetoothFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(MyBluetoothBroadcastReceiver, bluetoothFilter);
	}
	
	

	private void registerWifi() {
		IntentFilter WifiFilter = new IntentFilter();
		WifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		WifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		WifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(MyWifiBroadcastRecever, WifiFilter);
	}

	private void registerBettery() {
		IntentFilter batteryFilter = new IntentFilter();
		batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		this.registerReceiver(mBatteryInfoReceiver, batteryFilter);
	}
	
	
	private void registerUsbReciver(){
		   IntentFilter iFilter = new IntentFilter();
	       iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
	       iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
	       iFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
	       iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
	       iFilter.addDataScheme("file");
	       registerReceiver(usbBroadCastReceiver, iFilter);
	}
	
	BroadcastReceiver MyBluetoothBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
			    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
			            BluetoothAdapter.ERROR);
			    switch (state) {
			        case BluetoothAdapter.STATE_OFF:
			            Log.d(TAG, "STATE_OFF 手机蓝牙关闭");
			            mHandler.removeMessages(Comments.BLUETOOTH_NOT_CONNECT);
						mHandler.sendEmptyMessage(Comments.BLUETOOTH_NOT_CONNECT);
			            break;
			        case BluetoothAdapter.STATE_TURNING_OFF:
			            Log.d(TAG, "STATE_TURNING_OFF 手机蓝牙正在关闭");
			            break;
			        case BluetoothAdapter.STATE_ON:
			            Log.d(TAG, "STATE_ON 手机蓝牙开启");
			            mHandler.removeMessages(Comments.BLUETOOTH_IS_CONNECT);
						mHandler.sendEmptyMessage(Comments.BLUETOOTH_IS_CONNECT);
			            break;
			        case BluetoothAdapter.STATE_TURNING_ON:
			            Log.d(TAG, "STATE_TURNING_ON 手机蓝牙正在开启");
			            break;
			    }
			}
		}
		
	};

	BroadcastReceiver MyWifiBroadcastRecever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiInfo != null && wifiInfo.isConnected()) {
				mHandler.removeMessages(Comments.WIFI_IS_CONNECT);
				mHandler.sendEmptyMessage(Comments.WIFI_IS_CONNECT);
			} else {
				mHandler.removeMessages(Comments.WIFI_NOT_CONNECT);
				mHandler.sendEmptyMessage(Comments.WIFI_NOT_CONNECT);
			}
		}

	};

	private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mBetteryStatus = intent.getIntExtra("status", 0);
			mBetteryLevel = intent.getIntExtra("level", 0);
			mBetteryScale = intent.getIntExtra("scale", 100);
			switch (mBetteryStatus){
			case BatteryManager.BATTERY_STATUS_CHARGING:
				mHandler.removeMessages(Comments.BETTERY_CHARGE_NOW);
				// mHandler.sendEmptyMessage(Comments.BETTERY_CHARGE_NOW);
				mHandler.sendEmptyMessageDelayed(Comments.BETTERY_CHARGE_NOW,
						2000);
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				mHandler.removeMessages(Comments.BETTERY_DISCHARGE_NOW);
				mHandler.sendEmptyMessageDelayed(
						Comments.BETTERY_DISCHARGE_NOW, 2000);
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				mHandler.removeMessages(Comments.BETTERY_DISCHARGE_NOW);
				mHandler.sendEmptyMessageDelayed(
						Comments.BETTERY_DISCHARGE_NOW, 2000);
				break;
			}
		}
	};

	private void updateSystemTimer() {
		if (mDateFormat != null && mTvTime != null) {
			String dateTime = mDateFormat.format(new Date());
			mTvTime.setText(dateTime);
		}
	}

	private void startGetSystemTime() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(Comments.GET_SYSTEM_TIME);
			}
		}, 0, 500);
		startSendDeviceStatues();
	}

	private void startSendDeviceStatues() {
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				sendDeviceStatues();
			}
		}, 1000*10 , 10*1000*60);
	}
	
	private void sendDeviceStatues() {
		if (Comments.IS_CONNECT_WIFI) {
			 new Thread(new Runnable() {
				@Override
				public void run() {
					mStatuesMap.put("token", MySharePreData.GetData(
							BaseActivity.this, Comments.SP_FIlE_NAMW, "token"));
					mStatuesMap.put("device_id", Comments.DEVICE_ID);
					mStatuesMap.put("kwh", mBetteryInfo);
					mStatuesMap.put("device_name", "");
					String result = null;
					try {
						result = HttpUtils.postRequest(
								Comments.DEVICE_STATUES_INFO, mStatuesMap,
								BaseActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.e(TAG, "---------发送设备状态返回数据------" + result);
				}
			}).start();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(isPlayActivity){
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				mHandler.removeMessages(Comments.VOLUME_DOWN);
				mHandler.sendEmptyMessage(Comments.VOLUME_DOWN);
				return true;

			case KeyEvent.KEYCODE_VOLUME_UP:
				mHandler.removeMessages(Comments.VOLUME_UP);
				mHandler.sendEmptyMessage(Comments.VOLUME_UP);
				Log.i(TAG, "----KEYCODE_VOLUME_UP--音量加-");
				return true;
		     case  KeyEvent.KEYCODE_DPAD_DOWN:
		    	 if(isPlayActivity){
		    		mHandler.removeMessages(Comments.VOLUME_DOWN);
		 			mHandler.sendEmptyMessage(Comments.VOLUME_DOWN); 
		 			Log.i(TAG, "--isPalyActivity--KEYCODE_DPAD_DOWN--音量减--------");
		    	 }
		    	 return  false;
		     case  KeyEvent.KEYCODE_DPAD_UP:
		    	 if(isPlayActivity){
			    		mHandler.removeMessages(Comments.VOLUME_UP);
			 			mHandler.sendEmptyMessage(Comments.VOLUME_UP); 
			 			Log.i(TAG, "--isPalyActivity--KEYCODE_DPAD_UP--音量加--------");
			    	 }
			       return  false;
			}	
		}else{
			switch (event.getKeyCode()) {
			  case KeyEvent.KEYCODE_VOLUME_DOWN:
				mHandler.removeMessages(Comments.VOLUME_DOWN);
				mHandler.sendEmptyMessage(Comments.VOLUME_DOWN);
				return true;

			  case KeyEvent.KEYCODE_VOLUME_UP:
				mHandler.removeMessages(Comments.VOLUME_UP);
				mHandler.sendEmptyMessage(Comments.VOLUME_UP);
				Log.i(TAG, "----KEYCODE_VOLUME_UP--音量加-");
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	public void showVolumedialog() {
		mControlDialog = VolumeControlDialog.createDialog(this);
		mControlDialog.showVolumeDialog();
		Comments.VOLUME_DIALOG_SHOW = true;
	}

	/*private void showBlueToothIcon() {
		if (Comments.BLUETOOTH_OPEN) {
			mIvBlueTooth.setVisibility(View.VISIBLE);
		} else {
			mIvBlueTooth.setVisibility(View.GONE);
		}
	}*/
	 
	  private BroadcastReceiver usbBroadCastReceiver = new  BroadcastReceiver(){
		      @Override
		      public void onReceive(Context context, Intent intent) {
		       String action = intent.getAction();
		       if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
		    	   Comments.USB_IS_MONT =  false;
		       } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
		    	   Comments.USB_IS_MONT =  true;
		        }
		       System.out.println("---usbBroadCastReceiver--"+Comments.USB_IS_MONT);
		      }
		 };
	

	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	public boolean isPalyActivity() {
		return isPlayActivity;
	}

	public void setPlayActivity(boolean isPalyActivity) {
		this.isPlayActivity = isPalyActivity;
	}
	

}
