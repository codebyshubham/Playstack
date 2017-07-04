package com.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.shubham.server.HTTPBaseTask;
import com.shubham.server.HTTPResponeUtil;
import com.shubham.server.NANOServer;
import com.temp.App;

import fi.iki.elonen.NanoHTTPD.Response;

public class TaskDownlodFile implements HTTPBaseTask{

	@Override
	public Response work(Map<String, String> args) {
		String _PATH = args.get("location");
		File file = new File(_PATH);
		
		
		App.log(_PATH);
		
		if(file != null && file.isFile()){
			try {
				return HTTPResponeUtil.newFixedFileResponse(new FileInputStream(_PATH), NANOServer.getMimeTypeForFile(FilenameUtils.getExtension(file.getName())));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
