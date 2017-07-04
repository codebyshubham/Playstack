package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Phone;


public class PhoneDataItem extends DataItem {

    public static final String KEY_FORMATTED_PHONE_NUMBER = "formattedPhoneNumber";

    PhoneDataItem(ContentValues values) {
        super(values);
    }

    public String getNumber() {
        return getContentValues().getAsString(Phone.NUMBER);
    }
    
    /*
    public String getNormalizedNumber() {
        return getContentValues().getAsString(Phone.NORMALIZED_NUMBER);
    }
    */

    public String getFormattedPhoneNumber() {
        return getContentValues().getAsString(KEY_FORMATTED_PHONE_NUMBER);
    }

    public String getLabel() {
        return getContentValues().getAsString(Phone.LABEL);
    }

    /*
    public void computeFormattedPhoneNumber(String defaultCountryIso) {
        final String phoneNumber = getNumber();
        if (phoneNumber != null) {
            final String formattedPhoneNumber = PhoneNumberUtils.formatNumber(phoneNumber,
                    getNormalizedNumber(), defaultCountryIso);
            getContentValues().put(KEY_FORMATTED_PHONE_NUMBER, formattedPhoneNumber);
        }
    }*/
    /*
    @Override
    public String buildDataStringForDisplay(Context context, DataKind kind) {
        final String formatted = getFormattedPhoneNumber();
        if (formatted != null) {
            return formatted;
        } else {
            return getNumber();
        }
    }*/
}
