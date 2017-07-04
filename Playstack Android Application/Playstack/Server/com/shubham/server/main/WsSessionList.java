package com.shubham.server.main;

import java.util.HashMap;
import java.util.Map;

public class WsSessionList {
	
	public static Map<String, String> sessions = new HashMap<String, String>();
	
	public static void addSession(String ip,String key){
		sessions.put(ip, key);
	}
	
	public static void removeSession(String ip){
		sessions.remove(ip);
	}
	
	public static boolean isExits(String ip,String key){
		return sessions.get(ip) != null;
	}
	
}
