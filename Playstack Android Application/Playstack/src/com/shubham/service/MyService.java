package com.shubham.service;

import java.io.IOException;

import shubham.network.WIFINetwork;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.shubham.playstack.DataBase;
import com.shubham.playstack.MainActivity;
import com.shubham.server.NANOServer;
import com.shubham.server.UDPserver;

public class MyService extends Service{
	
	
	public static Context context;
	private int PORT = 8080;
	private UDPserver udp;
	private NANOServer nano;
	public static boolean isOn;
	public  static DataBase database;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		context = getBaseContext();
		isOn = true;

		database = new DataBase(context);
		
		
		udp = new UDPserver(getBaseContext(), PORT);
		nano = new NANOServer(getApplicationContext(), PORT);
		start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	

    private void start(){
    	udp.start();
    	try {
			nano.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	runAsForeground();
    }
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		udp.stop();
		nano.stop();
		stopAsForeground();
		isOn = false;
	}
	
	 public void stopAsForeground(){
	        stopForeground(true);
	    }
	
	  private void runAsForeground(){

			WIFINetwork wifi = new WIFINetwork(getBaseContext());
	    	wifi.updateConnectivity();
	    	String ip = wifi.getIp();
	    	
	        Intent notificationIntent = new Intent(this,MainActivity.class);
	        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


	        Notification notification=new NotificationCompat.Builder(this)
	                .setSmallIcon(com.shubham.playstack.R.drawable.ic_launcher)
	                .setContentText(ip)
	                .setContentTitle("Playstack runing...")
	                .setTicker("playstack runing...")
	                .setContentIntent(pendingIntent).build();

	        startForeground(1, notification);
	    }

}
