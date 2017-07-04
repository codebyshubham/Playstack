package com.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskAppUninstall implements WSBaseTask{

	private Context context = MyService.context;
	@Override
	public Object work(String[] args) {
		
		
		Uri packageURI = Uri.parse("package:"+args[0]);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(uninstallIntent);
		
		return null;
	}

}
