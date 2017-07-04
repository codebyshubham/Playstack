package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Identity;

public class IdentityDataItem extends DataItem {

	public IdentityDataItem(ContentValues values) {
		super(values);
	}

	public String getIdentity() {
		return getContentValues().getAsString(Identity.IDENTITY);
	}

	public String getNamespace() {
		return getContentValues().getAsString(Identity.NAMESPACE);
	}
}
