package com.example.languagehelper;

import java.util.Arrays;
import java.util.Comparator;

import com.example.languagehelper.MainActivity.Direction;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WordAdapter extends ArrayAdapter<PalabraMap> {

	private Context context;
	private PalabraMap[] byOrig, byTrad;
	private Direction dir = Direction.LEFT;

	public WordAdapter(Context context, int resource, PalabraMap[] objects) {
		super(context, resource, objects);
		this.context = context;
		this.byOrig = objects.clone();
		Arrays.sort(byOrig, sortByOrig);
		this.byTrad = objects.clone();
		Arrays.sort(byTrad, sortByTrad);
	}

	private Comparator<PalabraMap> sortByOrig = new Comparator<PalabraMap>() {
		@Override
		public int compare(PalabraMap lhs, PalabraMap rhs) {
			return lhs.getOrig().compareTo(rhs.getOrig());
		}
	};

	private Comparator<PalabraMap> sortByTrad = new Comparator<PalabraMap>() {
		@Override
		public int compare(PalabraMap lhs, PalabraMap rhs) {
			return lhs.getTrad().compareTo(rhs.getTrad());
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row, null);
		TextView orig = (TextView) rowView.findViewById(R.id.orig);
		TextView trad = (TextView) rowView.findViewById(R.id.trad);
		if (this.dir == Direction.LEFT) {
			orig.setText(byOrig[position].getOrig());
			trad.setText(byOrig[position].getTrad());
		} else {
			orig.setText(byTrad[position].getTrad());
			trad.setText(byTrad[position].getOrig());
		}
		return rowView;
	}

	public void setDirection(Direction selectedDirection) {
		this.dir = selectedDirection;
		this.notifyDataSetChanged();
	}
}
