package com.example.languagehelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.widget.ExpandableListView;

import com.example.languagehelper.MainActivity.Direction;
import com.example.languagehelper.dao.PageDao;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;
import com.example.languagehelper.dao.WordGroupDao;

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
		List<WordGroup> groups = mergePalabrasFromDb(pageNum, locale);
		wordAdapter = new ExpandableListAdapter(getActivity(), groups);
		setListAdapter(wordAdapter);
		super.onActivityCreated(savedInstanceState);
	}

	private List<WordGroup> getGroups(long pageNum, String locale) {
		PageDao pageDao = new PageDao(getActivity());
		Page page = pageDao.load()
				.eq(com.example.languagehelper.dao.PageTable.Columns.PAGENUM, pageNum)
				.eq(com.example.languagehelper.dao.PageTable.Columns.LOCALE, locale)
				.get();
		WordGroupDao groupDao = new WordGroupDao(getActivity());
		List<WordGroup> groups = groupDao.load()
				.eq(com.example.languagehelper.dao.WordGroupTable.Columns.PAGEID, page.getId())
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

	private List<WordGroup> mergePalabrasFromDb(long pageNum,
			String locale) {
		List<WordGroup> origGroups = getGroups(pageNum, MainActivity.ESPAÑOL);
		List<WordGroup> tradGroups = getGroups(pageNum, locale);
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
			WordPair wordPair = new WordPair(id, groupId, orig, trad, isFavorite);
			List<WordPair> words = tradGroups.get(groupPos).getWords();
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
		wordAdapter.setDirection(selectedDirection);
		ExpandableListView elv = getExpandableListView();
		elv.invalidateViews();
	}

}