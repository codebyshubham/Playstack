package com.shubham.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

public class HTTPResponeUtil {
	
	
	public static Response newFixedFileResponse(File file, String mime)throws FileNotFoundException {
		Response res;
		res = NanoHTTPD.newFixedLengthResponse(Response.Status.OK, mime,new FileInputStream(file), (int) file.length());
		res.addHeader("Accept-Ranges", "bytes");
		return res;
	}

	public static Response newFixedFileResponse(InputStream file, String mime) throws IOException {
		Response res;
		res = NanoHTTPD.newFixedLengthResponse(Response.Status.OK, mime,file,file.available());
		res.addHeader("Accept-Ranges", "bytes");
		return res;
	}


	public static Response getInternalErrorResponse(String s) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR,
				NanoHTTPD.MIME_PLAINTEXT, "INTERNAL ERROR: " + s);
	}

	public static Response getForbiddenResponse(String s) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN,
				NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: " + s);
	}

	public static Response getNotFoundResponse() {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND,
				NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
	}


}
