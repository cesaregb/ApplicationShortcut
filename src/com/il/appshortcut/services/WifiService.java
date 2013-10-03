package com.il.appshortcut.services;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiService extends ServiceVo {
	public WifiService(){
		ID = 6;
	}
	
	@Override
	public boolean run(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wifi.isWifiEnabled()){
			//TODO enable and disable are you fucking sure???? 
//			wifi.setWifiEnabled(false);
		}else{
			wifi.setWifiEnabled(true);
		}
		return true;
	}
	
}
