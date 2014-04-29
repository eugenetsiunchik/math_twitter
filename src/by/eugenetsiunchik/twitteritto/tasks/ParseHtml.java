package by.eugenetsiunchik.twitteritto.tasks;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import by.eugenetsiunchik.twitteritto.image.ImageLoader;

class ParseHtml extends AsyncTask<String, Void, Void> {
	private static final String TAG = ParseHtml.class.getSimpleName();
	private ImageView img;
	private List<String> output;

	public ParseHtml(ImageView img) {

		this.img = img;
	}

	protected Void doInBackground(String... arg) {
		output = new ArrayList<String>();
		try {
			HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
			Log.i(TAG, "arg: " + arg[0]);
			output = hh.getImgLinks();
			Log.i(TAG, "image url: " + output.get(0));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}	

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		// ImageLoader.getInstance().bind("http://texify.com/" + output.get(0), img);
	}
}
