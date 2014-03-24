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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.languagehelper.Palabra.Classification;
import com.example.languagehelper.dao.PalabraDao;
import com.example.languagehelper.dao.PalabraTable;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private static final String IS_INITIALIZED = "isInitialized";

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
	ConectoresFragment conectoresFragment;
	ConectoresFragment preposicionesFragment;
	ConectoresFragment verbosFragment;

	static final Locale ESPAÑOL = new Locale("es");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Initialize the database on first run
		SharedPreferences settings = getPreferences(0);
		if (!settings.getBoolean(IS_INITIALIZED, false)) {
			populateDb();
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(IS_INITIALIZED, true);
			editor.commit();
		}

		conectoresFragment = new ConectoresFragment();
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

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

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem spinnerItem = menu.findItem(R.id.language_spinner);
		Spinner spinner = (Spinner) spinnerItem.getActionView();
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.actionbar_spinner_item);
		adapter.addAll(getLocalesFromDb());
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		return true;
	}

	private List<String> getLocalesFromDb() {
		List<String> locales = new ArrayList<String>();
		PalabraDao dao = new PalabraDao(this);
		SQLiteDatabase db = dao.getDbHelper(this).getReadableDatabase();
		// SELECT DISTINCT Locale FROM PALABRA
		Cursor c = db.query(true, "Palabra", new String[]{"Locale"}, null, null, null, null, null, null);
		for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext()) {
			String localeStr = c.getString(0);
			Log.d(MainActivity.class.getName(), "locale: " + localeStr);
			locales.add(new Locale(localeStr).getDisplayLanguage());
		}
		c.close();
		return locales;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_swap:
			Log.i("MainActivity", "button pressed");
			conectoresFragment.swapViews();
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

	private void populateDb() {
		PalabraDao dao = new PalabraDao(this);
		// poblar base de datos
		try {
			String[] files = getResources().getAssets().list("");
			for (int i = 0; i < files.length; i++) {
				String filename = files[i];
				if (filename.startsWith("palabras")) {
					Log.d(MainActivity.class.getName(), "file: " + filename);
					String[] split = filename.split("\\.");
					// Remove "palabras-"
					String localeStr = split[0].substring(9);
					List<String> palabras = readWords(filename);
					for (int j = 0; j < palabras.size(); j++) {
						String p = palabras.get(j);
						Palabra palabra = new Palabra(j, p, new Locale(localeStr),
								Classification.CONNECTOR);
						dao.insert(palabra);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i(ConectoresFragment.class.getName(), "Database populated");
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			// return PlaceholderFragment.newInstance(position + 1);
			switch (position) {
			case 0:
				conectoresFragment = new ConectoresFragment();
				return conectoresFragment;
			case 1:
				preposicionesFragment = new ConectoresFragment();
				return preposicionesFragment;
			case 2:
				verbosFragment = new ConectoresFragment();
				return verbosFragment;
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
