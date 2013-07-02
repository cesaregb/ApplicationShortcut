package com.il.appshortcut.config;

public class AppManager {
	public static String LOG_WIDGET = "LOG_WIDGET";
	public static String LOG_EXCEPTIONS = "LOG_EXCEPTIONS";
	
	private static AppManager _instance;
	public static String CLEAR_STRING = "";
	public static String ID_PRE_FFILE = "com.il.appshortcut";
	
	
	private String currentSelection;
	
	private AppManager() { }

	public static AppManager getInstance() {
		if (_instance == null) {
			_instance = new AppManager();
			_instance.setCurrentSelection(CLEAR_STRING);
		}
		return _instance;
	}

	public String getCurrentSelection() {
		return this.currentSelection;
	}

	public void setCurrentSelection(String currentSelection) {
		this.currentSelection = currentSelection;
	}
	
	public void addKeyToCurrentSelection(String key){
		if (currentSelection == null){
			this.currentSelection = CLEAR_STRING;
		}
		this.currentSelection += key;
	}
}
