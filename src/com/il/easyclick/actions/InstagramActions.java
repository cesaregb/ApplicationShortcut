package com.il.easyclick.actions;

import android.content.Intent;

import com.il.easyclick.views.ActionVo;

public class InstagramActions extends CommonActions {
	public static final String INSTAGRAM_APP_NAME = "twitter";
	public static final String INSTAGRAM_PACKAGE = "com.instagram.android";  
	public static final String ACTION_NEW_INSTAGRAM_PHOTO = "com.il.appshortcut.actions.instagram.newPhoto"; //replace for package 
	
	public InstagramActions(String packageName, String className){
		super(INSTAGRAM_PACKAGE, className);
		ActionVo action = new ActionVo();
		action.setActionName("New Twitt");
		action.setAssigned(false);
		action.setActionPackage(packageName);
		action.setActionDescription("Create new facebook message");
		action.setPattern("");
		action.setClassName(className);
		actions.add(action);
	}
	
	public Intent getNewMessageIntent() throws Exception {
		Intent twitterIntent = new Intent(Intent.ACTION_VIEW); //Intent.ACTION_VIEW
	    twitterIntent.setAction(".creation.activity.MediaCaptureActivity");
	    twitterIntent.setFlags(0x3000000);  
	    twitterIntent.setType("text/plain");    
	    twitterIntent.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
	    twitterIntent.putExtra(Intent.EXTRA_TEXT, ("Ehmm"));
		return twitterIntent;
	}

	public Intent getNewPostIntent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
