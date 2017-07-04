package com.contact;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import com.contact.item.DataItem;
import com.contact.item.EmailDataItem;
import com.contact.item.GroupMembershipDataItem;
import com.contact.item.PhoneDataItem;
import com.temp.App;

import android.R.id;
import android.content.ContentValues;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;

public class RawContact {
	
	
	private final ContentValues values;
	private ArrayList<DataItem> groups;
	private ArrayList<DataItem> phones;
	private ArrayList<DataItem> emials;
	
	public RawContact(ContentValues values) {
		this.values = values;
	}
	
	public ArrayList<DataItem> getGroupsItem(){
		return groups;
	}
	
	public void setGropupsItems(ArrayList<DataItem> groups){
		this.groups = groups;
	}
	
	public ArrayList<DataItem> getPhonesItem(){
		return phones;
	}
	
	public void setPhonesItems(ArrayList<DataItem> phones){
		this.phones = phones;
	}
	
	public ArrayList<DataItem> getEmailsItem(){
		return emials;
	}
	
	public void setEmailsItems(ArrayList<DataItem> emials){
		this.emials = emials;
	}
	
	public ArrayList<String> getGroupIds(){
		ArrayList<String> ids = new ArrayList<>();
		for (DataItem item :groups) {
			ids.add(((GroupMembershipDataItem)item).getGroupRowId()+"");
		}
		return ids;
	}
	
	
	public ContentValues getValues() {
        return values;
    }
	
	public Long getId() {
        return getValues().getAsLong(RawContacts._ID);
    }
	
	public String getAccountName() {
        return getValues().getAsString(RawContacts.ACCOUNT_NAME);
    }
	
	public String getAccountTypeString() {
        return getValues().getAsString(RawContacts.ACCOUNT_TYPE);
    }
	
	public long getContactId() {
        return getValues().getAsLong(Contacts.Entity.CONTACT_ID);
    }

    public boolean isStarred() {
        return getValues().getAsBoolean(Contacts.STARRED);
    }
    
    public boolean isDeleted() {
        return getValues().getAsBoolean(RawContacts.DELETED);
    }
	
	

	
	
	
	
	
	
	
	public static class RawContactQuery{
		public static final String[] COLUMNS = new String[]{
			RawContacts._ID,
			RawContacts.CONTACT_ID,
			RawContacts.AGGREGATION_MODE,
			RawContacts.DELETED,
			RawContacts.TIMES_CONTACTED,
			RawContacts.LAST_TIME_CONTACTED,
			RawContacts.STARRED,
			RawContacts.CUSTOM_RINGTONE,
			RawContacts.SEND_TO_VOICEMAIL,
			RawContacts.ACCOUNT_NAME,
			RawContacts.ACCOUNT_TYPE,
			RawContacts.DATA_SET,
			RawContacts.SOURCE_ID,
			RawContacts.VERSION,
			RawContacts.DIRTY,
			RawContacts.SYNC1,
			RawContacts.SYNC2,
			RawContacts.SYNC3,
			RawContacts.SYNC4
		};
		
		public static final int _ID = 1;
		public static final int CONTACT_ID = 2;
		public static final int AGGREGATION_MODE = 3;
		public static final int DELETED = 4;
		public static final int TIMES_CONTACTED = 5;
		public static final int LAST_TIME_CONTACTED = 6;
		public static final int STARRED = 7;
		public static final int CUSTOM_RINGTONE = 8;
		public static final int SEND_TO_VOICEMAIL = 9;
		public static final int ACCOUNT_NAME = 10;
		public static final int ACCOUNT_TYPE = 11;
		public static final int DATA_SET = 12;
		public static final int SOURCE_ID = 13;
		public static final int VERSION = 14;
		public static final int DIRTY = 15;
		public static final int SYNC1 = 16;
		public static final int SYNC2 = 17;
		public static final int SYNC3 = 18;
		public static final int SYNC4 = 19;
		
	}
	
	@Override
    public String toString() {
    	
    	StringBuilder builder = new StringBuilder();
    	Set<Entry<String, Object>> set = values.valueSet();
    	
    	for (Entry<String, Object> entry : set) {
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
