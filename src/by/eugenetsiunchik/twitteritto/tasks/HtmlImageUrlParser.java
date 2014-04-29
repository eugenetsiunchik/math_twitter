package by.eugenetsiunchik.twitteritto.tasks;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import by.eugenetsiunchik.twitteritto.image.ImageLoader;
import by.eugenetsiunchik.twitteritto.models.FormulaModel;

import android.util.Log;
import android.widget.ImageView;

public class HtmlImageUrlParser implements Runnable {

	private static final String TAG = HtmlImageUrlParser.class.getSimpleName();
	private String parametr;
	private URL url;
	private List<String> listImgLinks;
	private String imageUrl;
	private ImageView imageView;
	private FormulaModel formulaModel;
	private String validateImageUrl;

	public HtmlImageUrlParser(String parametr, ImageView imageView, FormulaModel formulaModel) {
		this.parametr = parametr;
		this.imageView = imageView;
		this.formulaModel = formulaModel;
	}

	@Override
	public void run() {
		try {

			//url = new URL("http://chart.apis.google.com/chart?cht=tx&chl=%5C" + parametr);
			//listImgLinks = new HtmlHelper(url).getImgLinks();
			imageUrl = "http://chart.apis.google.com/chart?cht=tx&chl=" + URLEncoder.encode("\\" + parametr,"UTF-8");
			ImageLoader.getInstance().bind(imageUrl, imageView);
			formulaModel.setUrl(imageUrl);

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}

}
