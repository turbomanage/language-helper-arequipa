package com.example.languagehelper;

import java.util.List;

import com.example.languagehelper.dao.PalabraDao;
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * http://www.dreamincode.net/forums/topic/270612-how-to-get-started-with-expandablelistview/
 * @author david
 *
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<WordGroup> groups;
	private PalabraDao palabraDao;
	
	public ExpandableListAdapter(Context context, List<WordGroup> groups2) {
		this.context = context;
		this.groups = groups2;
		palabraDao = new PalabraDao(context);
	}
	
	public void addItem(PalabraMap item, WordGroup group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		List<PalabraMap> ch = groups.get(index).getWords();
		ch.add(item);
		groups.get(index).setWords(ch);
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		List<PalabraMap> chList = groups.get(groupPosition).getWords();
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		final PalabraMap palabra = (PalabraMap) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.row, null);
		}
		final ImageButton star = (ImageButton) view.findViewById(R.id.star);
		star.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				star.setSelected(!star.isSelected());
				Palabra orig = palabraDao.get(palabra.getId());
				if (star.isSelected()) {
					orig.setFavorite(true);
				} else {
					orig.setFavorite(false);
				}
				palabraDao.update(orig);
			}
		});
		star.setSelected(palabra.isFavorite());
		TextView orig = (TextView) view.findViewById(R.id.orig);
		orig.setText(palabra.getOrig());
		TextView trad = (TextView) view.findViewById(R.id.trad);
		trad.setText(palabra.getTrad());
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		List<PalabraMap> chList = groups.get(groupPosition).getWords();
		return chList.size();

	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		WordGroup group = (WordGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.group_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.category);
		tv.setText(group.getName());
		return view;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}


