package com.example.languagehelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class ConectoresFragment extends ListFragment {

	private WordAdapter wordAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String[] originales = readWords("originales.txt");
		String[] traducciones = readWords("palabras-en.txt");
		Palabra[] palabras = new Palabra[originales.length];
		for (int i = 0; i < originales.length; i++) {
			palabras[i] = new Palabra(originales[i], traducciones[i]);
		}
		wordAdapter = new WordAdapter(getActivity(), R.layout.row, palabras);
		setListAdapter(wordAdapter);
		super.onActivityCreated(savedInstanceState);
	}

	private String[] readWords(String filename) {
		ArrayList<String> words = new ArrayList<String>();
		try {
			InputStream is = getResources().getAssets().open(filename);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(isr);
			// primera pasada atrás del ciclo
			String word = bufferedReader.readLine();
			while (word != null) {
				words.add(word);
				word = bufferedReader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words.toArray(new String[words.size()]);
	}

	public void swapViews() {
		wordAdapter.swapViews();
	}

}
