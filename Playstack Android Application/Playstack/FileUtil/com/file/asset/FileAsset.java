package com.file.asset;

/**
 * Created by Pathik on 12/29/2015.
 */
public class FileAsset {
    public boolean isExits;
    public boolean isFile;
    public boolean isDirectory;
    public boolean isFileServe,isDirectoryServe;
    public final String path;

    public FileAsset(String path){
        this.path = path;
    }

}
