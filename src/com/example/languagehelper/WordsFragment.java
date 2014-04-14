package com.example.languagehelper;

import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.util.Log;
import android.widget.ExpandableListView;

import com.squareup.otto.Subscribe;

public class WordsFragment extends ExpandableListFragment {

	public static final String KEY_TAB_NUM = "tabNum";
	public static final String KEY_LOCALE = "locale";
	public static final String KEY_PAGE_NUM = "pageId";
	private static final String KEY_GROUP_STATE = "groupState";
	
	private ExpandableListAdapter wordAdapter;
	private String locale;
	private long pageNum;
	
	public WordsFragment() {
		super();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// Get locale and classification from Bundle
		this.locale = getArguments().getString(KEY_LOCALE);
		this.pageNum = getArguments().getLong(KEY_PAGE_NUM);
		wordAdapter = new ExpandableListAdapter(getActivity(), pageNum, locale);
		setListAdapter(wordAdapter);
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		ApplicationState.getBus().unregister(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ApplicationState.getBus().register(this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		if (inState == null) {
			return;
		}
		ExpandableListView elv = getExpandableListView();
		boolean[] groupState = inState.getBooleanArray(KEY_GROUP_STATE);
		for (int i = 0; i < groupState.length; i++) {
			if (groupState[i]) {
				elv.expandGroup(i);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		ExpandableListView elv = getExpandableListView();
		int numGroups = elv.getChildCount();
		boolean[] groupState = new boolean[numGroups];
		for (int i = 0; i < numGroups; i++) {
			if (elv.isGroupExpanded(i)) {
				groupState[i] = true;
			}
		}
		outState.putBooleanArray(KEY_GROUP_STATE, groupState);
	}
	
	@Subscribe public void notifyDirectionChanged(OrderChangedEvent event) {
		wordAdapter.notifyDirectionChanged();
		ExpandableListView elv = getExpandableListView();
		elv.invalidateViews();
	}

}