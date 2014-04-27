package by.eugenetsiunchik.twitteritto.oauth;

import android.net.Uri;


// You either come up with your own MyKeys class or you take out the extension and un-comment the values here
// with your own token and key. Let's see how this works as far as me not giving away my stuff all the time!
public class Keys {
	public static final String TWITTER_CONSUMER_KEY = "Bw6RrH1mTfJddYuteXHMeQ";
	public static final String TWITTER_CONSUMER_SECRET = "8ZBuUpV0PRDkAJLeBr5iMgCw6q5sB8FNvbeT4JsxIFI";

	public static final String OAUTH_CALLBACK_SCHEME	= "http";
	public static final String AUTH_CALLBACK_HOST = "twitteritto.com";
	public static final String OAUTH_CALLBACK_URL	=  OAUTH_CALLBACK_SCHEME + "://" + AUTH_CALLBACK_HOST;

	public static final Uri CALLBACK_URI = Uri.parse(OAUTH_CALLBACK_SCHEME + "://" + AUTH_CALLBACK_HOST);
}
