package com.il.appshortcut.actions.imp;

import java.util.List;

import android.content.Intent;

import com.il.appshortcut.actions.IFacebookActions;
import com.il.appshortcut.views.ApplicationActionItem;

public class FacebookActions extends CommonActions implements
		IFacebookActions {
	
	public static final String ACTION_NEW_MESSAGE = "something";
	
	public static List<ApplicationActionItem> getActions(){
		actions = CommonActions.getActions();
		
		ApplicationActionItem action = new ApplicationActionItem();
		action.setAction("action1");
		action.setName("action1");
		actions.add(action);
		
		ApplicationActionItem action2 = new ApplicationActionItem();
		action2.setAction("action2");
		action2.setName("action2");
		actions.add(action2);
		
		ApplicationActionItem action3 = new ApplicationActionItem();
		action3.setAction("action3");
		action3.setName("action3");
		actions.add(action3);
		
		return actions;
	}
	
	@Override
	public Intent getNewMessageIntent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent getNewPostIntent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
