package com.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.shubham.server.main.WSBaseTask;

public class TaskFileDelete implements WSBaseTask{

	@Override
	public Object work(String[] args) {
		
		String _PATH = args[0];
		
		File file = new File(_PATH);
		try {
			if(file.isFile()){				
				FileUtils.forceDelete(file);
			}else{
				FileUtils.deleteDirectory(file);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
