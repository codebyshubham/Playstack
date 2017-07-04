package com.sms;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskSmsThread implements WSBaseTask{

	private Context context = MyService.context;
	
	@Override
	public Object work(String[] args) {
		ContentResolver resolver = context.getContentResolver();
        Ans ans = new Ans();

        String _ID = args[0].toString();

        Uri uriSms = Uri.parse("content://sms/");
        Cursor cursorSms = resolver.query(
                uriSms,
                new String[]{
                        "_id",
                        "address",
                        "date",
                        "body",
                        "type"
                },
                "thread_id =?",
                new String[]{
                        _ID
                },
                "date ASC"
        );
        if(cursorSms != null){
            final int indexID = 0;
            final int indexADDRESS = 1;
            final int indexDATE = 2;
            final int indexBODY = 3;
            final int indexTYPE = 4;


            String id,address,date,body,type;
            while (cursorSms.moveToNext()){
                id = cursorSms.getString(indexID);
                address = cursorSms.getString(indexADDRESS);
                date = cursorSms.getString(indexDATE);
                body = cursorSms.getString(indexBODY);
                type = cursorSms.getString(indexTYPE);

                Ans.Sms sms = ans.new Sms();
                sms.id = id;
                sms.address = address;
                sms.date = date;
                sms.body = body;
                sms.you = type.equals("1");
                ans.sms.add(sms);
            }

            cursorSms.close();
        }
		return ans;
	}
	
	private class Ans{
        ArrayList<Sms> sms;

        public Ans(){
            sms = new ArrayList<>();
        }

        @SuppressWarnings("unused")
        public class Sms{
			String id;
            String address;
            String date;
            String body;
            boolean you;
        }
    }
}
