package com.il.easyclick.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.il.easyclick.R;
import com.il.easyclick.services.AirplaneModeService;
import com.il.easyclick.services.BluetoothService;
import com.il.easyclick.services.GPSService;
import com.il.easyclick.services.ServiceVo;
import com.il.easyclick.services.SoundOffService;
import com.il.easyclick.services.WifiService;

public class ServicesHelper {
	private List<ServiceVo> serviceList;
	
	public ServicesHelper(Context context){
		serviceList = new ArrayList<ServiceVo>();
		
		Drawable iconAirplane = context.getResources().getDrawable(R.drawable.airplane_on);
		AirplaneModeService airplaneModeService = new AirplaneModeService();
		airplaneModeService.setName("Airplane Service");
		airplaneModeService.setDescription("Airplane Service");
		airplaneModeService.setSelected(false);
		airplaneModeService.setIcon(iconAirplane);
		serviceList.add(airplaneModeService);
		
		Drawable iconWifi = context.getResources().getDrawable(R.drawable.wifi);
		WifiService wifi = new WifiService();
		wifi.setName("Wifi Service");
		wifi.setDescription("Wifi Service");
		wifi.setSelected(false);
		wifi.setIcon(iconWifi);
		serviceList.add(wifi);
		
		Drawable iconBluetooth = context.getResources().getDrawable(R.drawable.bluetooth);
		BluetoothService bluetoothService = new BluetoothService();
		bluetoothService.setName("Bluetooth Service");
		bluetoothService.setDescription("Bluetooth Service");
		bluetoothService.setSelected(false);
		bluetoothService.setIcon(iconBluetooth);
		serviceList.add(bluetoothService);
		
		Drawable iconGps = context.getResources().getDrawable(R.drawable.gps);
		GPSService gpsService = new GPSService();
		gpsService.setName("GPS Service");
		gpsService.setDescription("GPS Service");
		gpsService.setSelected(false);
		gpsService.setIcon(iconGps);
		serviceList.add(gpsService);
		
		Drawable iconSoundOff = context.getResources().getDrawable(R.drawable.sound_off);
		SoundOffService soundOffService = new SoundOffService();
		soundOffService.setName("Sound off");
		soundOffService.setDescription("Sound off");
		soundOffService.setSelected(false);
		soundOffService.setIcon(iconSoundOff);
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
