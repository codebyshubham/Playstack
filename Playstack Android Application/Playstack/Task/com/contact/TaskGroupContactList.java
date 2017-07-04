package com.contact;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskGroupContactList implements WSBaseTask{
    private Context context = MyService.context;

    @Override
    public Object work(String[] obj) {
        ContentResolver resolver = context.getContentResolver();
        Ans ans = new Ans();
        String _ID = obj[0].toString();
        //String _ID = "1";

        Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                        ContactsContract.Contacts.STARRED,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER,
                        ContactsContract.Contacts.LOOKUP_KEY
                },
                ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ?",
                new String[] {
                        _ID
                },
                null
        );
        if(cursor != null){
            final int indexID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            final int indexDISPLAY_NAME_PRIMARY = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
            final int indexSTARRED = cursor.getColumnIndex(ContactsContract.Contacts.STARRED);
            final int indexHAS_PHONE_NUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            final int indexLOOKUP = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);

            String id,name,star,hasNumber,lookup;
            while (cursor.moveToNext()){
                id = cursor.getString(indexID);
                name = cursor.getString(indexDISPLAY_NAME_PRIMARY);
                star = cursor.getString(indexSTARRED);
                hasNumber = cursor.getString(indexHAS_PHONE_NUMBER);
                lookup = cursor.getString(indexLOOKUP);

                Ans.Contact contact = ans.new Contact();
                contact.id = id;
                contact.name = name;
                contact.star = star;
                contact.hasNumber = hasNumber;
                contact.lookup = lookup;
                ans.contacts.add(contact);
            }
            cursor.close();
        }


        return ans;
    }


    @SuppressWarnings("unused")
    private class Ans{
        ArrayList<Contact> contacts;
        public Ans(){
            contacts = new ArrayList<>();
        }

        public class Contact{
			String id;
            String name;
            String star;
            String hasNumber;
            String lookup;
        }
    }
}
