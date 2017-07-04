package com.contact;

import android.content.ContentValues;
import android.database.Cursor;

public class ContactUtil {

	
	public static ContentValues cursorToContentValues(Cursor cursor) {
		ContentValues values = new ContentValues();
		
		if(cursor == null){
			return values;
		}

		String[] columns = cursor.getColumnNames();
	    int length = columns.length;
	    for (int i = 0; i < length; i++) {
	        switch (cursor.getType(i)) {
	            case Cursor.FIELD_TYPE_NULL:
	                values.putNull(columns[i]);
	                break;
	            case Cursor.FIELD_TYPE_INTEGER:
	                values.put(columns[i], cursor.getLong(i));
	                break;
	            case Cursor.FIELD_TYPE_FLOAT:
	                values.put(columns[i], cursor.getDouble(i));
	                break;
	            case Cursor.FIELD_TYPE_STRING:
	                values.put(columns[i], cursor.getString(i));
	                break;
	            case Cursor.FIELD_TYPE_BLOB:
	                values.put(columns[i], cursor.getBlob(i));
	                break;
	        }
	    }
	    return values;
	}
	
}
