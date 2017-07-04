package com.call;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskCall implements WSBaseTask{

	private Context context = MyService.context;
	
	@Override
	public Object work(String[] args) {
		
		String _NUMBER = args[0];
		
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse("tel:" + _NUMBER));
		context.startActivity(intent);
		return null;
	}

}
