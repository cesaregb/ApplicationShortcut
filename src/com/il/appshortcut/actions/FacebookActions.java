package com.il.appshortcut.actions;

import android.content.Intent;

import com.il.appshortcut.views.ActionVo;

public class FacebookActions extends CommonActions {
	
	public static final String ACTION_NEW_MESSAGE = "com.il.appshortcut.actions.facebook.newMessage"; //replace for package 
	
	public FacebookActions(){
		ActionVo action = new ActionVo();
		action.setName("New Message");
		action.setAssigned(false);
		action.setApplicationPackage(ACTION_NEW_MESSAGE);
		action.setDescription("Create new facebook message");
		action.setPatter("");
		actions.add(action);
	}
	
	public Intent getNewMessageIntent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Intent getNewPostIntent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
