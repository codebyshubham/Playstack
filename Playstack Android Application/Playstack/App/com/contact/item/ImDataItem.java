
package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;

public class ImDataItem extends DataItem {

    private final boolean mCreatedFromEmail;

    public ImDataItem(ContentValues values) {
        super(values);
        mCreatedFromEmail = false;
    }

    private ImDataItem(ContentValues values, boolean createdFromEmail) {
        super(values);
        mCreatedFromEmail = createdFromEmail;
    }

    public static ImDataItem createFromEmail(EmailDataItem item) {
        ImDataItem im = new ImDataItem(new ContentValues(item.getContentValues()), true);
        im.setMimeType(Im.CONTENT_ITEM_TYPE);
        return im;
    }

    public String getData() {
        if (mCreatedFromEmail) {
            return getContentValues().getAsString(Email.DATA);
        } else {
            return getContentValues().getAsString(Im.DATA);
        }
    }

    public String getLabel() {
        return getContentValues().getAsString(Im.LABEL);
    }

    public Integer getProtocol() {
        return getContentValues().getAsInteger(Im.PROTOCOL);
    }

    public boolean isProtocolValid() {
        return getProtocol() != null;
    }

    public String getCustomProtocol() {
        return getContentValues().getAsString(Im.CUSTOM_PROTOCOL);
    }

    public int getChatCapability() {
        Integer result = getContentValues().getAsInteger(Im.CHAT_CAPABILITY);
        return result == null ? 0 : result;
    }

    public boolean isCreatedFromEmail() {
        return mCreatedFromEmail;
    }
}
