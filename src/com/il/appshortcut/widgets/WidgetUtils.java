package com.il.appshortcut.widgets;

import android.content.Context;
import android.content.SharedPreferences;

import com.il.appshortcut.R;

public class WidgetUtils {
	final static String WIDGET_LEFT_ACTION 	= "com.il.appshortcut.intent.action.LEFT";
	final static String WIDGET_UP_ACTION 	= "com.il.appshortcut.intent.action.UP";
	final static String WIDGET_RIGHT_ACTION = "com.il.appshortcut.intent.action.RIGTH";
	final static String WIDGET_DOWN_ACTION 	= "com.il.appshortcut.intent.action.DOWN";
	
	final static String WIDGET_CLEAR_ACTION 	= "com.il.appshortcut.intent.action.CLEAR";
	
	final static String WIDGET_LEFT_VALUE = "1";
	final static String WIDGET_UP_VALUE = "2";
	final static String WIDGET_RIGHT_VALUE = "3";
	final static String WIDGET_DOWN_VALUE = "4";
	
	final static String WIDGET_CURRENT_SELECTION_PREF_ID = "com.il.appshortcut.widget.selection";
	final static String EMPTY_STRING = "NOTHING SELECTED";
	
	public static void updateSharedPref(Context context, String value){
		String currentSelection = getWidgetSelectionSharedPref(context) + value;
		SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.idPrefFile), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, currentSelection); // search by application  + action 
		editor.commit();
	}
	
	public static void clearSharedPref(Context context){
		SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.idPrefFile), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, "");  
		editor.commit();
	}
	
	public static String getWidgetSelectionSharedPref(Context context){
		SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.idPrefFile), Context.MODE_PRIVATE);
		String result = sharedPref.getString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, null);
		if (result == null){ result = ""; }
		return result; 
	}
}
