package com.il.appshortcut.android;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.dao.AppshortcutDAO;

public class SettingsActivity extends PreferenceActivity  {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.add(android.R.id.content, new PrefsFragment())
				.commit();
    }
	
	public static class PrefsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);
			final AppshortcutDAO dao = new AppshortcutDAO();
			
			Preference prefDeleteAll = (Preference) findPreference("pref_key_delete_all_info");
			prefDeleteAll.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					try{
						dao.removeAll(getActivity());
						Toast.makeText(getActivity(), "All information has been deleted ", Toast.LENGTH_SHORT).show();
					}catch(Exception e){/*TODO add Exception handlin*/}
					return true;
				}
			});
			
			Preference prefDeleteApps = (Preference) findPreference("pref_key_delete_applications");
			prefDeleteApps.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					try{
						dao.removeActions(getActivity());
						Toast.makeText(getActivity(), "All Actions had been deleted ", Toast.LENGTH_SHORT).show();
					}catch(Exception e){/*TODO add Exception handlin*/}
					return true;
				}
			});
			
			Preference prefDeleteActivities = (Preference) findPreference("pref_key_delete_activities");
			prefDeleteActivities.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					try{
						dao.removeActivities(getActivity());
						Toast.makeText(getActivity(), "All Activities had been deleted ", Toast.LENGTH_SHORT).show();
					}catch(Exception e){/*TODO add Exception handlin*/}
					return true;
				}
			});
		}
	}
}
