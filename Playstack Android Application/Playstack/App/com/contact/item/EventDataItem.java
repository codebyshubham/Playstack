package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Event;

public class EventDataItem extends DataItem {

	public EventDataItem(ContentValues values) {
		super(values);
	}

	public String getStartDate() {
		return getContentValues().getAsString(Event.START_DATE);
	}

	public String getLabel() {
		return getContentValues().getAsString(Event.LABEL);
	}
}
