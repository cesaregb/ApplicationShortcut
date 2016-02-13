package com.il.easyclick.helpers;

import android.content.Intent;
import android.content.pm.PackageManager;

import com.il.easyclick.actions.CommonActions;
import com.il.easyclick.actions.FacebookActions;
import com.il.easyclick.actions.TwitterActions;
import com.il.easyclick.actions.WhatsappActions;
import com.il.easyclick.views.ActionVo;

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
		}else if (action.getParentPackage().equalsIgnoreCase(TwitterActions.TWITTER_PACKAGE)) {
				if (action.getActionPackage()
						.equalsIgnoreCase(TwitterActions.ACTION_NEW_TWITT)) {
					
					actions = new TwitterActions(action.getParentPackage(), action.getClassName());
					resultIntent = ((TwitterActions) actions).getNewTwittIntent();
				} else {
					actions = new CommonActions(action.getActionPackage(), pm);
					resultIntent = actions.getOpenApplicationIntent(action.getClassName());
				}
		}else if (action.getParentPackage().equalsIgnoreCase(WhatsappActions.WHATSAPP_PACKAGE)) {
			if (action.getActionPackage()
					.equalsIgnoreCase(WhatsappActions.ACTION_NEW_MESSAGE)) {
				
				actions = new WhatsappActions(action.getParentPackage(), action.getClassName());
				resultIntent = ((WhatsappActions) actions).getNewMessageIntent();
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



