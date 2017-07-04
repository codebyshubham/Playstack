package com.file.asset;

import java.io.IOException;
import java.util.HashMap;

import android.content.res.AssetManager;

public class FileAssetUtil {
    private static HashMap<FileAssetKey,FileAsset> list = new HashMap<>();

    public static FileAsset put(FileAssetKey key, FileAsset fileAssert){
        return list.put(key, fileAssert);
    }

    private static FileAsset get(FileAssetKey key){
        return list.get(key);
    }

    private static FileAsset get(String key){
        return get(new FileAssetKey(key));
    }


    public static boolean isAssetFile(AssetManager assetManager,String key){
        return isAssetFile(assetManager, new FileAssetKey(key));
    }


    public static boolean isAssetFile(AssetManager assetManager,FileAssetKey key){
        FileAsset fileAsset = get(key);
        if(fileAsset == null){
            fileAsset = new FileAsset(key.getValue());
            put(key,fileAsset);
        }

        if(!fileAsset.isFileServe){
            fileAsset.isFileServe = true;
            if(!fileAsset.isDirectory){
                try {
                    assetManager.open(fileAsset.path);
                    fileAsset.isFile = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileAsset.isFile;
    }

    public static boolean isAssetDirectory(AssetManager assetManager,String key) {

        return isAssetDirectory(assetManager,new FileAssetKey(key));
    }
    private static String removeLastChar(String str) {
        if (str.length() > 0 && str.charAt(str.length()-1)=='/') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    private static boolean isAssetDirectory(AssetManager assetManager,FileAssetKey key){
        FileAsset fileAsset = get(key);
        if(fileAsset == null){
            fileAsset = new FileAsset(key.getValue());
            put(key,fileAsset);
        }

        if(!fileAsset.isDirectoryServe){
            fileAsset.isDirectoryServe = true;
            if(!fileAsset.isFile){
                try {
                    fileAsset.isDirectory = assetManager.list(removeLastChar(fileAsset.path)).length > 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileAsset.isDirectory;
    }


    public static boolean isAssetExits(AssetManager assetManager,String key){
        return  isAssetExits(assetManager, new FileAssetKey(key));
    }

    public static boolean isAssetExits(AssetManager assetManager,FileAssetKey key){
        return isAssetFile(assetManager,key) || isAssetDirectory(assetManager,key);
    }

}