package com.spidren.api;

import android.app.Activity;


public class MyConfig {
	public int w, h;
	public Activity context;
	public float dpi;
	public int[] dpix = new int[1001];
	public final int SDK_VERSION = android.os.Build.VERSION.SDK_INT;
	private int id = 1000;

	public MyConfig(Activity baseContext) {
		context = baseContext;
		dpi = baseContext.getResources().getDisplayMetrics().density;
		for (int i = 1; i < 1001; i++) {
			dpix[i] = (int) (dpi * i);
		}
	}
	
	
	public void set(int width,int height){
		w = width;
		h = height;
		
	}
	
	/*public void showMessages(final String msg) {
		Handler handler = new Handler(); 
		
		handler.post(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}*/
	
	/*public void Log(String msg){
		Log.i("My", msg);
	}*/
	
	public int getId(){
		return id++;
	}
	
	/*public void shareLink(String link){
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
	    share.setType("text/plain");
	    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	    share.putExtra(Intent.EXTRA_SUBJECT, "I am spidren");
	    share.putExtra(Intent.EXTRA_TEXT, link);
	    context.startActivity(Intent.createChooser(share, "Share link!"));
		
	}*/
	
	/*public void goToLink(String url){
		
		try {
		    Intent i = new Intent("android.intent.action.MAIN");
		    i.addCategory("android.intent.category.LAUNCHER");
		    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		    i.setClassName("com.android.chrome", "com.android.chrome.Main");
		    i.setData(Uri.parse(url));
		    context.startActivity(i);
		}
		catch(ActivityNotFoundException e) {
		    e.printStackTrace();
		    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		    context.startActivity(i);
		}
	}*/

	/*public void sendMail(String mail,String subject,String body){
		try {
			
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("plain/text");
			sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			String uriText = "mailto:" + Uri.encode(mail) + 
			          "?subject=" + Uri.encode(subject) + 
			          "&body=" + Uri.encode(body);
			Uri uri = Uri.parse(uriText);
			sendIntent.setData(uri);
			sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
			MainActivity.cf.context.startActivity(sendIntent);
			
		} catch (Exception e) {
			Intent send = new Intent(Intent.ACTION_SENDTO);
			String uriText = "mailto:" + Uri.encode(mail) + 
			          "?subject=" + Uri.encode(subject) + 
			          "&body=" + Uri.encode(body);
			Uri uri = Uri.parse(uriText);
			send.setData(uri);
			MainActivity.cf.context.startActivity(send);
		}
	}*/
	
	
	/*public void goFacebook(String faebookid  ,String link){
		Intent facebookIntent = getOpenFacebookIntent(faebookid,link);
		context.startActivity(facebookIntent);
	}
	
	private  Intent getOpenFacebookIntent(String faebookid ,String link) {

	    try {
	        context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
	        return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+faebookid));
	    } catch (Exception e) {
	        return new Intent(Intent.ACTION_VIEW, Uri.parse(link));
	    }
	}*/
}
