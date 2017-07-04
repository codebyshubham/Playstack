package com.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskSmsThreadList implements WSBaseTask{

	private Context context = MyService.context;
	
	
	@Override
	public Object work(String[] args) {
		
		Map<String, Contact> contacts = new HashMap<>();
		ArrayList<Detail> details = new ArrayList<>();
		Map<String,Thread> threads = new HashMap<>();
		Map<String, Boolean> unread = new HashMap<>();
		ContentResolver resolver = context.getContentResolver();
		
		
		
		//contact list with number
        Cursor cursorContactList = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_ID
                },
                ContactsContract.CommonDataKinds.Phone.NUMBER + "!=?",
                new String[]{""},
                null
        );
        if(cursorContactList != null){
            final int indexDISPLAY_NAME = 0;
            final int indexNUMBER = 1;
            final int indexLOOKUP = 2;
            final int indexPHOTO_ID = 3;

            String name,number,lookup,photoId;
            while (cursorContactList.moveToNext()){
                name = cursorContactList.getString(indexDISPLAY_NAME);
                number = cursorContactList.getString(indexNUMBER);
                lookup = cursorContactList.getString(indexLOOKUP);
                photoId = cursorContactList.getString(indexPHOTO_ID);



                Contact contact = new Contact();
                contact.name = name;
                contact.number = number;
                contact.lookup = lookup;
                contact.photo = photoId;
                contacts.put(contact.number, contact);
            }
            cursorContactList.close();
        }
        
        
        
        //sms thread list
        Uri uriThreadList = Uri.parse("content://mms-sms/canonical-addresses");
        Cursor cursorThreadList = resolver.query(uriThreadList, null, null, null, null);
        if(cursorThreadList != null){
            final int indexID = 0;
            final int indexADREES = 1;

            String id,address;
            while (cursorThreadList.moveToNext()){
                id = cursorThreadList.getString(indexID);
                address = cursorThreadList.getString(indexADREES);//mobile no

                Thread thread = new Thread();
                thread.id = id;
                thread.address = address;
                threads.put(thread.id, thread);
            }
            cursorThreadList.close();
        }
        
        
        //unread thread
        Uri uriUnread = Uri.parse("content://sms");
        Cursor cursorUnread = resolver.query(
                uriUnread,
                new String[]{"thread_id", "read"},
                "read = ?",
                new String[]{"0"},
                null
        );

        if(cursorUnread != null){
            final int indexID = 0;

            String id;
            while (cursorUnread.moveToNext()){
                id = cursorUnread.getString(indexID);
                unread.put(id, true);
            }
            cursorUnread.close();
        }
        
        
        
        //last sms in thread
        Uri uriInbox= Uri.parse("content://mms-sms/conversations?simple=true");
        Cursor cursorInbox = resolver.query(uriInbox,
                new String[]{
                        "_id",
                        "date",
                        "message_count",
                        "recipient_ids",
                        "snippet"
                }, null, null, " ORDER BY date DESE"
        );
        if(cursorInbox != null){
            final int indexID = 0;
            final int indexDATE = 1;
            final int indexCOUNT = 2;
            final int indexRECIIPENT = 3;
            final int indexSNIPPET = 4;

            String id,date,count,recipient,snippet;
            while (cursorInbox.moveToNext()){
                id = cursorInbox.getString(indexID);
                date = cursorInbox.getString(indexDATE);
                count = cursorInbox.getString(indexCOUNT);
                recipient = cursorInbox.getString(indexRECIIPENT);
                snippet = cursorInbox.getString(indexSNIPPET);



                Detail detail = new Detail();
                detail.id = id;
                detail.date = date;
                detail.count = count;
                //detail.recipient = recipient;
                detail.snippet = snippet;
                
                detail.address = threads.get(recipient) != null ? threads.get(recipient).address : null; 
                if(detail.address != null){
                	detail.name = contacts.get(detail.address) != null ? contacts.get(detail.address).name : null;                	
                }
                	
                detail.unread = unread.get(detail.id) != null;
                
                details.add(detail);
            }
            cursorInbox.close();
        }
        
        

		
		return details;
	}
	
	
	
	private class Thread{
        String id;
        String address;
    }


    @SuppressWarnings("unused")
	private class Contact{
		String name;
        String number;
        String lookup;
        String photo;
    }

    @SuppressWarnings("unused")
    private class Detail{
        String id;
        String date;
        String count;
        //String recipient;
        String snippet;
        boolean unread;
        String address;
        String name;
        
        Thread thread;
        Contact contact;
    }

}
