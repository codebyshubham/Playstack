package com.file;

import java.io.File;

import android.content.Context;
import android.os.StatFs;
import android.text.format.Formatter;

public class Storage {

	private Context context;

	// always start with path /
	private StorageType type;
	private String root;
	private File file;
	private StatFs statFs;

	public Storage(Context context, String root) {
		this.context = context;
		this.root = root;
		statFs = new StatFs(root);
	}

	public void setStorageType(StorageType type) {
		this.type = type;
	}

	public StorageType getStorageType() {
		return type;
	}

	public String getRootPath() {
		return root;
	}

	public void setRootPath(String root) {
		this.root = root;
		this.file = null;
	}

	public File getRootFile() {
		if (file == null) {
			file = new File(root);
		}
		return file;
	}

	public String getTitle() {
		if (type == StorageType.PRIMARY) {
			return "Storage";
		}
		return "External Storage";
	}

	public boolean isWritable() {
		return FileUtils.isWritableNormalOrSaf(context, getRootFile());
	}

	public String buildPath(String path) {
		return getRootPath() + path;
	}

	public File buildFile(String path) {
		return new File(buildPath(path));
	}

	public String[] listOfFile(String path) {
		File file = buildFile(path);

		if (file.exists() && file.isDirectory()) {
			return file.list();
		}

		return null;
	}

	public String getGBFree() {
		long sdAvailSize = (long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
		return Formatter.formatFileSize(context, sdAvailSize);
	}
	
	public String getGBTotal(){
		long sdTotalSize = (long) statFs.getBlockCount() * (long) statFs.getBlockSize();
		return Formatter.formatFileSize(context, sdTotalSize);
	}
	
	public String getGBUsed(){
		long sdTotalSize = ((long) statFs.getBlockCount() - (long) statFs.getAvailableBlocks()) * (long) statFs.getBlockSize();
		return Formatter.formatFileSize(context, sdTotalSize);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Storage))
			return false;

		Storage s = (Storage) obj;
		return s.root.equals(root);
	}
}
