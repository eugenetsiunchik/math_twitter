package by.eugenetsiunchik.twitteritto.tasks;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import by.eugenetsiunchik.twitteritto.oauth.OAUTH;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class OAuthFinalTokenTask extends AsyncTask<Void, Void, Void> {

	private static final String TAG = null;
	private CommonsHttpOAuthProvider mProvider;
	private CommonsHttpOAuthConsumer mConsumer;
	private String verifier;
	private SharedPreferences prefs;
	private Intent i;
	private OAUTH oauth;

	public OAuthFinalTokenTask(CommonsHttpOAuthProvider mProvider,
			CommonsHttpOAuthConsumer mConsumer, String verifier, SharedPreferences prefs, OAUTH oauth, Intent i) {
		// TODO Auto-generated constructor stub
		this.mProvider = mProvider;
		this.mConsumer = mConsumer;
		this.verifier = verifier;
		this.prefs = prefs;
		this.oauth = oauth;
		this.i = i;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			mProvider.retrieveAccessToken(mConsumer, verifier);
			OAUTH.saveAuthInformation(prefs, mConsumer.getToken(), mConsumer.getTokenSecret());
			
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oauth.setResult(oauth.RESULT_OK, i);
		Log.i(TAG, "finish auth");
		oauth.startActivity(i); // we either authenticated and have the extras or not, but we're going back
		oauth.finish();
		return null;
	}

}
