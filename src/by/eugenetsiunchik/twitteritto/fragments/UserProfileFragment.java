package by.eugenetsiunchik.twitteritto.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import by.eugenetsiunchik.twitteritto.R;
import by.eugenetsiunchik.twitteritto.adapters.feed.TweetsFeedCursorAdapter;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.http.HttpManager;
import by.eugenetsiunchik.twitteritto.http.HttpManagering;
import by.eugenetsiunchik.twitteritto.image.ImageLoader;
import by.eugenetsiunchik.twitteritto.oauth.Consumer;
import by.eugenetsiunchik.twitteritto.tasks.CredentialsStorage;
import by.eugenetsiunchik.twitteritto.tasks.DeleteTask;
import by.eugenetsiunchik.twitteritto.tasks.TimelineTask;
import by.eugenetsiunchik.twitteritto.utility.Constants;
import by.eugenetsiunchik.twitteritto.utility.SwipeDismissListViewTouchListener;
import by.eugenetsiunchik.twitteritto.utility.TimelineSelector;

public class UserProfileFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>, OnScrollListener {

	private static final String TAG = UserProfileFragment.class.getSimpleName();
	private TweetsFeedCursorAdapter mTweetsFeedCursorAdater;
	private ListView userTimeline;
	private static final int MARKER_FEED = 1;
	private static final String MARKER_TYPE = "1";
	private static final String ID = Contract.TweetsColumns.TWEET_ID;
	private static final String TYPE = Contract.TweetsColumns.TWEET_TYPE;
	private static final String startParametrSelection = TYPE + " = ?";
	private static final String sortOrder = ID + " DESC LIMIT ";
	private static final int countTweetParametr = 20;
	private static Integer countTweet = 40;
	private String lastId = null;
	private int count;
	private String firstId;
	private String URL = Constants.USER_TIMELINE_URL_STRING;
	private Integer countTweetConst = 40;
	private CredentialsStorage cSI;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new TimelineTask(HttpManager.getInstance().getClient(), Consumer
				.getInstance().getConsumer(), MARKER_FEED, null)
				.execute(new TimelineSelector(URL, getFirstId(), null, null, null));
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_user_profile, container, false);
		cSI = CredentialsStorage.getInstance();
		String urlImg = cSI.getImageUrl();
		String nameProfile = cSI.getName();
		String userName = cSI.getProfile();
		String profileDescription = cSI.getDescription();
		String profileFriendsCount = cSI.getFriendsCount();
		String profileFollowersCount = cSI.getFollowersCount();
		String profileStatusesCount = cSI.getStatusesCount();

		ImageView img = (ImageView) rootView.findViewById(R.id.profileIcon);
		TextView userNameTextView = (TextView) rootView
				.findViewById(R.id.userFullName);
		TextView profileNameTextView = (TextView) rootView
				.findViewById(R.id.profileName);
		TextView profileStatusesTextView = (TextView) rootView
				.findViewById(R.id.profileStatusesCount);
		TextView profileFriendsTextView = (TextView) rootView
				.findViewById(R.id.profileFriendsCount);
		TextView profileFollowersTextView = (TextView) rootView
				.findViewById(R.id.profileFollowersCount);
		TextView profileDescriptionTextView = (TextView) rootView
				.findViewById(R.id.profileDescription);

		userTimeline = (ListView) rootView.findViewById(R.id.userTimeline);

		mTweetsFeedCursorAdater = new TweetsFeedCursorAdapter(getActivity(),
				null, true);

		ImageLoader.getInstance().bind(urlImg, img);
		userNameTextView.setText(nameProfile);
		profileNameTextView.setText(userName);
		profileStatusesTextView.setText(profileStatusesCount);
		profileDescriptionTextView.setText(profileDescription);
		profileFollowersTextView.setText(profileFollowersCount);
		profileFriendsTextView.setText(profileFriendsCount);

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				userTimeline,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					private String id;

					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							
							Cursor cursor = (Cursor) mTweetsFeedCursorAdater
									.getItem(position);
							id = cursor.getString(cursor
									.getColumnIndex(Contract.TweetsColumns.TWEET_ID));
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
							 
					        // Setting Dialog Title
					        alertDialog.setTitle("Confirm Delete...");
					 
					        // Setting Dialog Message
					        alertDialog.setMessage("Delete message?");
					 
					        // Setting Icon to Dialog
					        //alertDialog.setIcon(R.drawable.delete);
					 
					        // Setting Positive "Yes" Button
					        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog,int which) {
					 
					            	new DeleteTask(getActivity(), HttpManagering
											.getInstance().getClient(), Consumer.getInstance().getConsumer())
											.execute(Long.parseLong(id));}
					        });
					 
					        // Setting Negative "NO" Button
					        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					             dialog.cancel();
					            }
					        });
					 
					        // Showing Alert Message
					        alertDialog.show();
							
							
							//cursor.requery();
							
						}
						//mTweetsFeedCursorAdater.notifyDataSetChanged();
					}
				});
		userTimeline.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		userTimeline.setOnScrollListener(touchListener.makeScrollListener());

		userTimeline.setAdapter(mTweetsFeedCursorAdater);
		getLoaderManager().initLoader(1, null, this);
		
		
		return rootView;

	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Log.i(TAG, "onCreateLoader: ");
		return new CursorLoader(getActivity(), Contract.TweetsColumns.HOME_URI,
				null, startParametrSelection, new String[] { MARKER_TYPE },
				sortOrder + countTweet);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if ((count = cursor.getCount()) != 0
				&& count < (countTweetParametr - 1)) {

			mTweetsFeedCursorAdater.swapCursor(cursor);
			return;

		}

		if (cursor.moveToLast())
			lastId = cursor.getString(cursor
					.getColumnIndex(Contract.TweetsColumns.TWEET_ID));
		Log.i(TAG, "last id " + lastId);
		
		if (cursor.moveToFirst()) {
			firstId = cursor.getString(cursor
					.getColumnIndex(Contract.TweetsColumns.TWEET_ID));
			setFirstID(firstId);
			

			Log.d(TAG, "Cursor Loader Finished " + firstId);

		}

		if ((count = cursor.getCount()) != countTweet && lastId != null) {

			//new TimelineTask(HttpManager.getInstance().getClient(), Consumer
				//	.getInstance().getConsumer(), MARKER_FEED, null)
					//.execute(new TimelineSelector(URL, null, lastId,
						//	countTweetConst, null));

		}

		if (cursor.getCount() == 0 && lastId == null) {
			Log.d(TAG, "onLoadFinished() Empty cursor");

			new TimelineTask(HttpManager.getInstance().getClient(), Consumer
					.getInstance().getConsumer(), MARKER_FEED, null)
					.execute(new TimelineSelector(URL, null, null, countTweetConst, null));
		}

		mTweetsFeedCursorAdater.swapCursor(cursor);

	}

	private void setFirstID(String firstId) {

		this.firstId = firstId;

	}
	
	private String getFirstId(){
		return firstId;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
		mTweetsFeedCursorAdater.swapCursor(null);

	}

}
