package com.il.appshortcut.views;


public class ActionVo {
	
	private String name;
	private String patter;
	private String description;
	private String applicationPackage;
	private boolean assigned = false;
	
	public String getPatter() {
		return patter;
	}
	public void setPatter(String patter) {
		this.patter = patter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApplicationPackage() {
		return applicationPackage;
	}
	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}
	
}
