package com.contact;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.contact.item.PhoneDataItem;
import com.temp.App;

import android.content.ContentValues;
import android.provider.ContactsContract.Contacts;


public class Contact {
	
	
	private ContentValues values;
	private ArrayList<RawContact> rawContacts;

	public ContentValues getValues(){
		return values;
	}
	
	public void setValue(ContentValues values){
		this.values = values;
	}
	
	
	public long getId(){
		return values.getAsLong(Contacts._ID);
	}
	
	public String getDisplayeNamePrimary(){
		return values.getAsString(Contacts.DISPLAY_NAME_PRIMARY);
	}
	public boolean getHasNumber() {
		return values.getAsBoolean(Contacts.HAS_PHONE_NUMBER);
	}
	public boolean isStared(){
		return values.getAsBoolean(Contacts.STARRED);
	}
	
	public ArrayList<String> getGropuIdFromMain(){
		return rawContacts.get(0).getGroupIds();
	}
	
	public String getMobileNumber(){
		if(getHasNumber()){
			PhoneDataItem phone = (PhoneDataItem) rawContacts.get(0).getPhonesItem().get(0);
			return phone.getNumber();
		}
		return null;
	}
	
	public String getlookup() {
		return values.getAsString(Contacts.LOOKUP_KEY);
	}
	
	
	public ArrayList<RawContact> getRawContacts(){
		return rawContacts;
	}
	
	
	public void setRawContacts(ArrayList<RawContact> rawContacts){
		this.rawContacts = rawContacts;
	}
	
	
	
	public static class ContactQurey{
		public  static final String[] COLUMNS = new String[]{
			Contacts._ID,
			Contacts.LOOKUP_KEY,
			Contacts.DISPLAY_NAME_PRIMARY,
			Contacts.PHOTO_ID,
			Contacts.PHOTO_URI,
			
			Contacts.PHOTO_THUMBNAIL_URI,
			Contacts.IN_VISIBLE_GROUP,
			Contacts.HAS_PHONE_NUMBER,
			Contacts.TIMES_CONTACTED,
			Contacts.LAST_TIME_CONTACTED,
			
			Contacts.STARRED,
			Contacts.CUSTOM_RINGTONE,
			Contacts.SEND_TO_VOICEMAIL,
			Contacts.CONTACT_PRESENCE,
			Contacts.CONTACT_STATUS,
			
			Contacts.CONTACT_STATUS_TIMESTAMP,
			Contacts.CONTACT_STATUS_RES_PACKAGE,
			Contacts.CONTACT_STATUS_LABEL,
			Contacts.CONTACT_STATUS_ICON
		};
		
		public static final int _ID = 1;
		public static final int LOOKUP_KEY = 2;
		public static final int DISPLAY_NAME_PRIMARY = 3;
		public static final int PHOTO_ID = 4;
		public static final int PHOTO_URI = 5;
		public static final int PHOTO_THUMBNAIL_URI = 6;
		public static final int IN_VISIBLE_GROUP = 7;
		public static final int HAS_PHONE_NUMBER = 8;
		public static final int TIMES_CONTACTED = 9;
		public static final int LAST_TIME_CONTACTED = 10;
		public static final int STARRED = 11;
		public static final int CUSTOM_RINGTONE = 12;
		public static final int SEND_TO_VOICEMAIL = 13;
		public static final int CONTACT_PRESENCE = 14;
		public static final int CONTACT_STATUS = 15;
		public static final int CONTACT_STATUS_TIMESTAMP = 16;
		public static final int CONTACT_STATUS_RES_PACKAGE = 17;
		public static final int CONTACT_STATUS_LABEL = 18;
		public static final int CONTACT_STATUS_ICON = 19;
		
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		Set<Map.Entry<String, Object>> set = values.valueSet();

		for (Map.Entry<String, Object> entry : set) {
			builder.append(entry.getKey() +": "+ objectToString(entry.getValue()) + App.NEW_LINE);
		}
		return builder.toString();
	}

	private String objectToString(Object object){
		if(object == null){
			return "null";
		}
		return object.toString();
	}

	

	
}
