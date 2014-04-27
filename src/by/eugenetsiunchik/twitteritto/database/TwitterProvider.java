 package by.eugenetsiunchik.twitteritto.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class TwitterProvider extends ContentProvider {

	DbHelper dbHelper;


	private static final int HOME_FEED = 0;

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);


	private static final String TAG = TwitterProvider.class.getSimpleName();

	static {

		sURIMatcher.addURI(Contract.AUTHORITY,
				Contract.TweetsColumns.HOME_PATH, HOME_FEED);
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delete = 0;
        switch (sURIMatcher.match(uri)) {
        case HOME_FEED:

    			Log.i(TAG, "delete (Provider) " + selection);
                delete = dbHelper.deleteTweet(selection, selectionArgs);
        		//getContext().getContentResolver().notifyChange(uri, null);
                break;
               default: throw new SQLException("Failed to delete row into " + uri);
        }
        return delete;
}

	@Override
	public String getType(Uri uri) {
		switch (sURIMatcher.match(uri)) {
		case HOME_FEED:
			return Contract.TweetsColumns.HOME_TYPE;
		
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri _uri = null;
		long id = 0;
		switch (sURIMatcher.match(uri)) {
		case HOME_FEED:
			id = dbHelper.addTweet(values);

			_uri = ContentUris.withAppendedId(
					Contract.TweetsColumns.HOME_URI, id);
			getContext().getContentResolver().notifyChange(_uri, null);
		break;
		default:
			throw new SQLException("Failed to insert row into " + uri);
		}
		return _uri;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		Log.i(TAG, "bulk insert (Provider cheked) " + values[0]);
		DbHelper.bulkInsert(dbHelper, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return values.length;
		
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DbHelper(getContext(), null, null, 0);
		if (dbHelper != null) {
			return true;
		}
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		switch (sURIMatcher.match(uri)) {
		case HOME_FEED:
			cursor = dbHelper.getTweets(projection,selection,
					 selectionArgs, sortOrder);			
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			break;
		
		default:
			throw new SQLException("Failed to insert row into " + uri);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
