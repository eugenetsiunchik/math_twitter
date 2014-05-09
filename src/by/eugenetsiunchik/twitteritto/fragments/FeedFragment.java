package by.eugenetsiunchik.twitteritto.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import by.eugenetsiunchik.twitteritto.R;
import by.eugenetsiunchik.twitteritto.adapters.feed.TweetsFeedCursorAdapter;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.http.HttpManager;
import by.eugenetsiunchik.twitteritto.oauth.Consumer;
import by.eugenetsiunchik.twitteritto.tasks.TimelineTask;
import by.eugenetsiunchik.twitteritto.utility.Constants;
import by.eugenetsiunchik.twitteritto.utility.PullToRefresh;
import by.eugenetsiunchik.twitteritto.utility.PullToRefresh.OnRefreshListener;
import by.eugenetsiunchik.twitteritto.utility.TimelineSelector;

public class FeedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnScrollListener {

	private static final int MARKER_FEED = 0;
	private static final String MARKER_TYPE = "0";
	private static final String ID = Contract.TweetsColumns.TWEET_ID;
	private static final String TYPE = Contract.TweetsColumns.TWEET_TYPE;
	private static final String startParametrSelection = TYPE + " = ?";
	private static final String sortOrder = ID + " DESC LIMIT ";
	private static final int countTweetParametr = 20;
	private static Integer countTweet = 40;
	private static String firstID = null;
	private String lastId = null;
	private String TAG = getClass().getSimpleName();
	private TweetsFeedCursorAdapter mTweetsFeedCursorAdater;
	private SharedPreferences mSettings;
	private String firstId;
	private Loader<Cursor> loader;
	private int count;
	private int loadMore;
	private ProgressBar progressBar;
	private static final Integer countTweetConst = 40;
	private static final String URL = Constants.HOME_TIMELINE_URL_STRING;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed, container, false);
		Log.i(TAG, "Fragment Created");
		mSettings = getActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

		final PullToRefresh feedList = (PullToRefresh) rootView.findViewById(R.id.listview);
		progressBar = (ProgressBar) rootView.findViewById(R.id.feedProgressBar);
		progressBar.setVisibility(View.VISIBLE);
		// feedList.addFooterView(nextPart);

		Log.i(TAG, "Task complete");
		mTweetsFeedCursorAdater = new TweetsFeedCursorAdapter(getActivity(), null, true);
		feedList.setOnScrollListener(this);
		feedList.setAdapter(mTweetsFeedCursorAdater);
		feedList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						new TimelineTask(HttpManager.getInstance().getClient(), Consumer.getInstance().getConsumer(),
								MARKER_FEED, feedList).execute(new TimelineSelector(URL, getFirstID(), null,
								countTweetConst, null));

					}

				}).start();
			}

		});
		progressBar.setVisibility(View.VISIBLE);
		getLoaderManager().initLoader(0, null, this);

		// setRetainInstance(true);

		return rootView;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		Log.i(TAG, "onCreateLoader: ");
		return new CursorLoader(getActivity(), Contract.TweetsColumns.HOME_URI, null, startParametrSelection,
				new String[] { MARKER_TYPE }, sortOrder + countTweet);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if ((count = cursor.getCount()) != 0 && count < countTweetParametr) {

			mTweetsFeedCursorAdater.swapCursor(cursor);
			return;
		}

		if (cursor.moveToLast()) {
			lastId = cursor.getString(cursor.getColumnIndex(Contract.TweetsColumns.TWEET_ID));

			Log.i(TAG, "last id " + lastId);

		}

		if (cursor.moveToFirst()) {

			firstId = cursor.getString(cursor.getColumnIndex(Contract.TweetsColumns.TWEET_ID));
			setFirstID(firstId);
			Log.d(TAG, "Cursor Loader Finished " + firstId);

		}
		if ((count = cursor.getCount()) != countTweet && lastId != null) {// load
																			// timeline
																			// if
																			// db
																			// have
																			// just
																			// part

			new TimelineTask(HttpManager.getInstance().getClient(), Consumer.getInstance().getConsumer(mSettings),
					MARKER_FEED, null).execute(new TimelineSelector(URL, null, lastId, countTweetConst, null));

		}

		if (cursor.getCount() == 0 && lastId == null) {// load if db empty
			Log.d(TAG, "onLoadFinished() Empty cursor");

			new TimelineTask(HttpManager.getInstance().getClient(), Consumer.getInstance().getConsumer(mSettings),
					MARKER_FEED, null).execute(new TimelineSelector(URL, null, lastId, countTweetConst, null));

		}
		progressBar.setVisibility(View.GONE);
		mTweetsFeedCursorAdater.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		progressBar.setVisibility(View.VISIBLE);
		mTweetsFeedCursorAdater.swapCursor(null);
	}

	public static String getFirstID() {

		return firstID;

	}

	private void setFirstID(String firstId) {

		firstID = firstId;

		Log.d(TAG, "firstID " + firstID);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// Lazy loading
		Log.d("onScroll ", firstVisibleItem + "-" + visibleItemCount + "-" + totalItemCount);
		loadMore = firstVisibleItem + visibleItemCount + 5;
		if (loadMore >= totalItemCount && totalItemCount != 0) {
			loader = getLoaderManager().getLoader(0);

			countTweet = countTweet + 40;
			((CursorLoader) loader).setSortOrder(sortOrder + countTweet);

			loader.forceLoad();

			loadMore = 0;
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}
