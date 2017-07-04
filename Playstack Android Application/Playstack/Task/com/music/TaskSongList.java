package com.music;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskSongList implements WSBaseTask{
	
	private Context context = MyService.context;
	
	

	@Override
	public Object work(String[] args) {
		final ContentResolver resolver = context.getContentResolver();
		Ans ans = new Ans();
		
		final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION
        };
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        final Cursor cursor = resolver.query(uri, cursor_cols, where, null, null);

        if(cursor != null){
            final int indexID = cursor.getColumnIndex( MediaStore.Audio.Media._ID);
            final int indexARTIST = cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST);
            final int indexALBUM = cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM);
            final int indexTITLE = cursor.getColumnIndex( MediaStore.Audio.Media.TITLE);
            final int indexSIZE = cursor.getColumnIndex( MediaStore.Audio.Media.SIZE);
            final int indexDATA = cursor.getColumnIndex( MediaStore.Audio.Media.DATA);
            final int indexALBUM_ID = cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM_ID);
            final int indexDURATION = cursor.getColumnIndex( MediaStore.Audio.Media.DURATION);


            String id,artist,album,title,size,data,albumId,duration;
            while (cursor.moveToNext()){
                id = cursor.getString(indexID);
                artist = cursor.getString(indexARTIST);
                album = cursor.getString(indexALBUM);
                title = cursor.getString(indexTITLE);
                size = cursor.getString(indexSIZE);
                data = cursor.getString(indexDATA);
                albumId = cursor.getString(indexALBUM_ID);
                duration = cursor.getString(indexDURATION);

                Ans.Song song = ans.new Song();
                song.id = id;
                song.artist = artist;
                song.album = album;
                song.title = title;
                song.size = size;
                song.data = data;
                song.albumId = albumId;
                song.duration = duration;
                ans.songs.add(song);
                

            }
            cursor.close();
        }
		
		return ans;
	}

	private class Ans{
        ArrayList<Song> songs;

        public Ans(){
            songs = new ArrayList<>();
        }

        public class Song{
            @SuppressWarnings("unused")
			String id,artist,album,title,size,data,albumId,duration;
        }
    }
}
