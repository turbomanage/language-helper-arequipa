package com.example.languagehelper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.widget.ExpandableListView;

import com.squareup.otto.Subscribe;

public class WordsFragment extends ExpandableListFragment {

	private static final String KEY_EXPANDED_GROUPS_FOR_PAGE = "groupsForPage";
	public static final String KEY_TAB_NUM = "tabNum";
	public static final String KEY_LOCALE = "locale";
	public static final String KEY_PAGE_NUM = "pageId";

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
		App.getEventBus().unregister(this);
		ExpandableListView elv = getExpandableListView();
		ExpandableListAdapter ela = (ExpandableListAdapter) getExpandableListAdapter();
		int numGroups = ela.getGroupCount();
		int groupStateMask = 0;
		for (int i = 0; i < numGroups; i++) {
			if (elv.isGroupExpanded(i)) {
				groupStateMask += (1<<i);
			}
		}
		// Store in preferences
		SharedPreferences settings = getActivity().getPreferences(0);
		Editor prefs = settings.edit();
		String key = KEY_EXPANDED_GROUPS_FOR_PAGE + this.pageNum;
		prefs.putInt(key, groupStateMask);
		prefs.commit();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		App.getEventBus().register(this);
		// Retrieve expanded groups from preferences
		ExpandableListView elv = getExpandableListView();
		ExpandableListAdapter ela = (ExpandableListAdapter) getExpandableListAdapter();
		int numGroups = ela.getGroupCount();
		SharedPreferences settings = getActivity().getPreferences(0);
		String key = KEY_EXPANDED_GROUPS_FOR_PAGE + this.pageNum;
		int groupStateMask = settings.getInt(key, 0);
		for (int i = 0; i < numGroups; i++) {
			if ((groupStateMask & (1<<i)) != 0) {
				elv.expandGroup(i);
			}
		}
	}

	@Subscribe
	public void notifyDirectionChanged(OrderChangedEvent event) {
		wordAdapter.notifyDirectionChanged();
		ExpandableListView elv = getExpandableListView();
		elv.invalidateViews();
	}

}