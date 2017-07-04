package com.notification;

import com.temp.App;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

@SuppressLint("NewApi")
public class NotificationListner extends NotificationListenerService {

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {

		String pack = sbn.getPackageName();
		String ticker = sbn.getNotification().tickerText.toString();
		Bundle extras = sbn.getNotification().extras;
		String title = extras.getString("android.title");
		String text = extras.getCharSequence("android.text").toString();
		
		
		App.log("pack:" + pack +"----ticker:" + ticker +"----title:"+title+"----text:" + text);
	}
	
	
	public void onNotificationRemoved(StatusBarNotification sbn) {
        App.log("Notification Removed");
 
    }

}
