package com.shubham.server.main;

import java.io.IOException;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shubham.playstack.DataBase;
import com.shubham.server.bean.MsgBean;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoWSD.WebSocket;
import fi.iki.elonen.NanoWSD.WebSocketFrame;
import fi.iki.elonen.NanoWSD.WebSocketFrame.CloseCode;

public class WSMain extends WebSocket{

	public static WSTaskManger taskManger;
	private DataBase dataBase;
	private Gson gson;
	private boolean isLogin;
	
	
	//for session
	private String session;
	private String ip;
	
	
	public WSMain(Context context,IHTTPSession handshakeRequest) {
		super(handshakeRequest);
		
		
		ip = handshakeRequest.getHeaders().get("host");
		session = ip;
		
	
		dataBase = new DataBase(context);
		gson = new GsonBuilder().setPrettyPrinting().create();
		
		
		if(taskManger == null){
			taskManger = new WSTaskManger(context);
		}
	}

	@Override
	protected void onOpen() {
		
	}

	@Override
	protected void onClose(CloseCode code, String reason, boolean initiatedByRemote) {
		
	}

	@Override
	protected void onMessage(WebSocketFrame message) {
		MsgBean msg = gson.fromJson(message.getTextPayload(), MsgBean.class);
		if(isLogin){
			switch (msg.type) {
			case "task":
					taskManger.excuteTask(this, msg);
				break;
			default:
				break;
			}
			
		}else{
			WSData data = new WSData();
			data.count = msg.count;
			
			if(msg.name.equals("login")){
				String username = msg.args.get(0);
				String password = msg.args.get(1);
				if(username.equals(dataBase.getString("username")) && password.equals(dataBase.getString("password"))){
					isLogin = true;
					
				}
				data.data = new Login(isLogin, session);
			}else{
				data.data = new Login(isLogin, session);
			}	
			try {
				this.send(gson.toJson(data));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onPong(WebSocketFrame pong) {
	
	}

	@Override
	protected void onException(IOException exception) {
		
	}

	@SuppressWarnings("unused")
	private class Login{
		boolean isLogin;
		String session;
		
		public Login(boolean islogin,String key){
			this.isLogin = islogin;
			this.session = key;
					
		}
	}
	

}
