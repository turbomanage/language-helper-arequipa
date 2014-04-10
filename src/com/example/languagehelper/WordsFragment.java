package com.example.languagehelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.widget.ExpandableListView;

import com.example.languagehelper.MainActivity.Direction;
import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;
import com.example.languagehelper.dao.WordGroupDao;

public class WordsFragment extends ExpandableListFragment {

	public static final String KEY_TAB_NUM = "tabNum";
	public static final String KEY_LOCALE = "locale";
	private static final String KEY_GROUP_STATE = "groupState";
	
	private ExpandableListAdapter wordAdapter;
	private int tabNum;
	private String locale;
	
	public WordsFragment() {
		super();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		tabNum = getArguments().getInt(KEY_TAB_NUM);
		locale = getArguments().getString(KEY_LOCALE);
		// Get locale and classification from Bundle
		Classification tabClassification = Classification.values()[tabNum];
		List<WordGroup> groups = mergePalabrasFromDb(tabClassification, locale);
		wordAdapter = new ExpandableListAdapter(getActivity(), groups);
		setListAdapter(wordAdapter);
		// Expand all groups by default
		ExpandableListView elv = getExpandableListView();
		for (int i = 0; i < groups.size(); i++) {
			elv.expandGroup(i);
		}
		super.onActivityCreated(savedInstanceState);
	}

	private List<WordGroup> getGroups(Classification tabClassification, String locale) {
		WordGroupDao groupDao = new WordGroupDao(getActivity());
		List<WordGroup> groups = groupDao.load()
				.eq(com.example.languagehelper.dao.WordGroupTable.Columns.CLASSIFICATION, tabClassification)
				.eq(com.example.languagehelper.dao.WordGroupTable.Columns.LOCALE, locale)
				.list();
		return groups;
	}

	private List<Palabra> getWordsInGroup(List<WordGroup> groups) {
		String[] groupIds = new String[groups.size()];
		for (int i = 0; i < groupIds.length; i++) {
			groupIds[i] = Long.toString(groups.get(i).getId());
		}
		PalabraDao dao = new PalabraDao(getActivity());
		List<Palabra> words = dao.load()
				.in(Columns.GROUPID, groupIds)
				.order(Columns.ORD.asc())
				.list();
		return words;
	}

	private List<WordGroup> mergePalabrasFromDb(Classification type,
			String locale) {
		List<WordGroup> origGroups = getGroups(type, locale);
		List<WordGroup> tradGroups = getGroups(type, locale);
		List<Palabra> origWords = getWordsInGroup(origGroups);
		List<Palabra> tradWords = getWordsInGroup(tradGroups);

		// Create map of groupIds to position in the list
		Map<Long, Integer> joinMap = new HashMap<Long, Integer>();
		for (int i = 0; i < tradGroups.size(); i++) {
			joinMap.put(tradGroups.get(i).getId(), i);
		}

		// skip title
		for (int j = 0; j < origWords.size(); j++) {
			long id = origWords.get(j).getId();
			String orig = origWords.get(j).getWord();
			String trad = tradWords.get(j).getWord();
			boolean isFavorite = origWords.get(j).isFavorite();
			long groupId = tradWords.get(j).getGroupId(); // origWords
			Integer groupPos = joinMap.get(groupId);
			List<WordPair> words = tradGroups.get(groupPos).getWords();
			WordPair wordPair = new WordPair(id, groupId, orig, trad, isFavorite);
			words.add(wordPair);
		}
		
		return tradGroups;
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
	
	public void setDirection(Direction selectedDirection) {
		// TODO Fix this Rubén
//		wordAdapter.setDirection(selectedDirection);
	}

}