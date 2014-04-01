package com.example.languagehelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable.Columns;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener, OnItemSelectedListener, LanguageModel {

	private static final String TRANSLATIONS_FOLDER = "words";

	private static final String KEY_INIT_LOCALES = "initLocales";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private String[] locales;
	private int selectedLocaleNum;

	static final Locale ESPAÑOL = new Locale("es");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Init locales
		initLocale("es");
		this.selectedLocaleNum = readLocales();
		initLocale(this.locales[this.selectedLocaleNum]);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				this, getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// TODO extract method
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	private int readLocales() {
		String defaultLocale = Locale.getDefault().getLanguage();
		int defaultPos = 0;
		try {
			// traverse directories
			locales = getAssets().list(TRANSLATIONS_FOLDER);
			for (int i = 0; i < locales.length; i++) {
				String lc = locales[i];
				if (lc.equals(defaultLocale)) {
					defaultPos = i;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return defaultPos;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem spinnerItem = menu.findItem(R.id.language_spinner);
		Spinner spinner = (Spinner) spinnerItem.getActionView();
		spinner.setOnItemSelectedListener(this);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.addAll(getDisplayLanguages());
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setSelection(this.selectedLocaleNum);
		return true;
	}

	private Collection<? extends String> getDisplayLanguages() {
		ArrayList<String> displayLanguages = new ArrayList<String>();
		for (String lc : locales) {
			String displayLanguage = new Locale(lc).getDisplayLanguage();
			displayLanguages.add(displayLanguage);
		}
		return displayLanguages;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_swap:
			Log.i("MainActivity", "button pressed");
			// TODO notify observers
			WordsFragment frag = (WordsFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
			frag.swapViews();
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	private List<String> readWords(String filename) {
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
			e.printStackTrace();
		}
		return words;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 * 
	 * Language spinner
	 */
	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int pos,
			long id) {
//		String itemAtPosition = (String) adapterView.getItemAtPosition(pos);
		initLocale(locales[pos]);
		selectedLocaleNum = pos;
		// TODO just replace the whole section pager adapter, but keep current tab selected
		mSectionsPagerAdapter.notifyDataSetChanged();
	}

	/**
	 * Initialize the selected locale. If not yet initialized in shared prefs,
	 * read the words for the locale and populate the database.
	 * 
	 * @param pos
	 */
	private void initLocale(String selectedLocale) {
		// Populate database for selected language if necessary
		SharedPreferences settings = getPreferences(0);
		String initLocales = settings.getString(KEY_INIT_LOCALES, new String());
		if (!initLocales.contains(selectedLocale)) {
			populateDbForLocale(selectedLocale);
			initLocales += "," + selectedLocale;
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(KEY_INIT_LOCALES, initLocales);
			editor.commit();
		}
	}

	private void populateDbForLocale(String selectedLocale) {
		PalabraDao dao = new PalabraDao(this);
		// poblar base de datos
		try {
			// Get all files in folder for locale
			// TODO check to make sure they're all there
			String path = TRANSLATIONS_FOLDER + "/" + selectedLocale;
			String[] files = getResources().getAssets().list(path);
			for (int i = 0; i < files.length; i++) {
				String filename = files[i];
				Log.d(MainActivity.class.getName(), "file: " + filename);
				List<String> palabras = readWords(path + "/" + filename);
				// Save title with ordinal 0
				Palabra title = new Palabra(0, filename.substring(1), new Locale(selectedLocale), Classification.TITLE);
				dao.insert(title);
				for (int j = 0; j < palabras.size(); j++) {
					String p = palabras.get(j);
					// Use file numbers to read in order of Classification enum
					Palabra palabra = new Palabra(j+1, p, new Locale(selectedLocale),
							Classification.values()[i+1]);
					dao.insert(palabra);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i(WordsFragment.class.getName(), "Database populated");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSelectedLocale() {
		return this.locales[this.selectedLocaleNum];
	}

	@Override
	public Classification getClassificationForTab(int ord) {
		// TODO Allow user ordering
		return Classification.values()[ord + 1];
	}

	@Override
	public int getNumTabs() {
		return Classification.values().length - 1; // omit title
	}

	@Override
	public String getTitleForTab(int ord) {
		PalabraDao dao = new PalabraDao(this);
		List<Palabra> types = dao.load().eq(Columns.ORD, 0).order(Columns.TYPE.asc()).list();
		return types.get(ord).getWord();
	}

}