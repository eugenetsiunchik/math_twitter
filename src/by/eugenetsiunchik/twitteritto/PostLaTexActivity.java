package by.eugenetsiunchik.twitteritto;

import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import by.eugenetsiunchik.twitteritto.tasks.HtmlHelper;

public class PostLaTexActivity extends Activity {

	private EditText editText;
	private String text;
	private final String TAG = getClass().getSimpleName();
	private URL url;
	private HtmlHelper hh;
	private List<String> listImgLinks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_activity);
		editText = (EditText) this.findViewById(R.id.postTweet);

	}

	public void Post(View v) {
		// TODO Auto-generated method stub

		Log.i(TAG, "Take");
		Intent intent = new Intent();
		if (editText.getTextSize() != 0) {
			text = editText.getText().toString();
			new HtmlParser(intent).execute(text);

		}
	}

	class HtmlParser extends AsyncTask<String, Void, Void> {

		private Intent intent;

		public HtmlParser(Intent intent) {
			this.intent = intent;
		}

		protected Void doInBackground(String... params) {
			
			try {
				
				url = new URL("http://texify.com/?$" + params[0] + "$");
				hh = new HtmlHelper(url);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
						
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			listImgLinks = hh.getImgLinks();
			text = "http://texify.com" + listImgLinks.get(0);
			intent.putExtra("message", text);
			setResult(RESULT_OK, intent);
			editText.setText(null);
			finish();
		}
	}
}
