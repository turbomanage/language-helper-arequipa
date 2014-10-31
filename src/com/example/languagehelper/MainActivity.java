package com.example.languagehelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.languagehelper.AddWordDialog.AddWordDialogListener;
import com.example.languagehelper.dao.PageDao;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.WordGroupDao;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener, AddWordDialogListener {

	private static final String TRANSLATIONS_FOLDER = "words";
	private static final String KEY_INIT_LOCALES = "initLocales";
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "activeTab";

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

	public enum Direction {
		TRAD_ON_LEFT, TRAD_ON_RIGHT
	};

	private String[] locales;
	private int selectedLocaleNum;

	static final String ESPAÑOL = "es";

	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Init locales
		initLocale("es");
		this.selectedLocaleNum = readLocales();
		String selectedLocale = this.locales[this.selectedLocaleNum];
		initLocale(selectedLocale);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		// TODO problem: recreates fragments on rotation, so all fragment state
		// is lost
		mSectionsPagerAdapter = new SectionsPagerAdapter(this,
				getSupportFragmentManager(), selectedLocale);

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
						WordsFragment wordsFragment = (WordsFragment) mSectionsPagerAdapter
								.getItem(position);
					}
				});

		drawTabs();
	}

	// For each of the sections in the app, add a tab to the action bar.
	private void drawTabs() {
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_swap:
			ApplicationState.getModel().swapAndNotify();
		case R.id.action_settings:
			return true;
		case R.id.action_add:
			showAddDialog();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
    private void showAddDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddWordDialog dlg = new AddWordDialog();
        dlg.show(fm, "fragment_add_word");
    }

	@Override
	public void onFinishEditDialog(String text) {
		Toast.makeText(this, "new word is " + text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Restore the previously serialized current dropdown position.
		getSupportActionBar().setSelectedNavigationItem(
				getPreferences(0).getInt(STATE_SELECTED_NAVIGATION_ITEM, 0));
	}

	@Override
	public void onPause() {
		super.onPause();
		// Serialize the current dropdown position.
		SharedPreferences prefs = getPreferences(0);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putInt(STATE_SELECTED_NAVIGATION_ITEM,
				getSupportActionBar().getSelectedNavigationIndex());
		prefsEditor.commit();
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

	private List<String> readLines(String filename) {
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

	/**
	 * Initialize the selected locale. If not yet initialized in shared prefs,
	 * read the words for the locale and populate the database.
	 * 
	 * @param pos
	 */
	private void initLocale(String locale) {
		// Populate database for selected language if necessary
		SharedPreferences settings = getPreferences(0);
		String initLocales = settings.getString(KEY_INIT_LOCALES, new String());
		if (!initLocales.contains(locale)) {
			populateDbForLocale(locale);
			initLocales += "," + locale;
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(KEY_INIT_LOCALES, initLocales);
			editor.commit();
		}
	}

	private void populateDbForLocale(String locale) {
		PalabraDao palabraDao = new PalabraDao(this);
		WordGroupDao groupDao = new WordGroupDao(this);
		// poblar base de datos
		try {
			// Get all files in folder for locale
			// TODO check to make sure they're all there
			String path = TRANSLATIONS_FOLDER + "/" + locale;
			String[] files = getResources().getAssets().list(path);
			for (int i = 0; i < files.length; i++) {
				String filename = files[i];
				Log.d(MainActivity.class.getName(), "file: " + filename);
				List<String> lines = readLines(path + "/" + filename);
				// Save title with ordinal 0
				String title = filename.substring(1);
				Page newPage = new Page(i, locale, title);
				long newPageId = new PageDao(this).insert(newPage);
				long newGroupId = -2; // hack, should never occur
				for (int j = 0; j < lines.size(); j++) {
					String line = lines.get(j);
					if (line.startsWith("*")) {
						String groupName = line.substring(1);
						WordGroup newGroup = new WordGroup(groupName, newPageId);
						newGroupId = groupDao.insert(newGroup);
					} else {
						String p = line;
						// Use file numbers to read in order of Classification
						// enum
						Palabra palabra = new Palabra(j, p, newGroupId);
						palabraDao.insert(palabra);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i(WordsFragment.class.getName(), "Database populated");
	}

}