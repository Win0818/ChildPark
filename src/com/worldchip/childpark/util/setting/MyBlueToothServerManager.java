package com.worldchip.childpark.util.setting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.worldchip.childpark.Comments.Comments;

/**
 * 客户端(主机)
 * 
 * **/
public class MyBlueToothServerManager {

	// 蓝牙组件
	private BluetoothServerSocket mServerSocket;
	private BluetoothSocket socket;
	private Handler mHandler;
	private static ExecutorService mExecutorService = Executors
			.newFixedThreadPool(10);
	private boolean isExit = true;
	private static MyBlueToothServerManager mInstance = null;
	private Object lock = new Object();

	public synchronized static MyBlueToothServerManager getInstance() {
		if (mInstance == null) {
			mInstance = new MyBlueToothServerManager();
		}
		return mInstance;
	}

	public void createJoin(Handler handler) {
		this.mHandler = handler;
		initBluetooth();
	}

	public void initBluetooth() {
		AcceptThread serverThread = new AcceptThread();
		serverThread.start();
	}

	public void writeMsg(String msg) {
		mExecutorService.execute(new WriteThread(msg));
	}

	public void exitGame() {
		isExit = true;
		try {
			mServerSocket.close();
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 服务器段的线程
	private class AcceptThread extends Thread {

		public void run() {
			try {
				BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
				mServerSocket = adapter.listenUsingRfcommWithServiceRecord(
						Comments.serverName, Comments.uuid);
				//getState
				Log.e("server", "等待连接");
				Message msg1 = new Message();
				msg1.what = Comments.CONNECTING;
				mHandler.sendMessage(msg1);
				socket = mServerSocket.accept();
				Message msg2 = mHandler.obtainMessage(Comments.CONNECT_OK);
				mHandler.sendMessage(msg2);
				Log.e("server", "已经连接上");
				isExit = false;
				new ReadThread().start();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
					mmOutStream.write((data + ",").getBytes());// 以","来区分每次发送的数据
					Log.e("lee", "-------GameServerManager sendMsg : " + data
							+ "已经发送");
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (!isExit) {
				try {
					// Read from the InputStream
					if ((bytes = mmInStream.read(buffer)) > 0) {
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						String msg = new String(buf_data);
						Log.e("lee", "GameServerManager msg = " + msg);
						String[] dataString = msg.split(",");
						for (int i = 0; i < dataString.length; i++) {
							if (dataString[i].equals("begin")) {
								
							} else {
								
							}
						}
					}
				} catch (IOException e) {
					try {
						mmInStream.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}

		}

	}


}