package com.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.contact.item.DataItem;
import com.contact.item.EmailDataItem;
import com.contact.item.GroupMembershipDataItem;
import com.contact.item.PhoneDataItem;
import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskContact implements WSBaseTask {

    private Context context = MyService.context;


    @Override
    public Object work(String[] obj) {

        String _ID = obj[0];
        ContentResolver resolver = context.getContentResolver();



        //for contacrt
        Contact contact = new Contact();
        Cursor contactCursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                Contact.ContactQurey.COLUMNS,
                ContactsContract.Contacts._ID +"=?",
                new String[]{
                        _ID
                },
                null
        );

        if(contactCursor != null){
            contactCursor.moveToFirst();
        }
        contact.setValue(ContactUtil.cursorToContentValues(contactCursor));
        if(contactCursor != null){
            contactCursor.close();
        }



        //for rawContact
        Cursor rowCursor = resolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                RawContact.RawContactQuery.COLUMNS,
                ContactsContract.RawContacts.CONTACT_ID +"=?",
                new String[]{
                        _ID
                },
                null
        );
        ArrayList<RawContact> rawContacts = loadRawContactsList(rowCursor);
        if(rowCursor != null){
            rowCursor.close();
        }




        //data for each rawContact...
        for (RawContact rawContact : rawContacts) {
            Cursor dataCursor = resolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    DataItem.DataItemQurey.COLUMNS,
                    ContactsContract.Data.RAW_CONTACT_ID +"=?",
                    new String[]{
                            rawContact.getId()+""
                    },
                    null
            );
            ArrayList<DataItem> dataItems = loadDataItemsList(dataCursor);
            //rawContact.setDataItems(dataItems);
            

			ArrayList<DataItem> groups = new ArrayList<>();
			ArrayList<DataItem> phones = new ArrayList<>();
			ArrayList<DataItem> emails = new ArrayList<>();
			
			for (DataItem item : dataItems) {
				if(item.getClass() == GroupMembershipDataItem.class){
					groups.add(item);
				}else if(item.getClass() == PhoneDataItem.class){
					phones.add(item);
				}else if(item.getClass() == EmailDataItem.class){
					emails.add(item);
				}
			}
			rawContact.setEmailsItems(emails);
			rawContact.setGropupsItems(groups);
			rawContact.setPhonesItems(phones);
            
            

            if(dataCursor != null){
                dataCursor.close();
            }
        }
        contact.setRawContacts(rawContacts);


        Ans ans = new Ans(contact);
        //for group list
        Cursor cursor= resolver.query(ContactsContract.Groups.CONTENT_URI,
                new String[] {
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE
                },
                null, null, null
        );
        if(cursor != null){
            final int indexID = cursor.getColumnIndex(ContactsContract.Groups._ID);
            final int indexTITLE = cursor.getColumnIndex(ContactsContract.Groups.TITLE);

            String id,title;
            while (cursor.moveToNext()){
                id = cursor.getString(indexID);
                title = cursor.getString(indexTITLE);

                ans.groups.put(id,title);
            }
            cursor.close();
        }


        //Thread.sleep(2500);

        //Log.i("My","Gson:" + new Gson().toJson(new Ans(contact)));
        //return ans;
        
        AnsNew ansNew = new AnsNew();
        ansNew.groups = ans.groups;
        ansNew.id = contact.getId();
        ansNew.name = contact.getDisplayeNamePrimary();
        ansNew.isHasNumber = contact.getHasNumber();
        ansNew.number = contact.getMobileNumber();
        ansNew.ans = ans;
        ansNew.gropusIn = contact.getGropuIdFromMain();
        ansNew.lookup = contact.getlookup();
        
        
        
        
        return ansNew;
    }

    
    private class AnsNew{
    	Map<String,String> groups;
    	long id;
    	String name;
    	boolean isHasNumber;
    	String number;
    	Ans ans;
    	ArrayList<String> gropusIn;
    	String lookup;
    	
    	public AnsNew() {
			groups = new HashMap<>();
		}
    	
    }
    

    private class Ans{
        Map<String,String> contact;
        ArrayList<Raw> raws;
        Map<String,String> groups;

        public Ans(Contact c){
            contact = new HashMap<>();
            raws = new ArrayList<>();
            groups = new HashMap<>();


            //for contact
            ContentValues s = c.getValues();
            Set<String> set = s.keySet();
            for (String key : set){
                contact.put(key,s.getAsString(key));
            }

            //for rawContact
            ArrayList<RawContact> rawContacts = c.getRawContacts();
            for (RawContact rawContact : rawContacts){
                Raw raw = new Raw(rawContact);
                raws.add(raw);
            }

        }


        public class Raw{
            Map<String,String> raw;
            Map<String,ArrayList<Item>> items;

            public Raw(RawContact r){
                raw = new HashMap<>();
                items = new HashMap<>();


                //for rawContact
                ContentValues s = r.getValues();
                Set<String> set = s.keySet();
                for (String key : set){
                    raw.put(key,s.getAsString(key));
                }


                //for dataItem
                ArrayList<DataItem> groupsItems = r.getGroupsItem();
                for(DataItem dataItem : groupsItems){
                	 Item item = new Item();
                     item.addItem("_id",dataItem.getId()+"");
                	 GroupMembershipDataItem group = (GroupMembershipDataItem) dataItem;
                     item.addItem("id",group.getGroupRowId() + "");
                     item.addItem("source",group.getGroupSourceId() + "");
                     addItem("group", item);
				}
                
                
                ArrayList<DataItem> emailsItems = r.getEmailsItem();
                for(DataItem dataItem : emailsItems){
                	 Item item = new Item();
                     item.addItem("_id",dataItem.getId()+"");
                     EmailDataItem email = (EmailDataItem) dataItem;
                     item.addItem("address",email.getAddress());
                     item.addItem("name",email.getDisplayName());
                     item.addItem("label",email.getLabel());
                     addItem("email",item);
				}
                
                ArrayList<DataItem> phoneItems = r.getPhonesItem();
                for(DataItem dataItem : phoneItems){
                	 Item item = new Item();
                     item.addItem("_id",dataItem.getId()+"");
                     PhoneDataItem phone = (PhoneDataItem) dataItem;
                     item.addItem("number",phone.getNumber());
                     item.addItem("label",phone.getLabel());
                     item.addItem("format",phone.getFormattedPhoneNumber());
                     addItem("phone", item);
				}
                
                
                
                
                /*for(DataItem dataItem : dataItems){
                    Item item = new Item();
                    item.addItem("_id",dataItem.getId()+"");
                    if(dataItem.getClass() == EmailDataItem.class){
                        EmailDataItem email = (EmailDataItem) dataItem;
                        item.addItem("address",email.getAddress());
                        item.addItem("name",email.getDisplayName());
                        item.addItem("label",email.getLabel());
                        addItem("email",item);
                    }else if(dataItem.getClass() == EventDataItem.class){
                        EventDataItem event = (EventDataItem) dataItem;
                        item.addItem("date",event.getStartDate());
                        item.addItem("label",event.getLabel());
                        addItem("event", item);
                    }else if(dataItem.getClass() == GroupMembershipDataItem.class){
                        GroupMembershipDataItem group = (GroupMembershipDataItem) dataItem;
                        item.addItem("id",group.getGroupRowId() + "");
                        item.addItem("source",group.getGroupSourceId() + "");
                        addItem("group", item);
                    }else if(dataItem.getClass() == NoteDataItem.class){
                        NoteDataItem note = (NoteDataItem) dataItem;
                        item.addItem("note",note.getNote());
                        addItem("note", item);
                    }else if(dataItem.getClass() == PhoneDataItem.class){
                        PhoneDataItem phone = (PhoneDataItem) dataItem;
                        item.addItem("number",phone.getNumber());
                        item.addItem("label",phone.getLabel());
                        item.addItem("format",phone.getFormattedPhoneNumber());
                        addItem("phone", item);
                    }else if(dataItem.getClass() == PhotoDataItem.class){
                        PhotoDataItem photo = (PhotoDataItem) dataItem;
                        item.addItem("id",photo.getPhotoFileId() + "");
                        addItem("photo", item);
                    }else if(dataItem.getClass() == WebsiteDataItem.class){
                        WebsiteDataItem website = (WebsiteDataItem) dataItem;
                        item.addItem("label",website.getLabel());
                        item.addItem("url",website.getUrl());
                        addItem("website", item);
                    }else if(dataItem.getClass() == DataItem.class){

                        //addItem("data",item);
                    }
                    //else if (dataItem.getClass() == IdentityDataItem.class || dataItem.getClass() == ImDataItem.class || dataItem.getClass() == NicknameDataItem.class || dataItem.getClass() == OrganizationDataItem.class || dataItem.getClass() == RelationDataItem.class)
                }*/
            }

            public void addItem(String key,Item item){
                ArrayList<Item> i = items.get(key);
                if( i == null){
                    i = new ArrayList<>();
                    items.put(key,i);
                }
                i.add(item);
            }
        }


        public class Item{
            Map<String,String> item;

            public Item(){
                item = new HashMap<>();
            }

            public void addItem(String key,String value){
                item.put(key,value);
            }
        }


    }


    /*private class Ans{
        Map<String,String> contact;
        Map<String,Raw> raws;


        public Ans(Contact c){
            contact = new HashMap<>();
            raws = new HashMap<>();




            //for contact
            ContentValues s = c.getValues();
            Set<String> set = s.keySet();
            for (String key : set){
                contact.put(key,s.getAsString(key));
            }

            //for rawContact
            ArrayList<RawContact> rawContacts = c.getRawContacts();
            for (RawContact rawContact : rawContacts){
                Raw raw = new Raw(rawContact);
                raws.put(rawContact.getAccountName(),raw);
            }

        }

        public class Raw{
            Map<String,String> raw;
            Map<String,Item> items;

            ArrayList<Item> other;


            public Raw(RawContact r){
                raw = new HashMap<>();
                items = new HashMap<>();
                other = new ArrayList<>();


                //for rawContact
                ContentValues s = r.getValues();
                Set<String> set = s.keySet();
                for (String key : set){
                    raw.put(key,s.getAsString(key));
                }

                //for dataItem
                ArrayList<DataItem> dataItems = r.getDataItems();
                for (DataItem dataItem : dataItems){
                    Item item = new Item(dataItem);
                    String key = dataItem.getContentValues().getAsString("_type");
                    if(key != null && !key.equals("null")){
                        items.put(dataItem.getContentValues().getAsString("_type"),item);
                    }else{
                        other.add(item);
                    }

                }
            }

            public class Item{
                Map<String,String> item;

                public Item(DataItem d){
                    item = new HashMap<>();

                    //for dataItem
                    ContentValues s = d.getContentValues();
                    Set<String> set = s.keySet();
                    for (String key : set){
                        item.put(key,s.getAsString(key));
                    }
                }
            }
        }

    }*/

    private ArrayList<DataItem> loadDataItemsList(Cursor cursor){
        ArrayList<DataItem> dataItems = new ArrayList<DataItem>();
        if(cursor != null){
            while (cursor.moveToNext()){
                DataItem item = DataItem.createFrom(ContactUtil.cursorToContentValues(cursor));
                dataItems.add(item);
            }
            cursor.close();
        }

        return dataItems;
    }


    private ArrayList<RawContact> loadRawContactsList(Cursor cursor) {
        ArrayList<RawContact> rawContacts = new ArrayList<>();
        if(cursor != null){
            while (cursor.moveToNext()){
                RawContact raw = new RawContact(ContactUtil.cursorToContentValues(cursor));
                rawContacts.add(raw);
            }
            cursor.close();
        }
        return rawContacts;
    }
}
