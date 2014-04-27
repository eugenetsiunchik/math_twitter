package by.eugenetsiunchik.twitteritto.oauth;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.SharedPreferences;
import android.util.Log;

public class Consumer {

	private static final String TAG = "CONSUMER";
	private static Consumer instance;
	private CommonsHttpOAuthConsumer mConsumer;
	private String mToken;
	private String mSecret;

	public Consumer() {
		try {
			mConsumer = new CommonsHttpOAuthConsumer(Keys.TWITTER_CONSUMER_KEY,
					Keys.TWITTER_CONSUMER_SECRET);
		} catch (Exception e) {
			Log.e(TAG, "Error creating consumer", e);
		}

	}

	public static Consumer getInstance() {
		if (instance == null) {
			instance = new Consumer();
		}
		return instance;
	}

	public CommonsHttpOAuthConsumer getConsumer(SharedPreferences settings) {
		if (settings.contains(OAUTH.USER_TOKEN)
				&& settings.contains(OAUTH.USER_SECRET)) {
			mToken = settings.getString(OAUTH.USER_TOKEN, null);
			mSecret = settings.getString(OAUTH.USER_SECRET, null);
			if (!(mToken == null || mSecret == null)) {
				Log.i(TAG, mToken + " " + mSecret);
				mConsumer.setTokenWithSecret(mToken, mSecret);
				
				return mConsumer;
			}
			return null;

		}
		return null;
	}

	public CommonsHttpOAuthConsumer getConsumer() {
		// TODO Auto-generated method stub
		return mConsumer;
	}

}
