package com.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.annotations.SerializedName;
import com.shubham.server.main.WSBaseTask;
import com.shubham.service.MyService;

public class TaskAppList implements WSBaseTask{
	
	private Context context = MyService.context;

	@Override
	public Object work(String[] args) {
		PackageManager manager = context.getPackageManager();		
		Ans ans = new Ans();
		
		List<PackageInfo> packs = manager.getInstalledPackages(PackageManager.GET_META_DATA);
		
		String sourceDir;
        File apk;
        long size;
        String version;
        String name;
        String packageName;
        int versionCode;
        boolean system;
        
        
        
        
        for (PackageInfo packageInfo : packs){
            sourceDir = packageInfo.applicationInfo.sourceDir;
            apk = new File(sourceDir);//nothing to send
            size = apk.canRead()? apk.length() : 0;
            version = packageInfo.versionName;
            name = packageInfo.applicationInfo.loadLabel(manager).toString();
            packageName = packageInfo.packageName;
            versionCode = packageInfo.versionCode;
            system = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            
            
            
            

            Ans.App app = ans.new App();
            app.dir = sourceDir;
            app.size = size;
            app.version = version;
            app.name = name;
            app.packageName = packageName;
            app.versionCode = versionCode;
            //app.system = system;
            
            if(system){
            	ans.system.add(app);
            }else {				
            	ans.user.add(app);
			}
        }
		return ans;
	}
	
	private class  Ans{
        public ArrayList<App> user;
        public ArrayList<App> system;

        public Ans(){
        	user = new ArrayList<>();
        	system = new ArrayList<>();
        }

        @SuppressWarnings("unused")
        public class App{
			String dir;
            long size;
            String version;
            String name;
            boolean exits;
            @SerializedName("package")
            String packageName;
            int versionCode;
            //boolean system;
        }
    }
}
