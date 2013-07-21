package com.il.appshortcut.views;

public class ActionVo {
	private int idAction;
	private String activityName;
	private String actionPackage;
	private String applicationPackage;
	private String pattern;
	private boolean assigned;
	private int type;
	
	public int getIdAction() {
		return idAction;
	}
	public void setIdAction(int idAction) {
		this.idAction = idAction;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getActionPackage() {
		return actionPackage;
	}
	public void setActionPackage(String actionPackage) {
		this.actionPackage = actionPackage;
	}
	public String getApplicationPackage() {
		return applicationPackage;
	}
	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}
	
}
