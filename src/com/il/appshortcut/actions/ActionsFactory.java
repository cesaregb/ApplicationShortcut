package com.il.appshortcut.actions;

import com.il.appshortcut.views.ApplicationVo;

public class ActionsFactory {
	
	
	public static CommonActions create(ApplicationVo app){
		CommonActions ca = null;
		if (app.getApplicationPackage().equalsIgnoreCase(FacebookActions.FACEBOOK_PACKAGE)){
			ca = new FacebookActions();
		}else{
			ca = new CommonActions();
			
		}
		ca.getActions().get(0).setActionPackage(app.getApplicationPackage());
		return ca;
	}
	
}
