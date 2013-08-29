package com.il.appshortcut.views;

public class ActionVo {
	private int idAction;
	private String actionName;
	private String actionDescription;
	private String actionPackage;
	private String parentPackage;
	private String pattern;
	private boolean assigned;
	private int type;
	private String className;
	
	public ActionVo(){ }
	
	public ActionVo (int id, String appPackage){
		this.idAction = id;
		this.actionPackage = appPackage;
	}
	
	public int getIdAction() {
		return idAction;
	}
	public void setIdAction(int idAction) {
		this.idAction = idAction;
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
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getParentPackage() {
		return parentPackage;
	}
	public void setParentPackage(String parentPackage) {
		this.parentPackage = parentPackage;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
