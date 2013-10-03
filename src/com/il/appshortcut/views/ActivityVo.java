package com.il.appshortcut.views;


public class ActivityVo {
	
	private int idActivity;
	private String name;
	private String description;
	private String pattern;
	private boolean assigned;
	private ActivityDetailListVo activityDetailListVo;
	private int idIcon;
	
	public ActivityVo(){}
	
	public ActivityVo(String _name){
		this.name = _name;
	}
	
	public int getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public String toString(){
		return this.name + " -- " + this.description;
	}

	public int getIdIcon() {
		return idIcon;
	}

	public void setIdIcon(int idIcon) {
		this.idIcon = idIcon;
	}

	public boolean isSavedActivity(){
		return (this != null 
					&& this.idActivity > 0);
	}

	public ActivityDetailListVo getActivityDetailListVo() {
		return activityDetailListVo;
	}

	public void setActivityDetailListVo(ActivityDetailListVo activityDetailListVo) {
		this.activityDetailListVo = activityDetailListVo;
	}
}
