package com.gallery;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import com.shubham.server.HTTPBaseTask;
import com.shubham.server.HTTPResponeUtil;

import fi.iki.elonen.NanoHTTPD.Response;

public class TaskGalleryPhoto implements HTTPBaseTask{

	@Override
	public Response work(Map<String, String> args) {

		String _PATH = args.get("location");
		if(_PATH == null){
			return null;
		}
		try {
			return HTTPResponeUtil.newFixedFileResponse(new FileInputStream(_PATH), "image/png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
