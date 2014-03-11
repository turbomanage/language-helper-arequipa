package com.example.languagehelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WordAdapter extends ArrayAdapter<Palabra> {

	private Context context;
	private Palabra[] palabras;

	public WordAdapter(Context context, int resource, Palabra[] objects) {
		super(context, resource, objects);
		this.context = context;
		this.palabras = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row, null);
		TextView orig = (TextView) rowView.findViewById(R.id.orig);
		TextView trad = (TextView) rowView.findViewById(R.id.trad);
		orig.setText(palabras[position].getOrig());
		trad.setText(palabras[position].getTrad());
		return rowView;
	}
}
