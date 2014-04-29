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
import android.widget.ImageView;
import by.eugenetsiunchik.twitteritto.models.FormulaModel;
import by.eugenetsiunchik.twitteritto.tasks.HtmlHelper;
import by.eugenetsiunchik.twitteritto.tasks.HtmlImageUrlParser;

public class FormulaActivity extends Activity {

	private EditText editText;
	private String text;
	private final String TAG = getClass().getSimpleName();
	private URL url;
	private HtmlHelper hh;
	private List<String> listImgLinks;
	private ImageView imageView;
	private FormulaModel formulaModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_activity);
		editText = (EditText) this.findViewById(R.id.editFormula);
		imageView = (ImageView) this.findViewById(R.id.imageFormula);
		formulaModel = new FormulaModel();

	}

	public void checkFormulaMethod(View v) {

		Log.i(TAG, "Take");
		if (editText.getTextSize() != 0) {
			text = editText.getText().toString();
			new Thread(new HtmlImageUrlParser(text, imageView, formulaModel)).start();
		}
		
	}
	
	public void sendFormulaMethod(View v){
		Intent intent = new Intent();
		Log.d(TAG, formulaModel.getUrl());
		intent.putExtra("imageUrl", formulaModel.getUrl());
		setResult(RESULT_OK, intent);
		editText.setText(null);
		finish();
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
				Log.e(TAG, e.toString());
			}
						
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			listImgLinks = hh.getImgLinks();
			text = "http://texify.com" + listImgLinks.get(0);
			
		}
	}
}
