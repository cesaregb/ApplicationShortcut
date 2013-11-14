package com.il.appshortcut.config;

import java.util.ArrayList;
import java.util.List;

import com.il.appshortcut.views.EventWrapper;

public class AppManager {
	public static String LOG_DEBUGGIN = "LOG_DEBUGGIN";
	public static String LOG_WIDGET = "LOG_WIDGET";
	public static String LOG_APPLICATION_INFO_FRAGMENT = "LOG_APPLICATION_INFO_FRAGMENT";
	public static String LOG_EXCEPTIONS = "LOG_EXCEPTIONS";
	public static String LOG_SQL = "LOG_SQL";
	public static String LOG_ACTIONS = "LOG_ACTIONS";
	public static String LOG_ACTIVITIES = "LOG_ACTIVITIES";
	
	public static String LOG_MANAGE_APPLICATIONS= "LOG_MANAGE_APPLICATIONS";
	
	
	private static AppManager _instance;
	public static String CLEAR_STRING = "";
	public static String ID_PRE_FFILE = "com.il.appshortcut";
	public static String ACTIVITY_ACTION_PARAM = "ACTIVITY_ACTION";
	public static String ACTIVITY_ACTION_RESULT_PARAM = "ACTIVITY_ACTION_RESULT";
	public static int ACTIVITY_ACTION_FROM_MAIN = 0;
	public static int ACTIVITY_ACTION_FROM_ACTIVITIES = 1;
	
	public static String WIDGET_PROXY_SELECTION = "WIDGET_PROXY_SELECTION";

	private List<EventWrapper> listEvents;
	
	private AppManager() { }

	public static AppManager getInstance() {
		if (_instance == null) {
			_instance = new AppManager();
		}
		return _instance;
	}

	public List<EventWrapper> getListEvents() {
		if (listEvents == null){
			listEvents = new ArrayList<EventWrapper>();
		}
		return listEvents;
	}
	
	public void addEvent(EventWrapper event) {
		if (_instance != null){
			listEvents = (listEvents != null)?listEvents: new ArrayList<EventWrapper>();
			if (listEvents.size() >= 3){
				listEvents.set(0, listEvents.get(1));
				listEvents.set(1, listEvents.get(2));
				listEvents.set(2, event);
			}else{
				listEvents.add(event);
			}
		}
	}

	public void setListEvents(List<EventWrapper> listEvents) {
		this.listEvents = listEvents;
	}


}
