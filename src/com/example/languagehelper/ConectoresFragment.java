package com.example.languagehelper;

import java.util.List;
import java.util.Locale;

import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class ConectoresFragment extends ListFragment {

	private WordAdapter wordAdapter;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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
		List<Palabra> palabras = dao.listByExample(exampleQuery);
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
