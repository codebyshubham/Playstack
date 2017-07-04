package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.Contacts.Photo;

public class PhotoDataItem extends DataItem {

	public PhotoDataItem(ContentValues values) {
		super(values);
	}

	public Long getPhotoFileId() {
		return getContentValues().getAsLong(Photo.PHOTO_FILE_ID);
	}

	public byte[] getPhoto() {
		return getContentValues().getAsByteArray(Photo.PHOTO);
	}
}
