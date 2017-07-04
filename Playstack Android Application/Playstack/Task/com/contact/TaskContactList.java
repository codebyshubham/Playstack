package com.contact;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;


public class TaskContactList implements WSBaseTask {
    Context context = MyService.context;

    @Override
    public Object work(String[] obj) {
        ContentResolver resolver = context.getContentResolver();
        Ans ans = new Ans();


        Cursor cursorContacts = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[] {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.STARRED,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER,
                        ContactsContract.Contacts.PHOTO_ID,
                        ContactsContract.Contacts.LOOKUP_KEY
                },
                ContactsContract.Contacts.IN_VISIBLE_GROUP + "=?",
                new String[]{"1"},
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
        );


        if(cursorContacts != null){
            final int indexID = cursorContacts.getColumnIndex(ContactsContract.Contacts._ID);
            final int indexSTAR = cursorContacts.getColumnIndex(ContactsContract.Contacts.STARRED);
            final int indexNAME = cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
            final int indexPHOTO = cursorContacts.getColumnIndex(ContactsContract.Contacts.PHOTO_ID);
            final int indexLOOKUP_KEY = cursorContacts.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
            final int indexHAS_NUMBER = cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);


            while(cursorContacts.moveToNext()) {
                Ans.Contact contact = ans.new Contact();

                contact.id = cursorContacts.getString(indexID);
                contact.star = cursorContacts.getInt(indexSTAR) == 1;
                contact.name = cursorContacts.getString(indexNAME);
                contact.photo = cursorContacts.getString(indexPHOTO);
                contact.lookup = cursorContacts.getString(indexLOOKUP_KEY);

                ans.contacts.add(contact);
            }
            cursorContacts.close();
        }

        return ans.contacts;
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
            boolean star;
            String photo;
            String lookup;
        }
    }
}
