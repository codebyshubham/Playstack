package com.call;

import android.content.Context;
import android.provider.CallLog;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskDeleteCallLog implements WSBaseTask{

	
	private Context context = MyService.context;
	
	@Override
	public Object work(String[] args) {
		
		String _ID = args[0];
		int ans = context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,CallLog.Calls._ID + " = ? ",new String[] { String.valueOf(_ID) });
		return ans;
	}

}
