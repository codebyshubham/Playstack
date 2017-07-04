package com.file.asset;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class AssertUtils {
	public Context context;
	public AssetManager manager;
	
	private String ROOT_DIR = "";
    private String ROOT_WEB_DIR = ROOT_DIR +"www";
    
    public AssertUtils(Context context) {
		this.context = context;
		manager = context.getAssets();
	}
    
    public void setRootDir(String dir) {
    	ROOT_DIR = dir;
	}
    
    public  String buildPath(String path){
        if(path != null && path.length() > 0){
            if(path.charAt(0) != '/'){
                return ROOT_DIR + "/" + path;
            }
            return ROOT_DIR + path;
        }else {
            return ROOT_DIR;
        }
    }
    
    public InputStream getFileInputStream(String path){
        try {
            return manager.open(buildPath(path));
        } catch (IOException e) {
            return null;
        }
    }
    
    public boolean isFileExist(String path){
        return FileAssetUtil.isAssetExits(manager,buildPath(path));
    }
    
    public InputStream getFileWebInputStream(String path){
        try {
            return manager.open(buildWebPath(path));
        } catch (IOException e) {
            return null;
        }
    }
    
    public boolean isFileWebExist(String path){
        return FileAssetUtil.isAssetExits(manager,buildWebPath(path));
    }


    public boolean isWebAssetFile(String path){
        return FileAssetUtil.isAssetFile(manager, buildWebPath(path));
    }

    public boolean isWebAssetDirectory(String path){
        return FileAssetUtil.isAssetDirectory(manager,buildWebPath(path));
    }
    
    public String buildWebPath(String path){
        if(path != null && path.length() > 0){
            if(path.charAt(0) != '/'){
                return ROOT_WEB_DIR + "/" + path;
            }
            return ROOT_WEB_DIR + path;
        }else {
            return ROOT_WEB_DIR;
        }
    }
    
}
