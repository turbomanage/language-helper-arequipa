package com.example.languagehelper;

import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.example.languagehelper.MainActivity.Direction;
import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;

public class WordsFragment extends ListFragment {

	public static final String KEY_TAB_NUM = "tabNum";
	public static final String KEY_LOCALE = "locale";
	
	private WordAdapter wordAdapter;
	private int tabNum;
	private MainActivity mainActivity;
	private String locale;
	
	public WordsFragment() {
		super();
	}
	
	// TODO onAttach or event bus in onCreate to register listener
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.d("wordFragment", "onResume");
		super.onResume();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		tabNum = getArguments().getInt(KEY_TAB_NUM);
		locale = getArguments().getString(KEY_LOCALE);
		// Get locale and classification from Bundle
		Classification tabClassification = Classification.values()[tabNum];
		String[] originales = readFromDb(tabClassification, MainActivity.ESPAÑOL);
		String[] traducciones = readFromDb(tabClassification, locale);
		PalabraMap[] palabras = new PalabraMap[originales.length];
		for (int i = 0; i < originales.length; i++) {
			palabras[i] = new PalabraMap(originales[i], traducciones[i]);
		}
		wordAdapter = new WordAdapter(getActivity(), R.layout.row, palabras);
		setListAdapter(wordAdapter);
		super.onActivityCreated(savedInstanceState);
	}
	
	private String[] readFromDb(Classification c, String locale) {
		PalabraDao dao = new PalabraDao(getActivity());
		Palabra exampleQuery = new Palabra();
		exampleQuery.setLocale(locale);
		exampleQuery.setType(c);
		List<Palabra> palabras = dao.load().byExample(exampleQuery).order(Columns.ORD.asc()).list();
		String[] words = new String[palabras.size() - 1];
		// skip title
		for (int j = 1; j < palabras.size(); j++) {
			String word = palabras.get(j).getWord();
			words[j-1] = word;
		}
		return words;
	}

	public void setDirection(Direction selectedDirection) {
		wordAdapter.setDirection(selectedDirection);
	}

}