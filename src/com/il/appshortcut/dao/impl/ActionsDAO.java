package com.il.appshortcut.dao.impl;

import static com.il.appshortcut.converter.ActionsConverter.convertActions2ContentValues;
import static com.il.appshortcut.converter.ActionsConverter.convertCursor2Action;
import static com.il.appshortcut.converter.ActionsConverter.convertCursor2ListAction;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.il.appshortcut.sqlite.ActionsOpenHelper;
import com.il.appshortcut.views.ActionVo;

public class ActionsDAO {

	private SQLiteDatabase database;

	private ActionsOpenHelper dbHelper;

	private String[] allColumns = { ActionsOpenHelper.FIELD_ID,
			ActionsOpenHelper.FIELD_ACTION_NAME,
			ActionsOpenHelper.FIELD_ACTION_PACKAGE,
			ActionsOpenHelper.FIELD_PARENT_PACKAGE,
			ActionsOpenHelper.FIELD_ASSIGNED, ActionsOpenHelper.FIELD_PATTERN,
			ActionsOpenHelper.FIELD_TYPE,
			ActionsOpenHelper.FIELD_ACTION_DESCRIPTION, 
			ActionsOpenHelper.FIELD_ACTION_CLASS_NAME 
			};

	public ActionsDAO(Context context) {
		dbHelper = new ActionsOpenHelper(context);
	}
	
	public void clearDatabase(){
		this.open();
		database.delete(ActionsOpenHelper.TABLE_NAME, null, null);
		this.close();
	}
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean isActionActive(ActionVo action) {
		return false;
	}

	public void addAction(ActionVo action) {
		this.open();
		ContentValues values = convertActions2ContentValues(action);
		database.insert(ActionsOpenHelper.TABLE_NAME, null, values);
		database.close();
		this.close();
	}

	public boolean isActionActive(String appPackage) {
		ActionVo action = getAction(new ActionVo(0, appPackage));
		return (action != null && action.getIdAction() > 0 && action
				.isAssigned());
	}

	public ActionVo getAction(ActionVo action) {
		this.open();
		ActionVo result = null;
		if (action != null && action.getActionPackage() != null) {

			Cursor cursor = database.query(ActionsOpenHelper.TABLE_NAME,
					allColumns, ActionsOpenHelper.FIELD_ACTION_PACKAGE + "=?",
					new String[] { String.valueOf(action.getActionPackage()) },
					null, null, null, null);

			if (cursor != null) {
				result = convertCursor2Action(cursor);
			}
		}
		this.close();
		return result;
	}
	
	public ActionVo getActionByPattern(String pattern) {
		this.open();
		ActionVo result = null;
		if (pattern != null) {
			Cursor cursor = database.query(ActionsOpenHelper.TABLE_NAME,
					allColumns, ActionsOpenHelper.FIELD_PATTERN + "=?",
					new String[] { pattern },
					null, null, null, null);
			if (cursor != null) {
				result = convertCursor2Action(cursor);
			}
		}
		this.close();
		return result;
	}
	
	public List<ActionVo> getAllActions() {
		List<ActionVo> list = null;
		this.open();
		Cursor cursor = database.query(ActionsOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, null, null);
		if (cursor != null) { list = convertCursor2ListAction(cursor); }
		this.close();
		return list;
	}
}
