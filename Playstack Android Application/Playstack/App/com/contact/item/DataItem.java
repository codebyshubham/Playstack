package com.contact.item;

import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Data;

import com.temp.App;

public class DataItem {
	
	private final ContentValues values;
	
	protected DataItem(ContentValues values) {
		this.values = values;
	} 
	
	
	
	public static DataItem createFrom(ContentValues values) {
        final String mimeType = values.getAsString(Data.MIMETYPE);
        if (GroupMembership.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","group");
            return new GroupMembershipDataItem(values);
        } /*else if (StructuredName.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","name");
            return new StructuredNameDataItem(values);
        }*/ else if (Phone.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","phone");
            return new PhoneDataItem(values);
        } else if (Email.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","email");
            return new EmailDataItem(values);
        }/* else if (StructuredPostal.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","postal");
            return new StructuredPostalDataItem(values);
        } else if (Im.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","im");
            return new ImDataItem(values);
        } else if (Organization.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","organization");
            return new OrganizationDataItem(values);
        } else if (Nickname.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","nickname");
            return new NicknameDataItem(values);
        } else if (Note.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","note");
            return new NoteDataItem(values);
        } else if (Website.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","website");
            return new WebsiteDataItem(values);
        } else if (SipAddress.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","sipaddress");
            return new SipAddressDataItem(values);
        } else if (Event.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","event");
            return new EventDataItem(values);
        } else if (Relation.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","relation");
            return new RelationDataItem(values);
        } else if (Identity.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","identity");
            return new IdentityDataItem(values);
        }*/ else if (Photo.CONTENT_ITEM_TYPE.equals(mimeType)) {
            values.put("_type","photo");
            return new PhotoDataItem(values);
        }
        return new DataItem(values);
    }
	
	
	
	public ContentValues getContentValues() {
        return values;
    }
	
	public void setRawContactId(long rawContactId) {
	    values.put(Data.RAW_CONTACT_ID, rawContactId);
	}
	
	public long getId() {
        return values.getAsLong(Data._ID);
    }
	
	public String getMimeType() {
        return values.getAsString(Data.MIMETYPE);
    }
	
	public Uri getDataUri(){
		return ContentUris.withAppendedId(Data.CONTENT_URI, getId());
	}
	
	
	public void setMimeType(String mimeType) {
        values.put(Data.MIMETYPE, mimeType);
    }

    public boolean isPrimary() {
        Integer primary = values.getAsInteger(Data.IS_PRIMARY);
        return primary != null && primary != 0;
    }

    public boolean isSuperPrimary() {
        Integer superPrimary = values.getAsInteger(Data.IS_SUPER_PRIMARY);
        return superPrimary != null && superPrimary != 0;
    }

    
    /*
    public boolean hasKindTypeColumn(DataKind kind) {
        final String key = kind.typeColumn;
        return key != null && values.containsKey(key) && values.getAsInteger(key) != null;
    }

    public int getKindTypeColumn(DataKind kind) {
        final String key = kind.typeColumn;
        return values.getAsInteger(key);
    }
    */
    
    public static class DataItemQurey{
    	public static final String[] COLUMNS = new String[]{
    		Data._ID,
    		Data.MIMETYPE,
    		Data.RAW_CONTACT_ID,
    		Data.IS_PRIMARY,
    		Data.IS_SUPER_PRIMARY,
    		Data.DATA_VERSION,
    		
    		Data.DATA1,
    		Data.DATA2,
    		Data.DATA3,
    		Data.DATA4,
    		Data.DATA5,
    		Data.DATA6,
    		Data.DATA7,
    		Data.DATA8,
    		Data.DATA9,
    		Data.DATA10,
    		Data.DATA11,
    		Data.DATA12,
    		Data.DATA13,
    		Data.DATA14,
    		Data.DATA15,
    		
    		Data.SYNC1,
    		Data.SYNC2,
    		Data.SYNC3,
    		Data.SYNC4
    	};
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
