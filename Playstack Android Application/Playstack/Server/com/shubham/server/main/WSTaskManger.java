package com.shubham.server.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.app.TaskAppList;
import com.app.TaskAppUninstall;
import com.call.TaskCall;
import com.call.TaskCallLogList;
import com.call.TaskDeleteCallLog;
import com.contact.TaskContact;
import com.contact.TaskContactList;
import com.contact.TaskGroupContactList;
import com.contact.TaskGroupList;
import com.file.TaskFileCopy;
import com.file.TaskFileDelete;
import com.file.TaskFileList;
import com.file.TaskFileMove;
import com.file.TaskFileRename;
import com.file.TaskStorageList;
import com.gallery.TaskGalleryAlbum;
import com.gallery.TaskGalleryAlbumList;
import com.google.gson.Gson;
import com.main.TaskSelectiveStatus;
import com.music.TaskAlbumList;
import com.music.TaskAlbumSongList;
import com.music.TaskSongList;
import com.shubham.server.bean.MsgBean;
import com.sms.TaskSendSms;
import com.sms.TaskSmsThread;
import com.sms.TaskSmsThreadList;

import fi.iki.elonen.NanoWSD.WebSocket;

public class WSTaskManger {

	public Context context;
	public Map<String, WSBaseTask> tasklist;
	public Gson gson;

	public WSTaskManger(Context context) {
		this.context = context;
		tasklist = new HashMap<>();
		gson = new Gson();
		
		//main
		registerTask("selective", new TaskSelectiveStatus());
		
		//music
		registerTask("song-list", new TaskSongList());
		registerTask("album-list", new TaskAlbumList());
		registerTask("album-song-list", new TaskAlbumSongList());
		
		//app
		registerTask("app-list", new TaskAppList());
		registerTask("app-uninstall", new TaskAppUninstall());
		
		//sms
		registerTask("sms-thread-list", new TaskSmsThreadList());
		registerTask("sms-thread", new TaskSmsThread());
		registerTask("sms-send", new TaskSendSms());
		
		//call-log
		registerTask("call-log-list", new TaskCallLogList());
		registerTask("call-log-delete", new TaskDeleteCallLog());
		registerTask("call-call", new TaskCall());
		
		//gallery
		registerTask("gallery-album-list", new TaskGalleryAlbumList());
		registerTask("gallery-album", new TaskGalleryAlbum());
		
		//file		
		registerTask("file-storage-list", new TaskStorageList());
		registerTask("file-file-list", new TaskFileList());
		registerTask("file-file-rename", new TaskFileRename());
		registerTask("file-file-move", new TaskFileMove());
		registerTask("file-file-copy", new TaskFileCopy());
		registerTask("file-file-delete", new TaskFileDelete());
		
		//contact
		registerTask("contact-contact-list", new TaskContactList());
		registerTask("contact-group-list", new TaskGroupList());
		registerTask("contact-group-contact-list", new TaskGroupContactList());
		registerTask("contact-contact", new TaskContact());
		
		
	}

	public void registerTask(String name, WSBaseTask task) {
		tasklist.put(name, task);
	}

	public void excuteTask(final WebSocket ws, final MsgBean msg) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				WSBaseTask task = tasklist.get(msg.name);
				WSData send = new WSData();
				send.count = msg.count;
				if (task != null) {
					try {
						//App.log("arg:" + msg.args);
						send.data = task.work(msg.args.toArray(new String[msg.args.size()]));
					} catch (Exception e) {
						send.data = "erroe";
						//App.log("task error: " + msg.name);
						e.printStackTrace();
					}
				} else {
					send.data = "404 task not found";
				}

				synchronized (ws) {
					try {
						ws.send(gson.toJson(send));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
}
