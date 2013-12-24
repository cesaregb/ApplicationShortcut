package com.il.easyclick.helpers;

import static com.il.easyclick.helpers.ActionHelper.getPatternIntent;

import java.util.ArrayList;
import java.util.List;

import com.il.easyclick.config.AppManager;
import com.il.easyclick.dao.ActionsDAO;
import com.il.easyclick.dao.ActivitiesDAO;
import com.il.easyclick.dao.ActivityDetailsDAO;
import com.il.easyclick.dao.AppshortcutDAO;
import com.il.easyclick.services.ServiceVo;
import com.il.easyclick.views.ActionVo;
import com.il.easyclick.views.ActivityDetailListVo;
import com.il.easyclick.views.ActivityDetailVo;
import com.il.easyclick.views.ActivityVo;
import com.il.easyclick.views.AllAppsList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class RunnableHelper extends AsyncTask<String, Integer, String> {
	
	AppshortcutDAO dao = new AppshortcutDAO();
	ActionsDAO actionsDao;
	private AllAppsList allAppsList;
	public ActionsDAO getActionsDao() {
		return actionsDao;
	}
	public void setActionsDao(ActionsDAO actionsDao) {
		this.actionsDao = actionsDao;
	}
	public AllAppsList getAllAppsList() {
		return allAppsList;
	}
	public void setAllAppsList(AllAppsList allAppsList) {
		this.allAppsList = allAppsList;
	}
	
	public List<Intent> getIntentList(String currentSelection, Context context){
		List<Intent> result = new ArrayList<Intent>();
		try {
			int typePattern = dao.getTypePatternAssigned(currentSelection, context);
			if (typePattern > 0){
				if (typePattern == AppshortcutDAO.TYPE_ACTION){
					if (actionsDao != null){
						ActionVo action = actionsDao.getActionByPattern(currentSelection);
						Intent i = getPatternIntent(action, context.getPackageManager());
						if (i != null){
							i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							result.add(i);
						}else{ Toast.makeText(context, "mh.. application could not be ran ", Toast.LENGTH_SHORT).show(); }

					}else{
						//TODO Error to developer 
					}
				}
				if (typePattern == AppshortcutDAO.TYPE_ACTIVITY){
					ActivitiesDAO activitiesDao = new ActivitiesDAO(context);
					ActivityVo activity = activitiesDao.getActivityByPattern(currentSelection);
					if (activity != null){
						
						ActivityDetailsDAO activitiesDetailsDao = new ActivityDetailsDAO(context);
						ActivityDetailListVo mActivityDetailListVo =  new ActivityDetailListVo();
						if (allAppsList != null){
							mActivityDetailListVo.data = activitiesDetailsDao
									.getAllActivityDetailsByActivity(
											String.valueOf(activity.getIdActivity()),
											allAppsList,
											context);
						}else{
							mActivityDetailListVo.data = activitiesDetailsDao
									.getAllActivityDetailsByActivity(
											String.valueOf(activity.getIdActivity()),
											context);
						}
						
						activity.setActivityDetailListVo(mActivityDetailListVo);
						ServicesHelper servicesHelper = new ServicesHelper(context);
						for (ActivityDetailVo item : mActivityDetailListVo.data) {
							if (item.getType() == ActivitiesDAO.TYPE_ACTION) {
								Intent i = getPatternIntent(item.getApplication().getCurrentAction(), context.getPackageManager());
								if (i != null){
									i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									result.add(i);
								}else{ Toast.makeText(context, "mh.. application could not be ran", Toast.LENGTH_SHORT).show(); }
							} else {
								ServiceVo service = servicesHelper.getServiceById(item
										.getIdAction());
								service.run(context);
								Log.d(AppManager.LOG_DEBUGGIN, "service:" + service.getName());
							}
						}
					}
				}
			}else{
				Toast.makeText(context, "Pattern not assigned", Toast.LENGTH_SHORT).show();
			}
			
		} catch (Exception e) {
			Toast.makeText(context,
					"Exception.. so bad right? ", Toast.LENGTH_SHORT).show();
		}
		return result;
	}
	
	
	private List<Intent> mLIntent;
	private Context mContext;
	public List<Intent> getmLIntent() {
		return mLIntent;
	}
	public void setmLIntent(List<Intent> mLIntent) {
		this.mLIntent = mLIntent;
	}
	public Context getmContext() {
		return mContext;
	}
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	
	Handler lunchAppTimer = new Handler();
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			new RunnableHelper(mContext, mLIntent).execute();
		}
	};
	
	public RunnableHelper(Context context) {this.mContext = context; }
	
	public RunnableHelper(List<Intent> lIntent) {
		mLIntent = lIntent;
	}

	public RunnableHelper(Context context, List<Intent> lIntent) {
		mContext = context;
		mLIntent = lIntent;
	}

	public RunnableHelper(Context context, List<Intent> lIntent,
			Object callback) {
		mContext = context;
		mLIntent = lIntent;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {
		if (mLIntent.size() > 0) {
			mContext.startActivity(mLIntent.get(0));
			mLIntent.remove(0);
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
	}

	protected void onPostExecute(String params) {
		super.onPostExecute(params);
		lunchAppTimer.removeCallbacks(runnable);
		lunchAppTimer.postDelayed(runnable, 2000);
	}
	
}
