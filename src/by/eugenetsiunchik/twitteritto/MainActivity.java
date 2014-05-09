package by.eugenetsiunchik.twitteritto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import by.eugenetsiunchik.twitteritto.adapters.pager.TPagerAdapter;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.oauth.OAUTH;
import by.eugenetsiunchik.twitteritto.utility.Constants;

public class MainActivity extends FragmentActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private TPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	private SharedPreferences mSettings;
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgressBar = (ProgressBar) findViewById(R.id.feedProgressBar);
		mProgressBar.setVisibility(View.VISIBLE);
		// primary sections of the app.
		mPagerAdapter = new TPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				InputMethodManager imgr = null;
				Log.i(TAG, "onPageSelected " + arg0);
				if (arg0 == 1) {
					imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				Log.i(TAG, "onPageScrolled " + arg0 + " " + arg1 + " " + arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				Log.i(TAG, "onPageScrollStateChanged " + arg0);

			}
		});
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.

		ContextHolder.getInstance().setContext(getApplicationContext());

		// new TimelineTask(HttpManagering.getInstance().getClient(), Consumer
		// .getInstance().getConsumer(mSettings), FeedFragment.getFirstID())
		// .execute(new TimelineSelector(Constants.HOME_TIMELINE_URL_STRING));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.autorization:
			startActivity(new Intent(getApplicationContext(), StartActivity.class));
			return true;
		case R.id.exit:
			removeTokens();
			startActivity(new Intent(getApplicationContext(), StartActivity.class));
			new Thread(new Runnable() {

				@Override
				public void run() {
					getApplicationContext().getContentResolver().delete(Contract.TweetsColumns.HOME_URI, null, null);

				}
			}).start();
			return true;

		case R.id.help:
			new Thread(new Runnable() {

				@Override
				public void run() {
					getApplicationContext().getContentResolver().delete(Contract.TweetsColumns.HOME_URI, null, null);

				}
			}).start();
			Toast.makeText(getApplicationContext(), "Delete DataBases", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void removeTokens() {
		mSettings = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = mSettings.edit();
		editor.remove(OAUTH.USER_TOKEN);
		editor.remove(OAUTH.USER_SECRET);
		editor.commit();

		Log.i(TAG, "PREFS CLEAN ");
		Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();

	}

}
