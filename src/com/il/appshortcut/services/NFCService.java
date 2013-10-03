package com.il.appshortcut.services;

import android.content.Context;

public class NFCService extends ServiceVo {
	public NFCService(){
		ID = 4;
	}
	
	@Override
	public boolean run(Context context) {
		return false;
	}

}
