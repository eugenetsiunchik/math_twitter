package by.eugenetsiunchik.twitteritto.tasks;

import java.io.IOException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import by.eugenetsiunchik.twitteritto.MainActivity;
import by.eugenetsiunchik.twitteritto.utility.Constants;
import by.eugenetsiunchik.twitteritto.utility.UserStatus;

public class CredentialsTask extends AsyncTask<Void, Void, JSONObject> {

	private static final String PROFILE_IMG_URL = "profile_image_url";
	private static final String NAME = "name";
	private static final String PROFILE_NAME = "screen_name";
	private static final String FOLLOWERS_COUNT = "followers_count";
	private static final String DESCRIPTION = "description";
	private static final String STATUSES_COUNT = "statuses_count";
	private static final String FRIENDS_COUNT = "friends_count";
	
	
	private String TAG = getClass().getSimpleName();
	private HttpClient client;
	private OAuthConsumer consumer;
	private Activity activity;
	private String profileImageUrl;
	private String profileName;
	private ProgressBar progressBar;
	private String profileScreenName;
	private String profileDescription;
	private String profileFollowersCount;
	private String profileFriendsCount;
	private String profileStatusesCount;
	private CredentialsStorage credentialInstance;

	public CredentialsTask(Activity activity, HttpClient client,
			OAuthConsumer consumer, ProgressBar progressBar) {
		this.activity = activity;
		this.client = client;
		this.consumer = consumer;
		this.progressBar = progressBar;
	}

	@Override
	protected void onPreExecute() {

		progressBar.setVisibility(View.VISIBLE);
		Toast.makeText(activity, "Authentification", Toast.LENGTH_LONG)
				.show();


	}

	@Override
	protected JSONObject doInBackground(Void... params) {
		JSONObject jso = null;
		HttpGet get = new HttpGet(Constants.VERIFY_URL_STRING);

		Log.i(TAG, "get " + get);
		try {
			consumer.sign(get);
			Log.i(TAG, get.getURI().toString());
			String response = client.execute(get, new BasicResponseHandler());
			//Log.i(TAG, response);
			jso = new JSONObject(response);

			Log.d(TAG, "authenticatedQuery: " + jso.toString(2));
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jso;
	}

	protected void onPostExecute(JSONObject jso) {

		if (jso != null) {
			Log.d("IMAGE URL", jso.optString(PROFILE_IMG_URL));
			UserStatus us;
			try {
				us = new UserStatus(jso);

				profileImageUrl = us.getString(PROFILE_IMG_URL);
				profileName = us.getString(NAME);
				profileScreenName = us.getString(PROFILE_NAME);
				profileDescription = us.getString(DESCRIPTION);
				profileFollowersCount = us.getString(FOLLOWERS_COUNT);
				profileFriendsCount = us.getString(FRIENDS_COUNT);
				profileStatusesCount = us.getString(STATUSES_COUNT);
				
			} catch (JSONException e) {

				e.printStackTrace();
			}
			credentialInstance = CredentialsStorage.getInstance();
			credentialInstance.setImageUrl(
					profileImageUrl);
			credentialInstance.setName(profileName);
			credentialInstance.setProfile(profileScreenName);
			credentialInstance.setDescription(profileDescription);
			credentialInstance.setFollowersCount(profileFollowersCount);
			credentialInstance.setFriendsCount(profileFriendsCount);
			credentialInstance.setStatusesCount(profileStatusesCount);
			
			activity.startActivity(new Intent(activity,
					MainActivity.class));
			activity.finish();
			Log.d(TAG, "succsess auth");

		} else {
			Log.d(TAG, "error auth");
			activity.finish();
		}
		progressBar.setVisibility(View.GONE);
	}
}