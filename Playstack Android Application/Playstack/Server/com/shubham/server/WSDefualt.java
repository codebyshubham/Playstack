package com.shubham.server;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoWSD.WebSocket;
import fi.iki.elonen.NanoWSD.WebSocketFrame;
import fi.iki.elonen.NanoWSD.WebSocketFrame.CloseCode;

public class WSDefualt extends WebSocket{

	public WSDefualt(IHTTPSession handshakeRequest) {
		super(handshakeRequest);
	}

	@Override
	protected void onOpen() {
		
	}

	@Override
	protected void onClose(CloseCode code, String reason, boolean initiatedByRemote) {
		
	}

	@Override
	protected void onMessage(WebSocketFrame message) {
		
	}

	@Override
	protected void onPong(WebSocketFrame pong) {
	
	}

	@Override
	protected void onException(IOException exception) {
		
	}

}
