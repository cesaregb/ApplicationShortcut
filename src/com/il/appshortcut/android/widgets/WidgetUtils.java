package com.il.appshortcut.android.widgets;

import android.content.Context;

import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.exception.AppShortcutException;

public class WidgetUtils {
	public final static String WIDGET_LEFT_ACTION 	= "com.il.appshortcut.intent.action.LEFT";
	public final static String WIDGET_UP_ACTION 	= "com.il.appshortcut.intent.action.UP";
	public final static String WIDGET_RIGHT_ACTION = "com.il.appshortcut.intent.action.RIGTH";
	public final static String WIDGET_DOWN_ACTION 	= "com.il.appshortcut.intent.action.DOWN";
	
	public final static String WIDGET_CLEAR_ACTION 	= "com.il.appshortcut.intent.action.CLEAR";
	
	public final static String WIDGET_LEFT_VALUE = "1";
	public final static String WIDGET_UP_VALUE = "2";
	public final static String WIDGET_RIGHT_VALUE = "3";
	public final static String WIDGET_DOWN_VALUE = "4";
	
	public final static String WIDGET_CURRENT_SELECTION_PREF_ID = "com.il.appshortcut.widget.selection";
	
	public final static String TAG_CURRENT_SELECTION = "TAG_CURRENT_SELECTION";
	public final static String IS_KEYWARD = "IS_KEYWARD";
	public final static String EMPTY_STRING = "NOTHING SELECTED";
	
	public static String updateSharedPref(Context context, String value) throws AppShortcutException{
		AppshortcutDAO dao = new AppshortcutDAO();
		return dao.updateWidgetSelection(context, value);
	}
	
	public static void clearSharedPref(Context context) throws AppShortcutException{
		AppshortcutDAO dao = new AppshortcutDAO();
		dao.clearWidgetSelection(context);
	}
	
	public static String getWidgetSelectionSharedPref(Context context) throws AppShortcutException{
		AppshortcutDAO dao = new AppshortcutDAO();
		return dao.getWidgetSelection(context);
	}
}
