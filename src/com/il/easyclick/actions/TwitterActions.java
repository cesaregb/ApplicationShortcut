package com.il.easyclick.actions;

import android.content.Intent;

import com.il.easyclick.views.ActionVo;

public class TwitterActions extends CommonActions {
	public static final String TWITTER_APP_NAME = "twitter";
	public static final String TWITTER_PACKAGE = "com.twitter.android";  
	public static final String ACTION_NEW_TWITT = "com.il.appshortcut.actions.twitter.newTwitt"; //replace for package 
	
	public TwitterActions(String packageName, String className){
		super(TWITTER_PACKAGE, className);
		ActionVo action = new ActionVo();
		action.setActionName("New Twitt");
		action.setAssigned(false);
		action.setActionPackage(packageName);
		action.setActionDescription("Create new twitt ");
		action.setPattern("");
		action.setClassName(className);
		actions.add(action);
	}
	
	public Intent getNewTwittIntent() throws Exception {
		Intent twitterIntent = new Intent(Intent.ACTION_VIEW); //Intent.ACTION_VIEW
	    twitterIntent.setAction("android.intent.action.SEND");
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
