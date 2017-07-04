package com.music;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import com.shubham.server.HTTPBaseTask;
import com.shubham.server.HTTPResponeUtil;

import fi.iki.elonen.NanoHTTPD.Response;

public class TaskSong implements HTTPBaseTask{

	@Override
	public Response work(Map<String, String> args) {
		String location = args.get("location");
		if(location == null){
			return null;
		}
		
		try {
			Response response = HTTPResponeUtil.newFixedFileResponse(new FileInputStream(location), "audio/mpeg");
			response.addHeader("Access-Control-Allow-Origin", "*");
			
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
