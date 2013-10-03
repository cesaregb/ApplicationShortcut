package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class ApplicationHelper {
	class PInfo {
	    private String appname = "";
	    private String pname = "";
	    private String versionName = "";
	    private int versionCode = 0;
	    private Drawable icon = null;
	    
		public String getAppname() {
			return appname;
		}
		public void setAppname(String appname) {
			this.appname = appname;
		}
		public String getPname() {
			return pname;
		}
		public void setPname(String pname) {
			this.pname = pname;
		}
		public String getVersionName() {
			return versionName;
		}
		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}
		public int getVersionCode() {
			return versionCode;
		}
		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}
		public Drawable getIcon() {
			return icon;
		}
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}
	}

	public ArrayList<PInfo> getPackages(PackageManager pm) {
	    ArrayList<PInfo> apps = getInstalledApps(pm, false); /* false = no system packages */
	    final int max = apps.size();
	    for (int i=0; i<max; i++) {
	    }
	    return apps;
	}

	public ArrayList<PInfo> getInstalledApps(PackageManager pm, boolean getSysPackages) {
	    ArrayList<PInfo> res = new ArrayList<PInfo>();        
	    List<PackageInfo> packs = pm.getInstalledPackages(0);
	    for(int i=0;i<packs.size();i++) {
	        PackageInfo p = packs.get(i);
	        if ((!getSysPackages) && (p.versionName == null)) {
	            continue ;
	        }
	        PInfo newInfo = new PInfo();
	        newInfo.appname = p.applicationInfo.loadLabel(pm).toString();
	        newInfo.pname = p.packageName;
	        newInfo.versionName = p.versionName;
	        newInfo.versionCode = p.versionCode;
	        newInfo.icon = p.applicationInfo.loadIcon(pm);
	        res.add(newInfo);
	    }
	    return res; 
	}
	
	public static int safeLongToInt(long l) {
	    int i = (int)l;
	    if ((long)i != l) {
	        throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
	    }
	    return i;
	}
}
