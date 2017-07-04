
package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class EmailDataItem extends DataItem {

	public EmailDataItem(ContentValues values) {
		super(values);
    }

    public String getAddress() {
        return getContentValues().getAsString(Email.ADDRESS);
    }

    public String getDisplayName() {
        return getContentValues().getAsString(Email.DISPLAY_NAME);
    }


    public String getLabel() {
        return getContentValues().getAsString(Email.LABEL);
    }
}
