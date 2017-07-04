package com.file;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Pathik on 12/27/2015.
 */
public class StorageUtils {

    private static Map<Integer,Storage> list;

    public static Storage getStorageCache(int key){
        return list.get(key);
    }

    public static Map<Integer,Storage> getStorageList(Context context,boolean forceToNew){
        List<Storage> tempList = null;

        if(forceToNew || list == null){
            tempList = getStorageDirectories(context);
        }
        if(tempList != null){
            Map<Integer,Storage> tempMap = new HashMap<>();
            for(int i=0 ; i<tempList.size() ;i++){
                tempMap.put(i,tempList.get(i));
            }
            list = tempMap;
        }
        return list;
    }

    private  static final Pattern DIR_SEPARATOR = Pattern.compile("/");
    private static List<Storage> getStorageDirectories(Context context) {

        final ArrayList<Storage> storages = new ArrayList<>();
        final String externalStorage = System.getenv("EXTERNAL_STORAGE");
        final String secondaryStorage = System.getenv("SECONDARY_STORAGE");
        final String emulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");


        if (TextUtils.isEmpty(emulatedStorageTarget)) {
            Storage storage;
            if (TextUtils.isEmpty(externalStorage)) {
                storage = new Storage(context,"/storage/sdcard0");
            } else {
                storage = new Storage(context,externalStorage);
            }
            storage.setStorageType(StorageType.PRIMARY);
            storages.add(storage);
        } else {
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPARATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }


            Storage storage;
            if (TextUtils.isEmpty(rawUserId)) {
                storage = new Storage(context,emulatedStorageTarget);
            } else {
                storage = new Storage(context,emulatedStorageTarget + File.separator + rawUserId);
            }
            storage.setStorageType(StorageType.PRIMARY);
            storages.add(storage);
        }

        if (!TextUtils.isEmpty(secondaryStorage)) {

            final String[] rawSecondaryStorages = secondaryStorage.split(File.pathSeparator);
            for(String root : rawSecondaryStorages){
                Storage storage = new Storage(context,root);
                storage.setStorageType(StorageType.SECONDARY);
                storages.add(storage);
            }
        }



        Storage usb = getUsbDrive(context);
        if (usb != null && !storages.contains(usb)){
            storages.add(usb);
        }
        return storages;
    }

    public static Storage getUsbDrive(Context context) {
        File parent;
        Storage storage;
        parent = new File("/storage");

        try {
            for (File f : parent.listFiles()) {
                if (f.exists() && f.getName().toLowerCase().contains("usb") && f.canExecute()) {
                    storage = new Storage(context,f.getPath());
                    storage.setStorageType(StorageType.USB);
                    return storage;
                }
            }
        } catch (Exception e) {

        }
        parent = new File("/mnt/sdcard/usbStorage");
        if (parent.exists() && parent.canExecute()){
            storage = new Storage(context,(parent).getPath());
            storage.setStorageType(StorageType.USB);
            return storage;
        }
        parent = new File("/mnt/sdcard/usb_storage");
        if (parent.exists() && parent.canExecute()){
            storage = new Storage(context,(parent).getPath());
            storage.setStorageType(StorageType.USB);
            return storage;
        }
        return null;
    }
}
