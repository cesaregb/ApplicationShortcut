package com.il.appshortcut.helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.il.appshortcut.android.ProxyActivity;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.views.ActionVo;

public class WidgetHelper {
	
	
	public static PendingIntent buildLunchEventBtnPendingIntent(Context context, String currentSelection) {
		Intent i = null;
		try{
			AppshortcutDAO dao = new AppshortcutDAO();
			ActionsDAO actionsDao = new ActionsDAO(context);
			int typePattern = dao.getTypePatternAssigned(currentSelection,
					context);
			
			if (typePattern > 0) {
				if (typePattern == AppshortcutDAO.TYPE_ACTION) {
					ActionVo action = actionsDao
							.getActionByPattern(currentSelection);
					i = com.il.appshortcut.helpers.ActionHelper
							.getPatternIntent(action,
									context.getPackageManager());
				}
				if (typePattern == AppshortcutDAO.TYPE_ACTIVITY) {
					i = new Intent(context, ProxyActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					i.putExtra(AppManager.WIDGET_PROXY_SELECTION, currentSelection);
				}
			}
		} catch (Exception e){}
		return PendingIntent.getActivity(context, 0, i, 0);
	}
}
