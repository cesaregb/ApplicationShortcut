package com.il.appshortcut.config;

public class AppManager {
	private static AppManager _instance;
	public static String CLEAR_STRING = "";

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
