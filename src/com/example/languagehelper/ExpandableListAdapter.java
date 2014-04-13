package com.example.languagehelper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.languagehelper.ApplicationState.Model;
import com.example.languagehelper.dao.PageDao;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;
import com.example.languagehelper.dao.WordGroupDao;

/**
 * http://www.dreamincode.net/forums/topic/270612-how-to-get-started-with-
 * expandablelistview/
 * 
 * @author david
 * 
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private PalabraDao palabraDao;
	private List<WordGroup> origGroups;
	private List<WordGroup> tradGroups;
	private List<WordGroup> selectedGroups;

	public ExpandableListAdapter(Context ctx, long pageNum, String locale) {
		this.mContext = ctx;
		palabraDao = new PalabraDao(mContext);
		sortAndGroup(pageNum, locale);
		setDirection();
	}

	private void sortAndGroup(long pageNum, String locale) {
		origGroups = loadGroups(pageNum, MainActivity.ESPAÑOL);
		tradGroups = loadGroups(pageNum, locale);
		List<Palabra> origWords = loadWordsInGroup(origGroups);
		List<Palabra> tradWords = loadWordsInGroup(tradGroups);

		WordPair[] pairs = new WordPair[origWords.size()];
		// skip title
		for (int j = 0; j < origWords.size(); j++) {
			Palabra orig = origWords.get(j);
			Palabra trad = tradWords.get(j);
			WordPair wordPair = new WordPair(orig, trad);
			pairs[j] = wordPair;
		}

		WordPair[] sortedByOrig = pairs.clone();
		Arrays.sort(sortedByOrig, sortByOrig);
		WordPair[] sortedByTrad = pairs.clone();
		Arrays.sort(sortedByTrad, sortByTrad);
		
		// Create map of groupIds to position in the list
		Map<Long, Integer> origMap = new HashMap<Long, Integer>();
		for (int i = 0; i < origGroups.size(); i++) {
			origMap.put(origGroups.get(i).getId(), i);
		}
		// Create map of groupIds to position in the list
		Map<Long, Integer> tradMap = new HashMap<Long, Integer>();
		for (int i = 0; i < tradGroups.size(); i++) {
			tradMap.put(tradGroups.get(i).getId(), i);
		}
		
		// Put each pair into corresponding groups by group ID
		for (int i = 0; i < sortedByOrig.length; i++) {
			WordPair pair = sortedByOrig[i];
			// Add pair to orig group
			long origGroupId = pair.getOrig().getGroupId();
			int origGroupPos = origMap.get(origGroupId);
			origGroups.get(origGroupPos).getWords().add(pair);
		}
		for (int i = 0; i < sortedByTrad.length; i++) {
			WordPair pair = sortedByTrad[i];
			// Add pair to trad group
			long tradGroupId = pair.getTrad().getGroupId();
			int tradGroupPos = tradMap.get(tradGroupId);
			tradGroups.get(tradGroupPos).getWords().add(pair);
		}
	}

	private List<WordGroup> loadGroups(long pageNum, String locale) {
		PageDao pageDao = new PageDao(this.mContext);
		Page page = pageDao
				.load()
				.eq(com.example.languagehelper.dao.PageTable.Columns.PAGENUM,
						pageNum)
				.eq(com.example.languagehelper.dao.PageTable.Columns.LOCALE,
						locale).get();
		WordGroupDao groupDao = new WordGroupDao(this.mContext);
		List<WordGroup> groups = groupDao
				.load()
				.eq(com.example.languagehelper.dao.WordGroupTable.Columns.PAGEID,
						page.getId()).list();
		return groups;
	}

	private List<Palabra> loadWordsInGroup(List<WordGroup> groups) {
		String[] groupIds = new String[groups.size()];
		for (int i = 0; i < groupIds.length; i++) {
			groupIds[i] = Long.toString(groups.get(i).getId());
		}
		PalabraDao dao = new PalabraDao(this.mContext);
		List<Palabra> words = dao.load().in(Columns.GROUPID, groupIds)
				.order(Columns.ORD.asc()).list();
		return words;
	}

	private Comparator<WordPair> sortByOrig = new Comparator<WordPair>() {
		@Override
		public int compare(WordPair lhs, WordPair rhs) {
			int groupCompare = ExpandableListAdapter.compare(lhs.getOrig()
					.getGroupId(), rhs.getOrig().getGroupId());
			if (groupCompare == 0) {
				return lhs.getOrig().getWord()
						.compareTo(rhs.getOrig().getWord());
			} else {
				return groupCompare;
			}
		}

	};

	private Comparator<WordPair> sortByTrad = new Comparator<WordPair>() {
		@Override
		public int compare(WordPair lhs, WordPair rhs) {
			int groupCompare = ExpandableListAdapter.compare(lhs.getTrad()
					.getGroupId(), rhs.getTrad().getGroupId());
			if (groupCompare == 0) {
				return lhs.getTrad().getWord()
						.compareTo(rhs.getTrad().getWord());
			} else {
				return groupCompare;
			}
		}
	};

	private static int compare(long lhs, long rhs) {
		if (lhs == rhs) {
			return 0;
		} else if (lhs > rhs) {
			return 1;
		} else {
			return -1;
		}
	}

	public void setDirection() {
		if (Model.INSTANCE.getDirection()) {
			this.selectedGroups = tradGroups;
		} else {
			this.selectedGroups = origGroups;
		}
		notifyDataSetChanged();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<WordPair> chList = selectedGroups.get(groupPosition).getWords();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		final WordPair wordPair = (WordPair) getChild(groupPosition,
				childPosition);
		// TODO optimize?
		if (true) {
			LayoutInflater infalInflater = (LayoutInflater) mContext
					.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			if (Model.INSTANCE.getDirection()) {
				view = infalInflater.inflate(R.layout.row_trad_first, null);
			} else {
				view = infalInflater.inflate(R.layout.row_orig_first, null);
			}
		}
		final ImageButton star = (ImageButton) view.findViewById(R.id.star);
		star.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toggle state
				star.setSelected(!star.isSelected());
				// Update underlying database
				Palabra orig = palabraDao.get(wordPair.getOrig().getId());
				orig.setFavorite(star.isSelected());
				palabraDao.update(orig);
			}
		});
		star.setSelected(wordPair.getOrig().isFavorite());
		TextView orig = (TextView) view.findViewById(R.id.orig);
		TextView trad = (TextView) view.findViewById(R.id.trad);
		orig.setText(wordPair.getOrig().getWord());
		trad.setText(wordPair.getTrad().getWord());
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		List<WordPair> chList = selectedGroups.get(groupPosition).getWords();
		return chList.size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return selectedGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return selectedGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		WordGroup group = (WordGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) mContext
					.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.group_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.category);
		tv.setText(group.getName());
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}