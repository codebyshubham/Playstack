package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Website;

public class WebsiteDataItem extends DataItem {

	public WebsiteDataItem(ContentValues values) {
		super(values);
	}

	public String getUrl() {
		return getContentValues().getAsString(Website.URL);
	}

	public String getLabel() {
		return getContentValues().getAsString(Website.LABEL);
	}
}
