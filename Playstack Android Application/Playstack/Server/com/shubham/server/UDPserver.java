package com.shubham.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class UDPserver {

	public final String SPLIT = "%%";
	private int port;
	private DatagramSocket socket;
	private Thread thread;
	private boolean isServerRunning;
	private WifiManager wifiManager;
	private WifiManager.WifiLock lock;
	
	public UDPserver(Context context, int port) {
		this.port = port;
		
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		lock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "ok");
		lock.setReferenceCounted(true);
	}
	
	public void start() {
		if (isServerRunning) {
			return;
		}
		isServerRunning = true;
		try {
			socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true); 
			thread = new Thread(runnable);
			thread.start();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		socket.close();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			lock.acquire();
			while (!socket.isClosed()) {
				try {
					byte[] recvBuf = new byte[15000];
					DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
					socket.receive(packet);
					String message = new String(packet.getData()).trim();
					String ans = null;
					
					switch (message) {
					case "PLAYSTACK_SERVER_FIND_REQUEST":
						ans = "PLAYSTACK_SERVER_FIND_RESPONSE" + SPLIT + getMacAddress() + SPLIT + getDeviceName();
						break;

					default:
						break;
					}
					
					if (ans != null) {
						byte[] sendData = ans.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), 8080);
						socket.send(sendPacket);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			isServerRunning = false;
			if (lock.isHeld()) {
				lock.release();
			}
		}
	};
	
	public boolean isRunning(){
		return isServerRunning;
	}
	
	public String getMacAddress() {
		WifiInfo info = wifiManager.getConnectionInfo();
		String address = info.getMacAddress();
		return address;
	}
	
	public String getDeviceName() {
		return android.os.Build.DEVICE + "-" + android.os.Build.MODEL;
	}
}
