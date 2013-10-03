package com.il.appshortcut.helpers;

import android.content.Intent;
import android.content.pm.PackageManager;

import com.il.appshortcut.actions.CommonActions;
import com.il.appshortcut.actions.FacebookActions;
import com.il.appshortcut.views.ActionVo;

public class ActionHelper {
	public static final String SEPARATOR = "-";
	public static final String SEPARATOR_2 = ":";
	
	public static Intent getPatternIntent(ActionVo action, PackageManager pm)
					throws Exception {
		Intent resultIntent = null;
		CommonActions actions = null;
		if (action.getParentPackage().equalsIgnoreCase(FacebookActions.FACEBOOK_PACKAGE)) {
			if (action.getActionPackage()
					.equalsIgnoreCase(FacebookActions.ACTION_NEW_MESSAGE)) {
				actions = new FacebookActions(action.getParentPackage(), action.getClassName());
				resultIntent = ((FacebookActions) actions).getNewMessageIntent();
			} else {
				actions = new CommonActions(action.getActionPackage(), pm);
				resultIntent = actions.getOpenApplicationIntent(action.getClassName());
			}
		}else{
			actions = new CommonActions(action.getActionPackage(), pm);
			resultIntent = actions.getOpenApplicationIntent(action.getClassName());
		}
		return resultIntent;
	}
	
	
}



