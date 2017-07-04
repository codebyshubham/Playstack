package com.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.shubham.server.main.WSBaseTask;

public class TaskFileRename implements WSBaseTask {

	@Override
	public Object work(String[] args) {
		String _PATH = args[0];
		String _NEW_NAME = args[1];
		
		File file = FileUtils.getFile(_PATH);
		File newFile = FileUtils.getFile(file.getParentFile(), _NEW_NAME);
		
		try {
			FileUtils.moveFile(file, newFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return false;
	}
}
