package com.contact;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.shubham.server.HTTPBaseTask;
import com.shubham.service.MyService;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

public class TaskContactPhoto implements HTTPBaseTask{
	
	private Context context = MyService.context;
	private ContentResolver resolver = context.getContentResolver();

	@Override
	public Response work(Map<String, String> args) {
		String _ID = args.get("id");
        String _LOOKUP = args.get("lookup");
        boolean preferHigh = Boolean.parseBoolean(args.get("large"));
        
        Uri lookupUri = ContactsContract.Contacts.getLookupUri(Long.parseLong(_ID),_LOOKUP);
        if(lookupUri != null){
            InputStream photoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(resolver, lookupUri,preferHigh);
            if(photoInputStream != null){
                try {
                    return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK,"image/jpeg",photoInputStream,photoInputStream.available());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return null;
	}

}
