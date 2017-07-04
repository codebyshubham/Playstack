package com.contact;

import android.content.ContentResolver;
import android.content.Context;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskContactNew implements WSBaseTask{
	private Context context = MyService.context;
	
	@Override
	public Object work(String[] args) {
		String _ID = args[0];
		ContentResolver resolver = context.getContentResolver();
		return null;
	}

}
