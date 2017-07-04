package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Relation;

public class RelationDataItem extends DataItem {

	public RelationDataItem(ContentValues values) {
		super(values);
	}

	public String getName() {
		return getContentValues().getAsString(Relation.NAME);
	}

	public String getLabel() {
		return getContentValues().getAsString(Relation.LABEL);
	}
}
