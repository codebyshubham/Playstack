package com.temp;

import android.util.Log;

public class App {
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static void log(String msg){
		Log.i("My", msg);
	}
	
	public static void log(int msg){
		log(msg +"");
	}
}
