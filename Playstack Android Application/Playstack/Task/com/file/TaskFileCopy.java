package com.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.shubham.server.main.WSBaseTask;

public class TaskFileCopy implements WSBaseTask{

	@Override
	public Object work(String[] args) {
		
		String _PATH = args[0];
		String _DESTINATION = args[1];
		
		
		File file = new File(_PATH);
		File destination = new File(_DESTINATION);
		
		try {
			if(file.isFile()){				
				FileUtils.copyFileToDirectory(file, destination);
			}else{
				FileUtils.copyDirectoryToDirectory(file, destination);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
