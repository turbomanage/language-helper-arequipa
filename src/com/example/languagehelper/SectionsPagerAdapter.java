package com.example.languagehelper;

import java.util.List;
import java.util.Locale;

import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

	// Save state so we can swap views
	// TODO change to listener or event bus
	private final WordsFragment[] fragments;
	private final String locale;
	private final List<Palabra> titles;

	public SectionsPagerAdapter(Context ctx, FragmentManager fm, String locale) {
		super(fm);
		this.locale = locale;
		this.fragments = new WordsFragment[Classification.values().length];
		PalabraDao dao = new PalabraDao(ctx);
		titles = dao.load().eq(Columns.ORD, 0).eq(Columns.LOCALE, locale).order(Columns._id.asc()).list();
	}

	@Override
	public Fragment getItem(int position) {
		if (this.fragments[position] == null) {
			WordsFragment wordsFragment = new WordsFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(WordsFragment.KEY_TAB_NUM, position);
			bundle.putString(WordsFragment.KEY_LOCALE, locale);
			wordsFragment.setArguments(bundle);
			this.fragments[position] = wordsFragment;
		}
		return this.fragments[position];
	}

	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles.get(position).getWord();
	}
	
}