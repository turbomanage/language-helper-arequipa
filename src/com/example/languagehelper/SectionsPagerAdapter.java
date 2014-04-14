package com.example.languagehelper;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.languagehelper.dao.PageDao;
import com.example.languagehelper.dao.PageTable.Columns;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

	private final String locale;
	private final List<Page> pages;

	public SectionsPagerAdapter(Context ctx, FragmentManager fm, String locale) {
		super(fm);
		this.locale = locale;
		PageDao dao = new PageDao(ctx);
		this.pages = dao.load().eq(Columns.LOCALE, locale)
				.order(Columns.PAGENUM.asc()).list();
	}

	@Override
	public Fragment getItem(int position) {
		WordsFragment wordsFragment = new WordsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(WordsFragment.KEY_TAB_NUM, position);
		bundle.putString(WordsFragment.KEY_LOCALE, locale);
		bundle.putLong(WordsFragment.KEY_PAGE_NUM, pages.get(position)
				.getPageNum());
		wordsFragment.setArguments(bundle);
		return wordsFragment;
	}

	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return pages.get(position).getTitle();
	}

}