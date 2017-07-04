package com.contact.item;

import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;

public class GroupMembershipDataItem extends DataItem {

	public GroupMembershipDataItem(ContentValues values) {
		super(values);
	}

	public Long getGroupRowId() {
		return getContentValues().getAsLong(GroupMembership.GROUP_ROW_ID);
	}

	public String getGroupSourceId() {
		return getContentValues().getAsString(GroupMembership.GROUP_SOURCE_ID);
	}
}
	