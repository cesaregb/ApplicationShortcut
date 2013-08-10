package com.il.appshortcut.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.il.appshortcut.R;
import com.il.appshortcut.android.fragments.FilterApplicationsDialogFragment.FilterDialogListener;
import com.il.appshortcut.helpers.ServicesHelper;

public class ServiceListDialogFragment extends DialogFragment {

	public int typeFilter;
	public String searchString;
	public int filterCheckbox;

	FilterDialogListener mListener;
	
	public interface ServiceListDialogFragmentListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (FilterDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FilterDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final ServicesHelper helper = new ServicesHelper(getActivity().getApplicationContext());
		
		filterCheckbox = 0;
		builder.setView(inflater.inflate(R.layout.comp_activity_services_list_dialog, null))
				.setTitle(R.string.filter)
				.setPositiveButton(R.string.apply,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogPositiveClick(ServiceListDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogNegativeClick(ServiceListDialogFragment.this);
							}
						})
				.setSingleChoiceItems(helper.getServicesAsStringArray(),
						0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface paramDialogInterface, int which) {
								filterCheckbox = which;
							}
						})
//				.setMultiChoiceItems(R.array.filter_options_string_array,
//						new boolean[] { true, true },
//								new DialogInterface.OnMultiChoiceClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//								if (isChecked) {
//									filterCheckbox += (which + 1);
//								} else  {
//									filterCheckbox -= (which + 1);
//								}
//							}
//						})
		;
		return builder.create();
	}
	
}
