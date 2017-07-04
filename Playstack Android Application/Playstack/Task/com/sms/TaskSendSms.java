package com.sms;

import java.util.ArrayList;

import android.telephony.SmsManager;

import com.shubham.server.main.WSBaseTask;



public class TaskSendSms implements WSBaseTask{

	
	
	
	@Override
	public Object work(String[] args) {
		
		String _PHONE = args[0];
		String _MSG = args[1];
		
		sendLongSMS(_PHONE, _MSG);
		return null;
	}
	
	public void sendLongSMS(String number,String msg) {

	    SmsManager smsManager = SmsManager.getDefault();
	    ArrayList<String> parts = smsManager.divideMessage(msg); 
	    smsManager.sendMultipartTextMessage(number, null, parts, null, null);
	}
}

