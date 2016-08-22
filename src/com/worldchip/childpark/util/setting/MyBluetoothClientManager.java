package com.worldchip.childpark.util.setting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.worldchip.childpark.Comments.Comments;

/**
 * 客户端
 **/
public class MyBluetoothClientManager {
	// 蓝牙组件
	private BluetoothAdapter adapter;
	private BluetoothSocket socket;
	private BluetoothDevice device;
	private Handler mHandler;
	private static MyBluetoothClientManager mInstance = null;
	private boolean isExit = true;
	private static ExecutorService mExecutorService = Executors
			.newFixedThreadPool(10);
	private String mSerachMacAddress = "";
	
	
	private Object lock = new Object();
	
	public synchronized static MyBluetoothClientManager getInstance() {
		if (mInstance == null) {
			mInstance = new MyBluetoothClientManager();
		}
		return mInstance;
	}
	
	

	public void CheckJoin(Handler handler,String macAddress) {
		this.mHandler = handler;
		this.mSerachMacAddress = macAddress;
		initBluetooth();
	}
	
	/**
	 * 初始化蓝牙设备，开启客户端线程
	 * **/
	private void initBluetooth() {
		adapter = BluetoothAdapter.getDefaultAdapter();
		adapter.cancelDiscovery();
		device = adapter.getRemoteDevice(mSerachMacAddress);
		ConnectThread clientThread = new ConnectThread();
		clientThread.start();
	}


	public void writeMsg(String msg) {
		mExecutorService.execute(new WriteThread(msg));
	}
	

	public void exitGame() {
		Log.e("chris", "exitGame -------------- >");
		isExit = true;
		if (adapter != null && adapter.isDiscovering()) {
			adapter.cancelDiscovery();
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket = null;
		}
	}
	
	

	// 客户端的线程
	private class ConnectThread extends Thread {
		@SuppressLint("NewApi") public void run() {
			try {
				Log.e("chris", "device = "+device);
				socket = device.createRfcommSocketToServiceRecord(Comments.uuid);
				Message msg1 = mHandler.obtainMessage(Comments.CONNECTING);
				mHandler.sendMessage(msg1);
				Log.e("chris", "正在创建客户端");
				if (!socket.isConnected()) {
					connectDevice();
				}
				Message msg2 = mHandler.obtainMessage(Comments.CONNECT_OK);
				mHandler.sendMessage(msg2);
				Log.e("chris", "已经连接上服务器");
				isExit = false;
				//new ReadThread().start();
			} catch (Exception e) {
				Log.e("chris", "Exception == " + e.toString());
				e.printStackTrace();
				Message msg = mHandler.obtainMessage(Comments.CONNECT_ERROR);
				mHandler.sendMessage(msg);
			}
		}
	}
	
	
	
	protected void connectDevice() {    
        try {    
            // 连接建立之前的先配对    
            if (device.getBondState() == BluetoothDevice.BOND_NONE) {    
                Method creMethod = BluetoothDevice.class    
                        .getMethod("createBond");    
                Log.e("TAG", "开始配对");    
                creMethod.invoke(device);    
            } else {    
            }    
        } catch (Exception e) {      
            e.printStackTrace();    
        }    
        adapter.cancelDiscovery();    
        try {    
            socket.connect();    
            
        } catch (IOException e) {    
            try {    
                socket.close();  
                socket = null;  
            } catch (IOException e2) {    
                  
            }    
        } finally {  
            
        }    
    }  

	
	// 发送数据线程
	private class WriteThread implements Runnable {
		String data = "";
		public WriteThread(String msg) {
			super();
			data = msg;
		}
		@Override
		public void run() {
			sendMsg(data);
		}
	}

	private void sendMsg(String data) {
		OutputStream mmOutStream = null;
		synchronized (lock) {
		try {
			if (socket != null) {
				mmOutStream = socket.getOutputStream();
				mmOutStream.write((data+",").getBytes());//以","来区分每次发送的数据
				Log.e("chris","-------GameClientManager sendMsg : "+ data + "已经发送");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		}
	}
	
	// 读取操作线程
	private class ReadThread extends Thread {
		@Override
		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;
			InputStream mmInStream = null;
			try {
				mmInStream = socket.getInputStream();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while (!isExit) {
				try {
					if ((bytes = mmInStream.read(buffer)) > 0) {
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						String msg = new String(buf_data);
						Log.e("chris", "GameClientManager msg = " + msg);
						String[] dataString = msg.split(",");
						for (int i = 0; i < dataString.length; i++) {
							if (dataString[i].equals("begin")) {} else {}
						}
					}
				} catch (IOException e) {
					try {
						mmInStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				}
			}
		}

	}
	
}