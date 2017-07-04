package com.shubham.server;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD.Response;

public interface HTTPBaseTask {
	public Response work(Map<String, String> args);
}
