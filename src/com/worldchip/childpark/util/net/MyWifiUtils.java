package com.worldchip.childpark.util.net;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.worldchip.childpark.Comments.Comments;

public class MyWifiUtils {

	private static final String TAG = "CHRIS";
	private WifiManager mWifiManager;
	private List<ScanResult> mWifiResultList;
	private WifiInfo mWifiInfo;
	private ScanResult mConnectResult;
	private static MyWifiUtils mInstance = null;
	
	private List<WifiConfiguration> mWifiConfigurations;

	public static MyWifiUtils getInstance(Context context) {
		if (mInstance == null) {
			synchronized (MyWifiUtils.class) {
				if (mInstance == null) {
					mInstance = new MyWifiUtils(context);
				}
			}
		}
		return mInstance;
	}

	public MyWifiUtils(Context context) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 打开wifi
	 */
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 关闭wifi
	 */
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**
	 * 获取wifi状态
	 * @return
	 */
	public int getWifiState() {
		Log.i(TAG, "--------MyWifiUtils---wifi状态--------"+mWifiManager.getWifiState());
		return mWifiManager.getWifiState();
	}

	/**
	 * 扫描wifi
	 * @return
	 */
	public void searchWifi() {
		if (mWifiManager != null) {
			mWifiManager.startScan();
			if(mWifiResultList != null){
				mWifiResultList.clear();
			}
			mWifiResultList = mWifiManager.getScanResults();
			sortResultListForWifiSignal(mWifiResultList);
		}
	}

	public List<WifiConfiguration> getConfiguredNetworks() {
		return mWifiManager.getConfiguredNetworks();
	}

	public List<ScanResult> getScanrResults() {
		if (mWifiResultList != null) {
			return mWifiResultList;
		}
		return null;
	}

	// 提供一个外部接口，传入要连接的无线网
	public boolean connect(String SSID, String Password, int Type) {
		WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, Password, Type);
		if (wifiConfig == null) {
			return false;
		}
		mWifiManager.pingSupplicant();
		int netID = mWifiManager.addNetwork(wifiConfig);
		boolean bRet = mWifiManager.enableNetwork(netID, true);
		return bRet;
	}

	/**
	 * 指定配置好的网络进行连接
	 * @param index
	 */
	public void connectConfiguration(WifiConfiguration wcg) {
		mWifiManager.enableNetwork(wcg.networkId, true);
	}

	/**
	 * 添加一个网络并连接
	 */
	public int addNetwork(WifiConfiguration wcg) {
		return mWifiManager.addNetwork(wcg);
	}

	/**
	 * 添加一个网络并连接
	 */
	public boolean enableNetwork(int netId, boolean disableOthers) {
		return mWifiManager.enableNetwork(netId, disableOthers);
	}

	/**
	 * 断开指定ID的网络
	 */
	public void disConnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	/**
	 * 获取已经连接的wifi的信息
	 * @return
	 */
	public String getMacAddress() {
		refreshWifiInfo();
		return (mWifiInfo == null) ? "null" : mWifiInfo.getMacAddress();
	}

	public int getIpAddress() {
		refreshWifiInfo();
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	public String getBSSID() {
		refreshWifiInfo();
		return (mWifiInfo == null) ? "null" : mWifiInfo.getBSSID();
	}

	public int getNetworkId() {
		refreshWifiInfo();
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	public String getSSID() {
		refreshWifiInfo();
		return (mWifiInfo == null) ? "null" : mWifiInfo.getSSID();
	}

	
	public void refreshWifiInfo() {
		if (mWifiInfo != null) {
			mWifiInfo = null;
		}
		mWifiInfo = mWifiManager.getConnectionInfo();
	}
	
    /**
     * 保存配置好的wifi
     * @return
     */
	public boolean SaveWifiConfiguration() {
		if (mWifiInfo != null) {
			mWifiManager.saveConfiguration();
			Log.i(TAG, "---SaveWifiConfiguration-已经连接的信息-"+mWifiInfo.getSSID());
			refreshWifiInfo();
			return true;
		}
		return false;
	}
	
	
     /**
      * 配置一个wifi
      * @param SSID
      * @param Password
      * @param Type
      * @return
      */
	private WifiConfiguration CreateWifiInfo(String SSID, String Password,
			int Type) {
		
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		
		WifiConfiguration tempConfig = isExsits(SSID);

		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		if (Type == Comments.SECURITY_NONE) {
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			Log.i(TAG, "---MyWifiUtils--CreateWifiInfo----");
		}

		if (Type == Comments.SECURITY_WEP) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == Comments.SECURITY_PSK) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.status = WifiConfiguration.Status.ENABLED;
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
		} else {
			return null;
		}
		return config;
	}

	
	private WifiConfiguration isExsits(String SSID) {
		 mWifiConfigurations  = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : mWifiConfigurations) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	public int getSecurity(ScanResult result) {
		if (result.capabilities.contains("WEP")) {
			return Comments.SECURITY_WEP;
		} else if (result.capabilities.contains("PSK")) {
			return Comments.SECURITY_PSK;
		} else if (result.capabilities.contains("EAP")) {
			return Comments.SECURITY_WEP;
		}
		return Comments.SECURITY_NONE;
	}
	
	
	public ScanResult getmConnectResult() {
		if (mConnectResult != null) {
			return mConnectResult;
		}
		return null;
	}
	

	public void setmConnectResult(ScanResult mConnectResult) {
		this.mConnectResult = mConnectResult;
	}
	
	/**
	 * 按强度排序wifi列表
	 */
	private void sortResultListForWifiSignal(List<ScanResult> scanResultList) {
		for (int i = 0; i < scanResultList.size() - 1; i++) {
			for (int j = i + 1; j < scanResultList.size(); j++) {
				if (scanResultList.get(i).level < scanResultList.get(j).level) {
					ScanResult temp = null;
					temp = scanResultList.get(i);
					scanResultList.set(i, scanResultList.get(j));
					scanResultList.set(j, temp);
				}
			}
		}
	}

}
