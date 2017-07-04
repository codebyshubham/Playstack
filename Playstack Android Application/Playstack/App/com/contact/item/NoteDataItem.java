package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Note;

public class NoteDataItem extends DataItem {

	public NoteDataItem(ContentValues values) {
		super(values);
	}

	public String getNote() {
		return getContentValues().getAsString(Note.NOTE);
	}
}
