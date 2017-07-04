package com.shubham.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.text.TextUtils;

import com.app.TaskAppDownload;
import com.app.TaskAppIcon;
import com.contact.TaskContactPhoto;
import com.file.Storage;
import com.file.StorageType;
import com.file.StorageUtils;
import com.file.TaskDownlodFile;
import com.file.asset.AssertUtils;
import com.gallery.TaskGalleryPhoto;
import com.gallery.TaskGalleryThumb;
import com.music.TaskAlbumCover;
import com.music.TaskSong;
import com.shubham.server.main.WSMain;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.NanoWSD;

public class NANOServer extends NanoWSD{

	public static AssertUtils assertUtils;
	public static boolean DEBUG = true;
	public Context context;
	

	public Map<String, HTTPBaseTask> httpTaskList;
	
	public ArrayList<File> SDCARD_ROOT_DIRS = new ArrayList<File>();
	@SuppressWarnings("serial")
	public static final List<String> INDEX_FILE_NAMES = new ArrayList<String>() {
		{
			add("index.html");
			add("index.htm");
		}
	};


	public NANOServer(Context context,int port) {
		super(port);
		this.context = context;
		assertUtils = new AssertUtils(context);
			
		init();	
		
		
		//http task
		httpTaskList = new HashMap<>();
		
		//app
		registerHttpTask("app-icon", new TaskAppIcon());
		registerHttpTask("app-download", new TaskAppDownload());
		
		//music
		registerHttpTask("album-cover", new TaskAlbumCover());
		registerHttpTask("song", new TaskSong());
		
		//gallery
		registerHttpTask("gallery-thumb", new TaskGalleryThumb());
		registerHttpTask("gallery-photo", new TaskGalleryPhoto());
		
		//contact
		registerHttpTask("contact-photo", new TaskContactPhoto());

		//file
		registerHttpTask("file-download", new TaskDownlodFile());
	}

	public void registerHttpTask(String name,HTTPBaseTask handler){
		httpTaskList.put(name, handler);
	}



	
	@Override
	protected WebSocket openWebSocket(IHTTPSession handshake) {
		switch (handshake.getUri()) {
		case "/main":
			return new WSMain(context,handshake);
		default:
			return new WSDefualt(handshake);
		}
	}

	@Override
	protected Response serveHttp(IHTTPSession session) {
		Method method = session.getMethod();
		Response response = null;



		switch (method){
			case GET:
				response = GET(session,session.getHeaders(),session.getUri());
				break;
			case POST:
				response = POST(session);
				break;
			default:
				break;
		}

		return response == null?newFixedLengthResponse(Status.OK,"text/plain", "Nothing to show: security purpose") : response;
	}


	public Response POST(IHTTPSession session){
		return null;
	}


	public Response GET(IHTTPSession session,Map<String,String> headers,String uri){
		Map<String, String> parms = session.getParms();
		String task = parms.get("task");
		
		if(!TextUtils.isEmpty(task)){
			HTTPBaseTask handler = httpTaskList.get(task);
			if(handler != null){
				try {
					Response response = httpTaskList.get(task).work(parms);
					if(response != null)
						return response;					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return newFixedLengthResponse(Status.OK,"text/plain", "Nothing to show: security purpose : action");
		}
		return serveWebSite(session, headers, uri);
	}





	public Response serveWebSite(IHTTPSession session,Map<String,String> headers,String uri){
		//First priority to rootDir
		for(File homeDir : SDCARD_ROOT_DIRS){
			// Make sure we won't die of an exception later
			if (!homeDir.isDirectory()) {
				return getInternalErrorResponse("given path is not a directory (" + homeDir + ").");
			}
		}

		// Remove URL arguments
		uri = uri.trim().replace(File.separatorChar, '/');
		if (uri.indexOf('?') >= 0) {
			uri = uri.substring(0, uri.indexOf('?'));
		}

		// Prohibit getting out of current directory
		if (uri.contains("../")) {
			return getForbiddenResponse("Won't serve ../ for security reasons.");
		}

		//check file available for server
		boolean canServeSdCardDirs = false;
		boolean canServeAssertDirs = false;
		File homeDirSdCard = null;


		//for sdcard
		for (int i = 0; !canServeSdCardDirs && i < this.SDCARD_ROOT_DIRS.size(); i++) {
			homeDirSdCard = this.SDCARD_ROOT_DIRS.get(i);
			canServeSdCardDirs = canServeUri(uri, homeDirSdCard);
		}

		//for assert if sdcard not exist to serve file
		if(!canServeSdCardDirs){
			if(uri.equals("/"))
				canServeAssertDirs = assertUtils.isFileWebExist("");
			else
				canServeAssertDirs = assertUtils.isFileWebExist(uri);

		}




		if(!canServeAssertDirs && !canServeSdCardDirs){
			return getNotFoundResponse();
		}

		if(canServeSdCardDirs){
			File f = new File(homeDirSdCard, uri);
			if (f.isDirectory() && !uri.endsWith("/")) {
				uri += "/";
				Response res = newFixedLengthResponse(Response.Status.REDIRECT, NanoHTTPD.MIME_HTML, "<html><body>Redirected: <a href=\"" + uri + "\">" + uri + "</a></body></html>");
				res.addHeader("Location", uri);
				return res;
			}

			if (f.isDirectory()) {
				// First look for index files (index.html, index.htm, etc) and if
				// none found, list the directory if readable.
				
				
				String indexFile = findIndexFileInDirectory(f);
				if (indexFile != null) {
					return serveWebSite(session,headers,uri + indexFile);
				}

				return newFixedLengthResponse("directory listing denied.:sdcard");
			}

			String mimeTypeForFile = getMimeTypeForFile(uri);
			Response response = serveFile(uri, headers, f, mimeTypeForFile);
			return response != null ? response : getNotFoundResponse();
		}

		if(canServeAssertDirs){
			if(assertUtils.isWebAssetDirectory(uri) && !uri.endsWith("/")){
				uri += "/";
				Response res = newFixedLengthResponse(Response.Status.REDIRECT, NanoHTTPD.MIME_HTML, "<html><body>Redirected: <a href=\"" + uri + "\">" + uri + "</a></body></html>");
				res.addHeader("Location", uri);
				return res;
			}

			if(assertUtils.isWebAssetDirectory(uri)){
				String indexFile = findIndexFileInAssert(uri);
				if (indexFile != null) {
					return serveWebSite(session, headers, uri + indexFile);
				}
				return newFixedLengthResponse("directory listing denied.:asset");
			}


			String mimeTypeForFile = getMimeTypeForFile(uri);
			InputStream inputStream = assertUtils.getFileWebInputStream(uri);
			if(inputStream != null){
				return serveAssert(uri, headers, inputStream, mimeTypeForFile);
			}
			return newFixedLengthResponse("File reading errore");
		}




		return  null;
	}

	public boolean isFileFromPath(String path) {
		if (path == null)
			return false;
		if (path.length() == 2)
			return false;

		if (path.charAt(path.length() - 1) == '/') {
			return false;
		}

		int dotIndex = path.lastIndexOf(".");
		int slashIndex = path.indexOf("/");

		return dotIndex > 0 && slashIndex < dotIndex;
	}

	Response serveAssert(String uri, Map<String, String> header, InputStream inputStream, String mime) {
		Response res;
		try {
			// Calculate etag
			String etag = null;
			if(DEBUG){
				etag = Integer.toHexString((uri + System.currentTimeMillis() +inputStream.available()).hashCode());
			}else{
				etag = Integer.toHexString((uri + inputStream.available()).hashCode());
			}



			// Support (simple) skipping:
			long startFrom = 0;
			long endAt = -1;
			String range = header.get("range");
			if (range != null) {
				if (range.startsWith("bytes=")) {
					range = range.substring("bytes=".length());
					int minus = range.indexOf('-');
					try {
						if (minus > 0) {
							startFrom = Long.parseLong(range.substring(0, minus));
							endAt = Long.parseLong(range.substring(minus + 1));
						}
					} catch (NumberFormatException ignored) {
					}
				}
			}

			// get if-range header. If present, it must match etag or else we
			// should ignore the range request
			String ifRange = header.get("if-range");
			boolean headerIfRangeMissingOrMatching = (ifRange == null || etag.equals(ifRange));

			String ifNoneMatch = header.get("if-none-match");
			boolean headerIfNoneMatchPresentAndMatching = ifNoneMatch != null && ("*".equals(ifNoneMatch) || ifNoneMatch.equals(etag));

			// Change return code and add Content-Range header when skipping is
			// requested
			long fileLen = inputStream.available();

			if (headerIfRangeMissingOrMatching && range != null && startFrom >= 0 && startFrom < fileLen) {
				// range request that matches current etag
				// and the startFrom of the range is satisfiable
				if (headerIfNoneMatchPresentAndMatching) {
					// range request that matches current etag
					// and the startFrom of the range is satisfiable
					// would return range from file
					// respond with not-modified
					res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
					res.addHeader("ETag", etag);
				} else {
					if (endAt < 0) {
						endAt = fileLen - 1;
					}
					long newLen = endAt - startFrom + 1;
					if (newLen < 0) {
						newLen = 0;
					}

					//FileInputStream fis = new FileInputStream(file);
					inputStream.skip(startFrom);

					res = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, mime, inputStream, newLen);
					res.addHeader("Accept-Ranges", "bytes");
					res.addHeader("Content-Length", "" + newLen);
					res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
					res.addHeader("ETag", etag);
				}
			} else {

				if (headerIfRangeMissingOrMatching && range != null && startFrom >= fileLen) {
					// return the size of the file
					// 4xx responses are not trumped by if-none-match
					res = newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE, NanoHTTPD.MIME_PLAINTEXT, "");
					res.addHeader("Content-Range", "bytes */" + fileLen);
					res.addHeader("ETag", etag);
				} else if (range == null && headerIfNoneMatchPresentAndMatching) {
					// full-file-fetch request
					// would return entire file
					// respond with not-modified
					res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
					res.addHeader("ETag", etag);
				} else if (!headerIfRangeMissingOrMatching && headerIfNoneMatchPresentAndMatching) {
					// range request that doesn't match current etag
					// would return entire (different) file
					// respond with not-modified

					res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
					res.addHeader("ETag", etag);
				} else {
					// supply the file
					res = newFixedFileResponse(inputStream, mime);
					res.addHeader("Content-Length", "" + fileLen);
					res.addHeader("ETag", etag);
				}
			}
		} catch (IOException ioe) {
			res = getForbiddenResponse("Reading file failed.");
		}

		return res;
	}


	Response serveFile(String uri, Map<String, String> header, File file, String mime) {
		Response res;
		try {
			// Calculate etag
			String etag = Integer.toHexString((file.getAbsolutePath() + file.lastModified() + "" + file.length()).hashCode());

			// Support (simple) skipping:
			long startFrom = 0;
			long endAt = -1;
			String range = header.get("range");
			if (range != null) {
				if (range.startsWith("bytes=")) {
					range = range.substring("bytes=".length());
					int minus = range.indexOf('-');
					try {
						if (minus > 0) {
							startFrom = Long.parseLong(range.substring(0, minus));
							endAt = Long.parseLong(range.substring(minus + 1));
						}
					} catch (NumberFormatException ignored) {
					}
				}
			}

			// get if-range header. If present, it must match etag or else we
			// should ignore the range request
			String ifRange = header.get("if-range");
			boolean headerIfRangeMissingOrMatching = (ifRange == null || etag.equals(ifRange));

			String ifNoneMatch = header.get("if-none-match");
			boolean headerIfNoneMatchPresentAndMatching = ifNoneMatch != null && ("*".equals(ifNoneMatch) || ifNoneMatch.equals(etag));

			// Change return code and add Content-Range header when skipping is
			// requested
			long fileLen = file.length();

			if (headerIfRangeMissingOrMatching && range != null && startFrom >= 0 && startFrom < fileLen) {
				// range request that matches current etag
				// and the startFrom of the range is satisfiable
				if (headerIfNoneMatchPresentAndMatching) {
					// range request that matches current etag
					// and the startFrom of the range is satisfiable
					// would return range from file
					// respond with not-modified
					res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
					res.addHeader("ETag", etag);
				} else {
					if (endAt < 0) {
						endAt = fileLen - 1;
					}
					long newLen = endAt - startFrom + 1;
					if (newLen < 0) {
						newLen = 0;
					}

					FileInputStream fis = new FileInputStream(file);
					fis.skip(startFrom);

					res = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, mime, fis, newLen);
					res.addHeader("Accept-Ranges", "bytes");
					res.addHeader("Content-Length", "" + newLen);
					res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
					res.addHeader("ETag", etag);
				}
			} else {

				if (headerIfRangeMissingOrMatching && range != null && startFrom >= fileLen) {
					// return the size of the file
					// 4xx responses are not trumped by if-none-match
					res = newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE, NanoHTTPD.MIME_PLAINTEXT, "");
					res.addHeader("Content-Range", "bytes */" + fileLen);
					res.addHeader("ETag", etag);
				} else if (range == null && headerIfNoneMatchPresentAndMatching) {
					// full-file-fetch request
					// would return entire file
					// respond with not-modified
					res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
					res.addHeader("ETag", etag);
				} else if (!headerIfRangeMissingOrMatching && headerIfNoneMatchPresentAndMatching) {
					// range request that doesn't match current etag
					// would return entire (different) file
					// respond with not-modified

					res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
					res.addHeader("ETag", etag);
				} else {
					// supply the file
					res = newFixedFileResponse(file, mime);
					res.addHeader("Content-Length", "" + fileLen);
					res.addHeader("ETag", etag);
				}
			}
		} catch (IOException ioe) {
			res = getForbiddenResponse("Reading file failed.");
		}

		return res;
	}




	private boolean canServeUri(String uri, File homeDir) {
		boolean canServeUri;
		File f = new File(homeDir, uri);
		canServeUri = f.exists();
		return canServeUri;
	}

	private String findIndexFileInAssert(String uri) {
		for (String fileName : NANOServer.INDEX_FILE_NAMES) {
			if(assertUtils.isFileWebExist(uri + fileName)){
				return fileName;
			}
		}
		return null;
	}

	private String findIndexFileInDirectory(File directory) {
		for (String fileName : NANOServer.INDEX_FILE_NAMES) {
			File indexFile = new File(directory, fileName);
			
			if (indexFile.isFile()) {
				return fileName;
			}
		}
		return null;
	}


	private Response newFixedFileResponse(File file, String mime)throws FileNotFoundException {
		Response res;
		res = newFixedLengthResponse(Response.Status.OK, mime,new FileInputStream(file), (int) file.length());
		res.addHeader("Accept-Ranges", "bytes");
		return res;
	}

	private Response newFixedFileResponse(InputStream file, String mime) throws IOException {
		Response res;
		res = newFixedLengthResponse(Response.Status.OK, mime,file,file.available());
		res.addHeader("Accept-Ranges", "bytes");
		return res;
	}



	protected Response getInternalErrorResponse(String s) {
		return newFixedLengthResponse(Response.Status.INTERNAL_ERROR,
				NanoHTTPD.MIME_PLAINTEXT, "INTERNAL ERROR: " + s);
	}

	protected Response getForbiddenResponse(String s) {
		return newFixedLengthResponse(Response.Status.FORBIDDEN,
				NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: " + s);
	}

	protected Response getNotFoundResponse() {
		return newFixedLengthResponse(Response.Status.NOT_FOUND,
				NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
	}


	
	
	public void init(){
		Map<Integer,Storage> storages = StorageUtils.getStorageList(context,false);
		Set<Integer> keys = storages.keySet();
		for (Integer key : keys){
			Storage storage = storages.get(key);
			if(storage.getStorageType() == StorageType.PRIMARY){
				File file = new File(storage.buildPath("/flysync"));
				if(file.exists() && file.isDirectory()){
					SDCARD_ROOT_DIRS.add(file);
				}

				break;
			}
		}
		
		for(File f : SDCARD_ROOT_DIRS){
			//App.log(f.getAbsolutePath());
		}
		
		
	}
}
