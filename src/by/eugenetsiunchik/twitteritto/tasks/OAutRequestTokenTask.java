package by.eugenetsiunchik.twitteritto.tasks;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import by.eugenetsiunchik.twitteritto.oauth.Keys;
import by.eugenetsiunchik.twitteritto.oauth.OAUTH;

public class OAutRequestTokenTask extends AsyncTask<Void, Void, String> {
	private String TAG = getClass().getSimpleName();
	private Activity activity;
	private CommonsHttpOAuthConsumer consumer;
	private CommonsHttpOAuthProvider provider;
	private SharedPreferences prefs;
	private String url;

	public OAutRequestTokenTask(Activity activity, CommonsHttpOAuthConsumer consumer,
			CommonsHttpOAuthProvider provider, SharedPreferences prefs) {

		this.activity = activity;
		this.consumer = consumer;
		this.provider = provider;
		this.prefs = prefs;

	}

	@Override
	protected String doInBackground(Void... arg0) {
		try {
			Log.i(TAG, "Retrieving request token from Google servers");
			url = provider.retrieveRequestToken(consumer, Keys.OAUTH_CALLBACK_URL);
			Log.i(TAG, "Popping a browser with the authorize URL : " + url);

			
		} catch (Exception e) {
			Log.e(TAG, "Error during OAUth retrieve request token", e);
		}
		return url;
	}
	
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		OAUTH.saveRequestInformation(prefs, consumer.getToken(), consumer.getTokenSecret());
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivityForResult(intent, 100);
	}

}
