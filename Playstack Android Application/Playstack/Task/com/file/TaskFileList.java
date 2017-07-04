package com.file;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import com.shubham.server.main.WSBaseTask;

public class TaskFileList implements WSBaseTask{

	
	
	@Override
	public Object work(String[] args) {

		ArrayList<Item> directoryList  = new ArrayList<>();
		ArrayList<Item> fileList = new ArrayList<>();
		
		String _PATH = args[0];	
		File root = new File(_PATH);
		
		if(root.exists() && root.isDirectory()){
			
			File[] files = root.listFiles();
			for (File file : files) {
				Item item = new Item();
				item.name = file.getName();
				item.path = file.getAbsolutePath();
				item.date = file.lastModified();
				
				if(file.isFile()){
					item.type = FilenameUtils.getExtension(file.getName());
					item.size = file.length();
					fileList.add(item);
				}else{
					directoryList.add(item);
				}
			}
		}
		
		
		Ans ans = new Ans();
		ans.directory = directoryList;
		ans.file = fileList;
		return ans;
	}
	
	
	
	
	@SuppressWarnings("unused")
	private class Ans{
		ArrayList<Item> directory;
		ArrayList<Item> file;
	}
	
	@SuppressWarnings("unused")
	private class Item{
		String name;
		long size;
		String path;
		String type;
		long date;
	}
}
