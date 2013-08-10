package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.il.appshortcut.R;
import com.il.appshortcut.services.AirplaneModeService;
import com.il.appshortcut.services.BluetoothService;
import com.il.appshortcut.services.ServiceVo;
import com.il.appshortcut.services.WifiService;

public class ServicesHelper {
	private List<ServiceVo> serviceList;
	
	public ServicesHelper(Context context){
		serviceList = new ArrayList<ServiceVo>();
		
		Drawable icon = context.getResources().getDrawable(R.drawable.cuadrito);
		
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
		
	}

	public List<ServiceVo> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<ServiceVo> serviceList) {
		this.serviceList = serviceList;
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
