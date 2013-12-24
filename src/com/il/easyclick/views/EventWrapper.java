package com.il.easyclick.views;

import com.il.easyclick.dao.AppshortcutDAO;

public class EventWrapper {
	private EventIconVo eventIconVo;
	private Object object;
	private int type;
	
	public EventIconVo getEventIconVo() {
		return eventIconVo;
	}
	public void setEventIconVo(EventIconVo eventIconVo) {
		this.eventIconVo = eventIconVo;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getPatter(){
		String result = "";
		try{
			if (this.type == AppshortcutDAO.TYPE_ACTION){
				ActionVo app = (ActionVo) this.object;
				result = app.getPattern();
			}else{
				ActivityVo activity = (ActivityVo) this.object;
				result = activity.getPattern();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
