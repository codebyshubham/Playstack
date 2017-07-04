package com.contact;

import java.util.ArrayList;

import com.contact.item.DataItem;
import com.contact.item.EmailDataItem;
import com.contact.item.GroupMembershipDataItem;
import com.contact.item.PhoneDataItem;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;



public class ContactLoader extends AsyncTaskLoader<Contact>{

	
	private int id;
	
	public ContactLoader(Context context,int id) {
		super(context);
		this.id = id;
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
	}

	@Override
	public Contact loadInBackground() {
		
		Contact contact = new Contact();
		Cursor contactCursor = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, Contact.ContactQurey.COLUMNS, ContactsContract.Contacts._ID +"=?", new String[]{id+""}, null);
		//contactCursor.moveToFirst();
		contact.setValue(ContactUtil.cursorToContentValues(contactCursor));
		
		
		
		
		Cursor rowCursor = getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, RawContact.RawContactQuery.COLUMNS, ContactsContract.RawContacts.CONTACT_ID +"=?", new String[]{id+""}, null);
		ArrayList<RawContact> rawContacts = loadRawContactsList(rowCursor);
		
		for (RawContact rawContact : rawContacts) {
			Cursor dataCursor = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, DataItem.DataItemQurey.COLUMNS, ContactsContract.Data.RAW_CONTACT_ID +"=?", new String[]{rawContact.getId()+""}, null);
			ArrayList<DataItem> dataItems = loadDataItemsList(dataCursor);
			
			
			ArrayList<DataItem> groups = new ArrayList<>();
			ArrayList<DataItem> phones = new ArrayList<>();
			ArrayList<DataItem> emails = new ArrayList<>();
			
			for (DataItem item : dataItems) {
				if(item.getClass() == GroupMembershipDataItem.class){
					groups.add(item);
				}else if(item.getClass() == PhoneDataItem.class){
					phones.add(item);
				}else if(item.getClass() == EmailDataItem.class){
					emails.add(item);
				}
			}
			rawContact.setEmailsItems(emails);
			rawContact.setGropupsItems(groups);
			rawContact.setPhonesItems(phones);
			
			//rawContact.setDataItems(dataItems);
		}	

		contact.setRawContacts(rawContacts);
		return contact;		
	}
	
	
	
	private ArrayList<DataItem> loadDataItemsList(Cursor cursor){
		ArrayList<DataItem> dataItems = new ArrayList<DataItem>();
		if(cursor != null && cursor.moveToFirst()){
			do {
				
				DataItem item = DataItem.createFrom(ContactUtil.cursorToContentValues(cursor));
				dataItems.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataItems;
	}
	
	
	private ArrayList<RawContact> loadRawContactsList(Cursor cursor) {	
		ArrayList<RawContact> rawContacts = new ArrayList<RawContact>();
		if(cursor != null && cursor.moveToFirst()){
			do {
				RawContact raw = new RawContact(ContactUtil.cursorToContentValues(cursor)); 
				rawContacts.add(raw);
				
			} while (cursor.moveToNext());
		}
		cursor.close();
		return rawContacts;
	}
	
}
