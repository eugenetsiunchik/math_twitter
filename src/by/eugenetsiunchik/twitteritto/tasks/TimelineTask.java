package by.eugenetsiunchik.twitteritto.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import by.eugenetsiunchik.twitteritto.ContextHolder;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.utility.PullToRefresh;
import by.eugenetsiunchik.twitteritto.utility.TimelineSelector;
import by.eugenetsiunchik.twitteritto.utility.UserStatus;

public class TimelineTask extends AsyncTask<TimelineSelector, Void, JSONArray> {

	private HttpClient mClient;
	private OAuthConsumer mConsumer;

	private String TAG = getClass().getSimpleName();
	private Context context;
	private List<UserStatus> usList = new ArrayList<UserStatus>();
	private int typeTweet;
	private Uri URI = Contract.TweetsColumns.HOME_URI;
	private PullToRefresh feedList;

	public TimelineTask(HttpClient mClient, OAuthConsumer mConsumer,
			int typeTweet, PullToRefresh feedList) {
		this.typeTweet = typeTweet;
		this.mClient = mClient;
		this.mConsumer = mConsumer;
		this.feedList = feedList;

		context = ContextHolder.getInstance().getContext();
		Log.i(TAG, "Constructed complete");
	}

	@Override
	protected JSONArray doInBackground(TimelineSelector... params) {
		JSONArray array = null;
		Log.i(TAG, "doInBackground");
		try {
			for (int i = 0; i < params.length; ++i) {
				Uri sUri = Uri.parse(params[i].url);
				Uri.Builder builder = sUri.buildUpon();
				if (params[i].since_id != null) {
					builder.appendQueryParameter("since_id",
							String.valueOf(params[i].since_id));
				} else if (params[i].max_id != null) {
					builder.appendQueryParameter("max_id",
							String.valueOf(params[i].max_id));
				}
				if (params[i].count != null) {
					builder.appendQueryParameter("count", String
							.valueOf((params[i].count > 200) ? 200
									: params[i].count));
				}
				if (params[i].page != null) {
					builder.appendQueryParameter("page",
							String.valueOf(params[i].page));
				}
				Log.i(TAG, builder.build().toString());
				HttpGet get = new HttpGet(builder.build().toString());
				mConsumer.sign(get);
  				String response = mClient.execute(get,
						new BasicResponseHandler());
				array = new JSONArray(response);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
		return array;
	}

	protected void onPostExecute(JSONArray array) {

		if (array != null) {
			try {
				for (int i = 0; i < array.length(); ++i) {
					JSONObject status = array.getJSONObject(i);
					Log.d(TAG, "Timeline: "
							+ array.getJSONObject(0).toString(2));

					usList.add(UserStatus.createModel(status));

				}
				if(feedList != null)
					feedList.onRefreshComplete();
				if(usList.size() != 0)
				context.getContentResolver().bulkInsert(URI,
						UserStatus.listToValues(usList, typeTweet));
				
			

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
