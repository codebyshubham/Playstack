package com.main;

import java.util.HashMap;
import java.util.Map;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskSelectiveStatus implements WSBaseTask{

	Map<String, Boolean> list = new HashMap<>();
	
	
	@Override
	public Object work(String[] args) {
		list.clear();
		
		
		list.put("home", true);
		list.put("download", true);
		
		list.put("music", MyService.database.getBoolean("music"));
		list.put("gallery", MyService.database.getBoolean("gallery"));
		list.put("contact", MyService.database.getBoolean("contacts"));
		list.put("call-log", MyService.database.getBoolean("call_log"));
		list.put("file", MyService.database.getBoolean("file"));
		list.put("app", MyService.database.getBoolean("app"));
		list.put("sms", MyService.database.getBoolean("sms"));
		
		
		return list;
	}

}
