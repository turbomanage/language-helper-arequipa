package com.example.languagehelper;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class ConectoresFragment extends ListFragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String[] originales = this.getActivity().getResources()
				.getStringArray(R.array.conectores_originales);
		String[] traducciones = this.getActivity().getResources()
				.getStringArray(R.array.conectores);
		Palabra[] palabras = new Palabra[originales.length];
		for (int i = 0; i < originales.length; i++) {
			palabras[i] = new Palabra(originales[i], traducciones[i]);
		}
		WordAdapter wordAdapter = new WordAdapter(getActivity(), R.layout.row, palabras);
		setListAdapter(wordAdapter);
		super.onActivityCreated(savedInstanceState);
	}
	
}
