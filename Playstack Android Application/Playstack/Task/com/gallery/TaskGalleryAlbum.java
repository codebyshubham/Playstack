package com.gallery;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskGalleryAlbum implements WSBaseTask {

	private Context context = MyService.context;
	private ArrayList<Item> list = new ArrayList<>();
	
	
	
	@Override
	public Object work(String[] args) {
		
		
		list.clear();
		String _BUCKET_ID = args[0];
		
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = new String[] {
				MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATE_TAKEN,
				MediaStore.Images.Media.DATA 
		};
		String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
		String WHERE = MediaStore.Images.Media.BUCKET_ID +" = '" + _BUCKET_ID+"'";
		Cursor cursor = context.getContentResolver().query(uri, projection, WHERE, null, BUCKET_ORDER_BY);
		
		
		if(cursor != null){
			
			final int indexID = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			final int indexDATE_TAKEN = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
			final int indexDATA = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			
			String id,date,data;
			while (cursor.moveToNext()) {
				id = cursor.getString(indexID);
				date = cursor.getString(indexDATE_TAKEN);
				data = cursor.getString(indexDATA);
				
				Item item = new Item();
				item.id = id;
				item.date = date;
				item.data = data;
				
				File file = new File(data);
				if(file.exists()){
					item.fname = file.getName();
				}
				
				list.add(item);	
			}	
			cursor.close();
		}
		return list;
	}

	public class Item{
		String id;
		String date;
		String data;
		String fname;
	}
}
