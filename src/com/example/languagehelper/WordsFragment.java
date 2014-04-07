package com.example.languagehelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;

import com.example.languagehelper.MainActivity.Direction;
import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;

public class WordsFragment extends ExpandableListFragment {

	public static final String KEY_TAB_NUM = "tabNum";
	public static final String KEY_LOCALE = "locale";
	
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
		PalabraMap[] palabras = mergePalabrasFromDb(tabClassification, locale);
		WordGroup group = new WordGroup("group1");
		group.setWords(Arrays.asList(palabras));
		List<WordGroup> groups = new ArrayList<WordGroup>();
		groups.add(group);
		wordAdapter = new ExpandableListAdapter(getActivity(), groups);
		setListAdapter(wordAdapter);
		super.onActivityCreated(savedInstanceState);
	}
	
	private PalabraMap[] mergePalabrasFromDb(Classification c,
			String locale) {
		PalabraDao dao = new PalabraDao(getActivity());
		Palabra origQuery = new Palabra();
		origQuery.setLocale(MainActivity.ESPAÑOL);
		origQuery.setType(c);
		List<Palabra> origPalabras = dao.load().byExample(origQuery).order(Columns.ORD.asc()).list();
		Palabra tradQuery = new Palabra();
		tradQuery.setLocale(locale);
		tradQuery.setType(c);
		List<Palabra> tradPalabras = dao.load().byExample(tradQuery).order(Columns.ORD.asc()).list();
		PalabraMap[] merged = new PalabraMap[origPalabras.size() - 1];
		// skip title
		for (int j = 1; j < origPalabras.size(); j++) {
			long id = origPalabras.get(j - 1).getId();
			String orig = origPalabras.get(j - 1).getWord();
			String trad = tradPalabras.get(j - 1).getWord();
			boolean isFavorite = origPalabras.get(j - 1).isFavorite();
			merged[j - 1] = new PalabraMap(id, orig, trad, isFavorite);
		}
		return merged;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// TODO save state of the expanded groups
	}
	
	public void setDirection(Direction selectedDirection) {
//		wordAdapter.setDirection(selectedDirection);
	}

}