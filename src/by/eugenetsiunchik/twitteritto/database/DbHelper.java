package by.eugenetsiunchik.twitteritto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	public static final int DB_VERSION = 1;
	private static final String DB_NAME = "etv_twitter.data.db";
	public static final String TWEET_FEED_TABLE = "ETV_TWITTER_FEED_TABLE";

	public static final String[] TWEETS_COLUMNS = {
			Contract.TweetsColumns.TWEET_ID, Contract.TweetsColumns.TWEET_TYPE,
			Contract.TweetsColumns.NAME, Contract.TweetsColumns.DATE,
			Contract.TweetsColumns.TEXT, Contract.TweetsColumns.IMG_URL,
			Contract.TweetsColumns.STATUS_IMG_URL };

	public static final int TWEET_ID_COL_INDEX = 0;
	public static final int TWEET_TYPE_COL_INDEX = 1;
	public static final int TWEET_USER_NAME_COL_INDEX = 2;
	public static final int DATE_COL_INDEX = 3;
	public static final int TEXT_COL_INDEX = 4;
	public static final int IMG_URL_COL_INDEX = 5;
	public static final int STATUS_IMG_URL_COL_INDEX = 6;

	public static final String CREATE_TWEETS_TABLE = "CREATE TABLE "
			+ TWEET_FEED_TABLE + " (" + TWEETS_COLUMNS[TWEET_ID_COL_INDEX]
			+ " INTEGER PRIMARY KEY, " + TWEETS_COLUMNS[TWEET_TYPE_COL_INDEX]
			+ " INTEGER, " + TWEETS_COLUMNS[TWEET_USER_NAME_COL_INDEX]
			+ " VARCHAR NOT NULL, " + TWEETS_COLUMNS[DATE_COL_INDEX]
			+ " VARCHAR, " + TWEETS_COLUMNS[TEXT_COL_INDEX] + " VARCHAR, "
			+ TWEETS_COLUMNS[IMG_URL_COL_INDEX] + " VARCHAR, "
			+ TWEETS_COLUMNS[STATUS_IMG_URL_COL_INDEX] + " VARCHAR)";
	public static final String DROP_TWEET_TABLE = "DROP TABLE IF EXISTS "
			+ TWEET_FEED_TABLE;

	private static final String TAG = DbHelper.class.getSimpleName();

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, DB_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL(CREATE_TWEETS_TABLE);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			Log.d(TAG, CREATE_TWEETS_TABLE);
		}

	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, int oldVersion,
			int newVersion) {
		try {
			db.beginTransaction();
			db.execSQL(DROP_TWEET_TABLE);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		onCreate(db);
	}

	public long addTweet(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		Log.i(TAG, values.toString());
		return db.insert(TWEET_FEED_TABLE, null, values);
	}
	
	public int deleteTweet(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int delete = 0;
		try {
			db.beginTransaction();
			delete  = db.delete(TWEET_FEED_TABLE, whereClause, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return delete;
	}

	public Cursor getTweets(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = getWritableDatabase();

		Cursor cursor = db.query(TWEET_FEED_TABLE, projection, selection,
				selectionArgs, null, null, sortOrder);
		return cursor;
	}

	public Cursor getTweets() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(TWEET_FEED_TABLE, null, null, null, null,
				null, null);

		return cursor;
	}

	public int deleteItem(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int delete = 0;
		try {
			db.beginTransaction();
			delete = db.delete(table, whereClause, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return delete;
	}

	public static void bulkInsert(DbHelper dbHelper, ContentValues[] values) {
		SQLiteDatabase sqld = dbHelper.getWritableDatabase();
		sqld.beginTransaction();
		try {
			Log.i(TAG, "bulkinsert (dbHelper cheked)" + values[0]);
			for (int i = 0; i < values.length; i++) {
				sqld.insert(TWEET_FEED_TABLE, null, values[i]);
			}
			sqld.setTransactionSuccessful();
		} finally {
			Log.i(TAG, "bulkinsert (dbHelper cheked) Transaction complete");

			sqld.endTransaction();
		}

	}
}
