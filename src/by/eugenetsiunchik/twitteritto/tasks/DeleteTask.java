package by.eugenetsiunchik.twitteritto.tasks;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import by.eugenetsiunchik.twitteritto.adapters.feed.TweetsFeedCursorAdapter;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.http.HttpManager;
import by.eugenetsiunchik.twitteritto.utility.Constants;

public class DeleteTask extends AsyncTask<Long, Void, JSONObject> {

	private static final String TAG = DeleteTask.class.getSimpleName();
	private Context context;
	private HttpClient client;
	private OAuthConsumer ñonsumer;
	private Long id;
	private JSONObject jso;
	private TweetsFeedCursorAdapter mTweetsFeedCursorAdapter;

	public DeleteTask(Context context, HttpClient client, OAuthConsumer consumer) {
		this.context = context;
		this.client = client;
		this.ñonsumer = consumer;
	}

	@Override
	protected JSONObject doInBackground(Long... params) {
		id = params[0];
		try {
			HttpPost post = new HttpPost(Constants.STATUSES_DELETE_URL_STRING + id + ".json");
			ñonsumer.sign(post);
			String response = new HttpManager().getResponse(post);
			jso = new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jso;
	}

	protected void onPostExecute(JSONObject jso) {
		
		if (jso != null) { 
			Toast.makeText(context, "delete sucsess", Toast.LENGTH_SHORT).show();
			try {
				Log.d(TAG, "delete success: " + jso.toString(2));

				context.getContentResolver().delete(
						Contract.TweetsColumns.HOME_URI, id.toString(), null);
				//mTweetsFeedCursorAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			Toast.makeText(context, "delete failed", Toast.LENGTH_SHORT).show();

		}
	}

}
