package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.il.appshortcut.R;
import com.il.appshortcut.services.AirplaneModeService;
import com.il.appshortcut.services.BluetoothService;
import com.il.appshortcut.services.GPSService;
import com.il.appshortcut.services.ServiceVo;
import com.il.appshortcut.services.SoundOffService;
import com.il.appshortcut.services.WifiService;

public class ServicesHelper {
	private List<ServiceVo> serviceList;
	
	public ServicesHelper(Context context){
		serviceList = new ArrayList<ServiceVo>();
		
		Drawable icon = context.getResources().getDrawable(R.drawable.navigation_accept);
		
		AirplaneModeService airplaneModeService = new AirplaneModeService();
		airplaneModeService.setName("airplaneModeService");
		airplaneModeService.setDescription("airplaneModeService");
		airplaneModeService.setSelected(false);
		airplaneModeService.setIcon(icon);
		serviceList.add(airplaneModeService);
		
		WifiService wifi = new WifiService();
		wifi.setName("wifi");
		wifi.setDescription("wifi");
		wifi.setSelected(false);
		wifi.setIcon(icon);
		serviceList.add(wifi);
		
		BluetoothService bluetoothService = new BluetoothService();
		bluetoothService.setName("bluetoothService");
		bluetoothService.setDescription("bluetoothService");
		bluetoothService.setSelected(false);
		bluetoothService.setIcon(icon);
		serviceList.add(bluetoothService);
		
		GPSService gpsService = new GPSService();
		gpsService.setName("gpsService");
		gpsService.setDescription("gpsService");
		gpsService.setSelected(false);
		gpsService.setIcon(icon);
		serviceList.add(gpsService);
		
		SoundOffService soundOffService = new SoundOffService();
		soundOffService.setName("soundOffService");
		soundOffService.setDescription("soundOffService");
		soundOffService.setSelected(false);
		soundOffService.setIcon(icon);
		serviceList.add(soundOffService);
		
	}

	public List<ServiceVo> getServiceList() {
		return serviceList;
	}
	
	public List<ServiceVo> getServiceList(List<ServiceVo> list) {
		if (list != null 
				&& list.size() > 0){
			
			for (ServiceVo item : list){
				
				for (int i = 0; i < serviceList.size(); i++){
					if (serviceList.get(i).equals(item)){
						serviceList.get(i).setSelected(true);
					}
				}
				
			}
		}
		return serviceList;
	}
	
	public ServiceVo getServiceById(int id){
		for (ServiceVo item : serviceList){
			if (item.ID == id){
				return item;
			}
		}
		return null;
	}

	public String[] getServicesAsStringArray(){
		String[] result = new String[serviceList.size()];
		int i = 0;
		for (ServiceVo service : serviceList){
			result[i] = service.getName();
			i++;
		}
		return result;
	}
	
	
}
