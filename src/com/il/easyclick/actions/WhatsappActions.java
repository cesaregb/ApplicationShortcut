package com.il.easyclick.actions;

import android.content.Intent;

import com.il.easyclick.views.ActionVo;

public class WhatsappActions extends CommonActions {
	public static final String WHATSAPP_APP_NAME = "whatsapp";
	public static final String WHATSAPP_PACKAGE = "com.whatsapp";  
	public static final String ACTION_NEW_MESSAGE = "com.il.appshortcut.actions.whatsapp.newMessage"; //replace for package 
	
	public WhatsappActions(String packageName, String className){
		super(WHATSAPP_PACKAGE, className);
		ActionVo action = new ActionVo();
		action.setActionName("New Message");
		action.setAssigned(false);
		action.setActionPackage(packageName);
		action.setActionDescription("Create new whatsapp message");
		action.setPattern("");
		action.setClassName(className);
		actions.add(action);
	}
	
	public Intent getNewMessageIntent() throws Exception {
		Intent waIntent = new Intent(Intent.ACTION_SEND);
	    waIntent.setType("text/plain");
	            String text = "";
	    waIntent.setPackage("com.whatsapp");
	    waIntent.putExtra(Intent.EXTRA_TEXT, text);//
	    return waIntent;
	}

}
