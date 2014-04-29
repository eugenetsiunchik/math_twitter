package by.eugenetsiunchik.twitteritto.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import by.eugenetsiunchik.twitteritto.http.HttpManagering;
import by.eugenetsiunchik.twitteritto.utility.Constants;
import by.eugenetsiunchik.twitteritto.utility.Downloader;

public class UploadStatusWithMediaTask extends AsyncTask<String, Void, String> {

	private static final String TAG = UploadStatusTask.class.getSimpleName();
	private OAuthConsumer consumer;
	private HttpResponse response;

	public UploadStatusWithMediaTask(Context context, OAuthConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	protected String doInBackground(String... params) {

		HttpPost post = new HttpPost(Constants.STATUSES_WITH_MEDIA_URL_STRING);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		byte[] bytesImage = Downloader.imageToByteArray(params[1]);
		Log.i(TAG, params[0] + "/n" + params[1] + "/n" + bytesImage);

		builder.addTextBody("status", params[0]);
		builder.addBinaryBody("media[]", bytesImage);
		final HttpEntity yourEntity = builder.build();

		try {
			consumer.sign(post);

			post.setEntity(yourEntity);

			response = HttpManagering.getInstance().getClient().execute(post);

		} catch (IOException e) {
			Log.e(TAG, e.toString());

		} catch (OAuthMessageSignerException e1) {
			Log.e(TAG, e1.toString());

		} catch (OAuthExpectationFailedException e1) {
			Log.e(TAG, e1.toString());

		} catch (OAuthCommunicationException e1) {
			Log.e(TAG, e1.toString());
		}
		return getContent(response);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i(TAG, result);
	}

	public static String getContent(HttpResponse response) {
		BufferedReader rd;
		String body = "";
		String content = "";
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			while ((body = rd.readLine()) != null) {
				content += body + "\n";
			}
		} catch (IllegalStateException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		return content.trim();
	}

}
