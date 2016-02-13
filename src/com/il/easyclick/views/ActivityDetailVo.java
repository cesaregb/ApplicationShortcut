package com.il.easyclick.views;

import android.graphics.drawable.Drawable;

import com.il.easyclick.services.ServiceVo;

public class ActivityDetailVo {
	private int idActivityDetail;
	private int type;
	private int order;
	private int top;
	private int actionType;
	private int idActivity;
	private int idAction;
	private Drawable icon;
	private ApplicationVo application;
	private ServiceVo service;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public int getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}
	public int getIdAction() {
		return idAction;
	}
	public void setIdAction(int idAction) {
		this.idAction = idAction;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public int getIdActivityDetail() {
		return idActivityDetail;
	}
	public void setIdActivityDetail(int idActivityDetail) {
		this.idActivityDetail = idActivityDetail;
	}
	@Override
	public boolean equals(Object o) {
		ActivityDetailVo detail = (ActivityDetailVo) o;
		return (this.order == detail.getOrder());
	}
	public ApplicationVo getApplication() {
		return application;
	}
	public void setApplication(ApplicationVo application) {
		this.application = application;
	}
	public ServiceVo getService() {
		return service;
	}
	public void setService(ServiceVo service) {
		this.service = service;
	}
	
}
