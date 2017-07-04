package com.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import com.shubham.server.HTTPBaseTask;
import com.shubham.server.HTTPResponeUtil;

import fi.iki.elonen.NanoHTTPD.Response;

public class TaskAppDownload implements HTTPBaseTask{

	@Override
	public Response work(Map<String, String> args) {
		String location = args.get("location");
		
		
		if(location == null)
			return null;
		
		try {
			return HTTPResponeUtil.newFixedFileResponse(new FileInputStream(location), "application/octet-stream");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
