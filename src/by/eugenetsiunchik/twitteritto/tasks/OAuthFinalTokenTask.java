package by.eugenetsiunchik.twitteritto.tasks;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import by.eugenetsiunchik.twitteritto.ContextHolder;
import by.eugenetsiunchik.twitteritto.StartActivity;
import by.eugenetsiunchik.twitteritto.oauth.OAUTH;

public class OAuthFinalTokenTask extends AsyncTask<Void, Void, Void> {

	private static final String TAG = null;
	private CommonsHttpOAuthProvider mProvider;
	private CommonsHttpOAuthConsumer mConsumer;
	private String verifier;
	private SharedPreferences prefs;
	private OAUTH oauth;

	public OAuthFinalTokenTask(CommonsHttpOAuthProvider mProvider, CommonsHttpOAuthConsumer mConsumer, String verifier,
			SharedPreferences prefs, OAUTH oauth) {

		this.mProvider = mProvider;
		this.mConsumer = mConsumer;
		this.verifier = verifier;
		this.prefs = prefs;
		this.oauth = oauth;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			mProvider.retrieveAccessToken(mConsumer, verifier);
			OAUTH.saveAuthInformation(prefs, mConsumer.getToken(), mConsumer.getTokenSecret());

		} catch (OAuthMessageSignerException e) {
			Log.e(TAG, e.toString());
		} catch (OAuthNotAuthorizedException e) {
			Log.e(TAG, e.toString());
		} catch (OAuthExpectationFailedException e) {
			Log.e(TAG, e.toString());
		} catch (OAuthCommunicationException e) {
			Log.e(TAG, e.toString());
		}
		Intent i = new Intent(oauth, StartActivity.class);
		oauth.setResult(200, i);
		Log.i(TAG, "finish auth");
		oauth.startActivity(i); // we either authenticated and have the extras
								// or not, but we're going back
		oauth.finish();
		return null;
	}

}
