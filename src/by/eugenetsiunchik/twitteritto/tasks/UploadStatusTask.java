package by.eugenetsiunchik.twitteritto.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.http.HttpManager;
import by.eugenetsiunchik.twitteritto.utility.Constants;
import by.eugenetsiunchik.twitteritto.utility.UserStatus;

public class UploadStatusTask extends AsyncTask<String, Void, JSONObject> {

	private static final String TAG = UploadStatusTask.class.getSimpleName();
	private HttpParams httpParams;
	private OAuthConsumer ñonsumer;
	private Context context;

	public UploadStatusTask(Context context, OAuthConsumer consumer) {
		this.context = context;
		this.ñonsumer = consumer;
	}

	protected JSONObject doInBackground(String... params) {
		// TODO Send message to back-end like POST
		JSONObject jso = null;

		try {
			HttpPost post = new HttpPost(Constants.STATUSES_URL_STRING);
			LinkedList<BasicNameValuePair> out = new LinkedList<BasicNameValuePair>();
			out.add(new BasicNameValuePair("status", params[0]));
			post.setEntity(new UrlEncodedFormEntity(out, HTTP.UTF_8));
			httpParams = HttpManager.getInstance().getParams();
			// httpParams.
			post.setParams(httpParams);

			// sign the request to authenticate
			ñonsumer.sign(post);
			String response = new HttpManager().getResponse(post);
			jso = new JSONObject(response);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.toString());
		} catch (OAuthMessageSignerException e) {
			Log.e(TAG, e.toString());
		} catch (OAuthExpectationFailedException e) {
			Log.e(TAG, e.toString());
		} catch (OAuthCommunicationException e) {
			Log.e(TAG, e.toString());
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
		return jso;
	}

	@Override
	protected void onPostExecute(JSONObject jso) {
		// TODO This is in the UI thread, so we can mess with the UI
		super.onPostExecute(jso);
		if (jso != null) { // authorization succeeded, the json object contains the user information

			Toast.makeText(context, "Send sucsess", Toast.LENGTH_SHORT).show();
			
			try {
				Log.d(TAG, "send success: " + jso.toString(2));

				context.getContentResolver().insert(Contract.TweetsColumns.HOME_URI, UserStatus.toValues(jso, 0));
			
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}

		} else {
			Toast.makeText(context, "Send failed", Toast.LENGTH_SHORT).show();

		}
	}

}
