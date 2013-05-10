package com.il.appshortcut.actions;

import android.content.Intent;

public interface IFacebookActions {
	public Intent getNewMessageIntent() throws Exception;
	public Intent getNewPostIntent() throws Exception; 
	
}
