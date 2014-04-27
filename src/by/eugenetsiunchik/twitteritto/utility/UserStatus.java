package by.eugenetsiunchik.twitteritto.utility;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.util.Log;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.models.JSONModel;

public class UserStatus extends JSONModel {

	private static final String NAME = "name";
	private static final String DATE = "created_at";
	private static final String TEXT = "text";
	private static final String PROFILE_IMAGE_URL = "profile_image_url";
	private static final String TWEET_ID = "id";
	private static final String ENTITIES = "entities";
	private static final String MEDIA = "media";
	private static final String USER = "user";
	private static final String MEDIA_URL = "media_url";
	
	
	private static UserStatus model;
	private JSONArray jso;

	public UserStatus(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
	}

	public long getId() {
		try {
			return getLong(TWEET_ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public String getUserName() {
		try {
			return getSubObject(USER).getString(NAME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getText() {
		try {
			return getString(TEXT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getDate() {
		try {
			return getString(DATE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressLint("SimpleDateFormat")
	/*
	 * public Date getCreatedAt() throws ParseException { String stringDate =
	 * mStatus.optString("created_at"); String stringDateFormat =
	 * "EEE MMM dd HH:mm:ss z yyyy"; SimpleDateFormat format = new
	 * SimpleDateFormat(stringDateFormat, Locale.ENGLISH); Date date =
	 * format.parse(stringDate); return date; }
	 */
	public String getImageUrl() {
		try {
			return getSubObject(USER).getString(PROFILE_IMAGE_URL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getMediaUrl() {
		try {
			jso = getSubObject(ENTITIES).getJSONArray(MEDIA);
			Log.i("mm", jso.getJSONObject(0).getString("media_url"));
			return jso.getJSONObject(0).getString(MEDIA_URL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static UserStatus createModel(JSONObject jsonObject)
			throws JSONException {
		return new UserStatus(jsonObject);
	}

	public static ContentValues toValues(JSONObject jso, int typeTweet) throws JSONException {
		ContentValues values = new ContentValues();
		model = createModel(jso);
		values.put(Contract.TweetsColumns.TWEET_ID, model.getId());
		values.put(Contract.TweetsColumns.TWEET_ID, typeTweet);
		values.put(Contract.TweetsColumns.NAME, model.getUserName());
		values.put(Contract.TweetsColumns.DATE, model.getDate());
		values.put(Contract.TweetsColumns.TEXT, model.getText());
		values.put(Contract.TweetsColumns.IMG_URL, model.getImageUrl());
		values.put(Contract.TweetsColumns.STATUS_IMG_URL, model.getMediaUrl());

		return values;
	}

	public static ContentValues[] listToValues(List<UserStatus> list,
			int typeTweet) throws JSONException {
		ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			UserStatus model = list.remove(0);
			values[i] = new ContentValues();

			values[i].put(Contract.TweetsColumns.TWEET_ID, model.getId());
			values[i].put(Contract.TweetsColumns.TWEET_TYPE, typeTweet);
			values[i].put(Contract.TweetsColumns.NAME, model.getUserName());
			values[i].put(Contract.TweetsColumns.DATE, model.getDate());
			values[i].put(Contract.TweetsColumns.TEXT, model.getText());
			values[i].put(Contract.TweetsColumns.IMG_URL, model.getImageUrl());
			values[i].put(Contract.TweetsColumns.STATUS_IMG_URL, model.getMediaUrl());
			//Log.i("Insert to database status ing url", model.getMediaUrl());

		}
		return values;
	}

}
