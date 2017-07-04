package com.music;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskAlbumList implements WSBaseTask{
	
	private Context context = MyService.context;
	

	@Override
	public Object work(String[] args) {
		final ContentResolver resolver = context.getContentResolver();
		Ans ans = new Ans();
		
		final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {
        		MediaStore.Audio.Albums._ID,
        		MediaStore.Audio.Albums.ALBUM,
        		MediaStore.Audio.Albums.ARTIST,
        		MediaStore.Audio.Albums.ALBUM_ART,
        		MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };
        final Cursor cursor = resolver.query(uri, cursor_cols, null, null, MediaStore.Audio.Albums.ALBUM + " ASC");

        if(cursor != null){
            final int indexID = cursor.getColumnIndex( MediaStore.Audio.Albums._ID);
            final int indexALBUM = cursor.getColumnIndex( MediaStore.Audio.Albums.ALBUM);
            final int indexARTIST = cursor.getColumnIndex( MediaStore.Audio.Albums.ARTIST);
            final int indexALBUM_ART = cursor.getColumnIndex( MediaStore.Audio.Albums.ALBUM_ART);
            final int indexNUMBER_OF_SONGS = cursor.getColumnIndex( MediaStore.Audio.Albums.NUMBER_OF_SONGS);


            String id,album,artist,albumArt,numOfSongs;
            while (cursor.moveToNext()){
                id = cursor.getString(indexID);
                album = cursor.getString(indexALBUM);
                artist = cursor.getString(indexARTIST);
                albumArt = cursor.getString(indexALBUM_ART);
                numOfSongs = cursor.getString(indexNUMBER_OF_SONGS);
                

                Ans.Album a = ans.new Album();
                a.id = id;
                a.artist = artist;
                a.album = album;
                a.albumArt = albumArt;
                a.numOfSongs = numOfSongs;
                
                ans.albums.add(a);
                
            }
            cursor.close();
        }
		
		return ans;
	}

	private class Ans{
        ArrayList<Album> albums;

        public Ans(){
            albums = new ArrayList<>();
        }

        public class Album{
            @SuppressWarnings("unused")
            String id,album,artist,albumArt,numOfSongs;
        }
    }
}
