package com.il.appshortcut.services;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;


public class BluetoothService extends ServiceVo {
	private static final int REQUEST_ENABLE_BT = 0;
    
	@Override
	public boolean run(Context context) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
		if (mBluetoothAdapter.isEnabled()) {
		    mBluetoothAdapter.disable(); 
		}else{
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		return true;
	}

}
