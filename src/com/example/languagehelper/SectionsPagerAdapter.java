package com.example.languagehelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

	private final LanguageModel model;
	// Save state so we can swap views
	// TODO change to listener or event bus
	private final WordsFragment[] fragments;

	public SectionsPagerAdapter(LanguageModel model, FragmentManager fm) {
		// TODO pass selectedLocale instead
		// TODO put title query here
		super(fm);
		this.model = model;
		this.fragments = new WordsFragment[model.getNumTabs()];
	}

	@Override
	public Fragment getItem(int position) {
		if (this.fragments[position] == null) {
			this.fragments[position] = new WordsFragment();
			// TODO pass args
		}
		return this.fragments[position];
	}

	@Override
	public int getCount() {
		return model.getNumTabs();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return model.getTitleForTab(position);
		// TODO return title from this
	}
	
}