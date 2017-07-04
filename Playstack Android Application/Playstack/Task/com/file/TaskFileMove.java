package com.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.shubham.server.main.WSBaseTask;

public class TaskFileMove implements WSBaseTask{

	@Override
	public Object work(String[] args) {
		String _PATH = args[0];
		String _DESTINATION = args[1];
		
		
		File file = new File(_PATH);
		File destination = new File(_DESTINATION);
		
		try {
			FileUtils.moveToDirectory(file, destination, false);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return false;
	}

}
