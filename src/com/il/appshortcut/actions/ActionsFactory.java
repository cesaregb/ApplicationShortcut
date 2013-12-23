package com.il.appshortcut.actions;

import com.il.appshortcut.views.ApplicationVo;

public class ActionsFactory {
	
	
	public static CommonActions create(ApplicationVo app){
		CommonActions ca = null;
		if (app.getApplicationPackage().equalsIgnoreCase(FacebookActions.FACEBOOK_PACKAGE)){
			ca = new FacebookActions(app.getApplicationPackage(), app.getComponentName().getClassName());
		}else if(app.getApplicationPackage().equalsIgnoreCase(TwitterActions.TWITTER_PACKAGE)){ 
			ca = new TwitterActions(app.getApplicationPackage(), app.getComponentName().getClassName());
		}else if(app.getApplicationPackage().equalsIgnoreCase(WhatsappActions.WHATSAPP_PACKAGE)){ 
			ca = new WhatsappActions(app.getApplicationPackage(), app.getComponentName().getClassName());
		}else{
			ca = new CommonActions(app.getApplicationPackage(), app.getComponentName().getClassName());
		}
		ca.getActions().get(0).setActionPackage(app.getApplicationPackage());
		return ca;
	}
	
}
