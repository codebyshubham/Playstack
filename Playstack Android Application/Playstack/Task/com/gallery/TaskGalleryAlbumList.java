package com.gallery;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskGalleryAlbumList implements WSBaseTask {

	private Context context = MyService.context;
	private ArrayList<Album> albums = new ArrayList<>();
	
	@Override
	public Object work(String[] args) {
		
		albums.clear();

		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = new String[] {
				MediaStore.Images.Media.BUCKET_ID,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME, 
				MediaStore.Images.Media.DATE_TAKEN,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID};
		String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
		String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
		
		
		Cursor cursor = context.getContentResolver().query(uri, projection, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);
		
		
		
		if(cursor != null){
			final int indexBUCKET_ID = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
			final int indexBUCKET_DISPLAY_NAME = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			final int indexDATE_TAKEN = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
			final int indexDATA = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			final int indexID = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			
			String id,name,date,data,b_id;
			while (cursor.moveToNext()) {
				id = cursor.getString(indexID);
				name = cursor.getString(indexBUCKET_DISPLAY_NAME);
				date = cursor.getString(indexDATE_TAKEN);
				data = cursor.getString(indexDATA);
				b_id = cursor.getString(indexBUCKET_ID);
						
				
				Album album = new Album();
				album.id = id;
				album.name = name;
				album.date = date;
				album.data = data;
				album.b_id = b_id;
				
				albums.add(album);
				
			}
			cursor.close();
		}
		return albums;
	}

	
	@SuppressWarnings("unused")
	private class Album{
		String id;
		String name;
		String date;
		String data;
		String b_id;
	}
	
}
