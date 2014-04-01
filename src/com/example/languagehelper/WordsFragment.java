package com.example.languagehelper;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;

public class WordsFragment extends ListFragment {

	private WordAdapter wordAdapter;
	private LanguageModel model;
	private int tabNum;
	private MainActivity mainActivity;
	
	public WordsFragment() {
		super();
	}
	
	// TODO onAttach or event bus in onCreate to register listener
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// Get locale and classification from Bundle
		String[] originales = readFromDb(Classification.CONNECTOR, MainActivity.ESPAÑOL);
		String[] traducciones = readFromDb(Classification.CONNECTOR, Locale.ENGLISH);
		PalabraMap[] palabras = new PalabraMap[originales.length];
		for (int i = 0; i < originales.length; i++) {
			palabras[i] = new PalabraMap(originales[i], traducciones[i]);
		}
		wordAdapter = new WordAdapter(getActivity(), R.layout.row, palabras);
		setListAdapter(wordAdapter);
		super.onActivityCreated(savedInstanceState);
	}
	
	private String[] readFromDb(Classification c, Locale locale) {
		PalabraDao dao = new PalabraDao(getActivity());
		Palabra exampleQuery = new Palabra();
		exampleQuery.setLocale(locale);
		exampleQuery.setType(c);
		List<Palabra> palabras = dao.load().byExample(exampleQuery).order(Columns.ORD.asc()).list();
		String[] words = new String[palabras.size()];
		int i = 0;
		for (Palabra p : palabras) {
			words[i++] = p.getWord();
		}
		return words;
	}

	public void swapViews() {
		wordAdapter.swapViews();
	}

}