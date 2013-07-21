package com.il.appshortcut.actions;

import android.content.Intent;
import android.net.Uri;

import com.il.appshortcut.views.ApplicationActionVo;

public class FacebookActions extends CommonActions {
	public static final String FACEBOOK_APP_NAME = "facebook";
	public static final String FACEBOOK_PACKAGE = "com.facebook.katana";  
	public static final String ACTION_NEW_MESSAGE = "com.il.appshortcut.actions.facebook.newMessage"; //replace for package 
	
	public FacebookActions(){
		ApplicationActionVo action = new ApplicationActionVo();
		action.setName("New Message");
		action.setAssigned(false);
		action.setActionPackage(ACTION_NEW_MESSAGE);
		action.setDescription("Create new facebook message");
		action.setPatter("");
		actions.add(action);
	}
	
	public Intent getNewMessageIntent() throws Exception {
		return new Intent(Intent.ACTION_VIEW,
	              Uri.parse("fb://messaging/"));
	}

	public Intent getNewPostIntent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
