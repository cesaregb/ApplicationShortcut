package com.il.appshortcut.actions;

import android.content.Intent;
import android.net.Uri;

import com.il.appshortcut.views.ActionVo;

public class FoursquareActions extends CommonActions {
	public static final String FOURSQUARE_APP_NAME = "foursquare";
	public static final String FOURSQUARE_PACKAGE = "com.joelapenna.foursquared";  
	public static final String ACTION_BROWSE_VENUE = "com.il.appshortcut.actions.foursquare.browse.venue"; //replace for package 
	
	public FoursquareActions(String packageName, String className){
		super(FOURSQUARE_PACKAGE, className);
		ActionVo action = new ActionVo();
		action.setActionName("Browse Venue");
		action.setAssigned(false);
		action.setActionPackage(packageName);
		action.setActionDescription("Search for foursquare venue");
		action.setPattern("");
		action.setClassName(className);
		actions.add(action);
	}
	
	public Intent getBroseVeneIntent(){
		Uri uriUrl = Uri.parse("http://foursquare.com/venue/VENUE_ID");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        return launchBrowser;
	}
	
	public Intent getBroseVeneIntent(String venue){
		Uri uriUrl = Uri.parse("http://foursquare.com/venue/VENUE_ID");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		return launchBrowser;
	}
	
}
