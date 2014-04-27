package by.eugenetsiunchik.twitteritto.oauth;

import junit.framework.Assert;
import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import by.eugenetsiunchik.twitteritto.StartActivity;
import by.eugenetsiunchik.twitteritto.tasks.OAutRequestTokenTask;
import by.eugenetsiunchik.twitteritto.tasks.OAuthFinalTokenTask;
import by.eugenetsiunchik.twitteritto.utility.Constants;

public class OAUTH extends Activity {
	private static final String TAG = "OAUTH";

	public static final String USER_TOKEN = "user_token";
	public static final String USER_SECRET = "user_secret";
	public static final String REQUEST_TOKEN = "request_token";
	public static final String REQUEST_SECRET = "request_secret";
	private static SharedPreferences settings;
	private CommonsHttpOAuthProvider mProvider;
	private CommonsHttpOAuthConsumer mConsumer;
	private SharedPreferences prefs;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.i(TAG, "start activity OAUTH");
		// We don't need to worry about any saved states: we can reconstruct the state
		//mConsumer = new CommonsHttpOAuthConsumer(
		//		Keys.TWITTER_CONSUMER_KEY, 
		//		Keys.TWITTER_CONSUMER_SECRET);
		
		mConsumer = Consumer.getInstance().getConsumer();
		
		mProvider = new CommonsHttpOAuthProvider (
				Constants.TWITTER_REQUEST_TOKEN_URL, 
				Constants.TWITTER_ACCESS_TOKEN_URL,
				Constants.TWITTER_AUTHORIZE_URL);
		
		
		// It turns out this was the missing thing to making standard Activity launch mode work
		mProvider.setOAuth10a(true);
		
		prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

		Intent i = this.getIntent();
		if (i.getData() == null) {
			try {
                                // This is really important. If you were able to register your real callback Uri with Twitter, and not some fake Uri
                                // like I registered when I wrote this example, you need to send null as the callback Uri in this function call. Then
                                // Twitter will correctly process your callback redirection
				Log.i(TAG, "Retrieving request token");
				
				new OAutRequestTokenTask(this, mConsumer, mProvider, prefs).execute();
				
				//Log.i(TAG, "url: " + authUrl);
				Log.i(TAG,"get url");
				//saveRequestInformation(prefs, mConsumer.getToken(), mConsumer.getTokenSecret());
				//this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			}catch(Exception e){
				
			}
		}
	}
	

	@Override
	protected void onResume() {
		super.onResume();
        Log.i(TAG, "RESUME OAUTH");
		Uri uri = getIntent().getData();
		if (uri != null && Keys.CALLBACK_URI.getScheme().equals(uri.getScheme())) {
			String token = prefs.getString(OAUTH.REQUEST_TOKEN, null);
			String secret = prefs.getString(OAUTH.REQUEST_SECRET, null);
			Log.i(TAG, "Token and secret: " + token + "/n " + secret);
			Intent i = new Intent(this, StartActivity.class); // Currently, how we get back to main activity.
			
			try {
				if(!(token == null || secret == null)) {
					mConsumer.setTokenWithSecret(token, secret);
				}
				String otoken = uri.getQueryParameter(OAuth.OAUTH_TOKEN);
				final String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

				// We send out and save the request token, but the secret is not the same as the verifier
				// Apparently, the verifier is decoded to get the secret, which is then compared - crafty
				// This is a sanity check which should never fail - hence the assertion
				Assert.assertEquals(otoken, mConsumer.getToken());
				
				new OAuthFinalTokenTask(mProvider,mConsumer,verifier,prefs,this,i).execute();
				// Now we can retrieve the goodies
				// Clear the request stuff, now that we have the real thing
				OAUTH.saveRequestInformation(prefs, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				
			}
		}
	}
	
	public static void saveRequestInformation(SharedPreferences settings, String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if(token == null) {
			editor.remove(OAUTH.REQUEST_TOKEN);
			Log.d(TAG, "Clearing Request Token");
		}
		else {
			editor.putString(OAUTH.REQUEST_TOKEN, token);
			Log.d(TAG, "Saving Request Token: " + token);
		}
		if (secret == null) {
			editor.remove(OAUTH.REQUEST_SECRET);
			Log.d(TAG, "Clearing Request Secret");
		}
		else {
			editor.putString(OAUTH.REQUEST_SECRET, secret);
			Log.d(TAG, "Saving Request Secret: " + secret);
		}
		editor.commit();
		
	}
	
	public static void saveAuthInformation(SharedPreferences settings, String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if(token == null) {
			editor.remove(OAUTH.USER_TOKEN);
			Log.d(TAG, "Clearing OAuth Token");
		}
		else {
			editor.putString(OAUTH.USER_TOKEN, token);
			Log.d(TAG, "Saving OAuth Token: " + token);
		}
		if (secret == null) {
			editor.remove(OAUTH.USER_SECRET);
			Log.d(TAG, "Clearing OAuth Secret");
		}
		else {
			editor.putString(OAUTH.USER_SECRET, secret);
			Log.d(TAG, "Saving OAuth Secret: " + secret);
		}
		editor.commit();
		
	}
	
	public static void removeAuthInformation(SharedPreferences setting) {
		
		settings = PreferenceManager.getDefaultSharedPreferences(new OAUTH());
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(OAuth.OAUTH_TOKEN);
		editor.remove(OAuth.OAUTH_TOKEN_SECRET);
		editor.commit();
	}
	
}
