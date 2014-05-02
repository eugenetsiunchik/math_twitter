package by.eugenetsiunchik.twitteritto.tasks;

import java.net.URLEncoder;

import android.util.Log;
import android.widget.ImageView;
import by.eugenetsiunchik.twitteritto.image.ImageLoader;
import by.eugenetsiunchik.twitteritto.models.FormulaModel;

public class HtmlImageUrlParser implements Runnable {

	private static final String TAG = HtmlImageUrlParser.class.getSimpleName();
	private String parametr;
	private String imageUrl;
	private ImageView imageView;
	private FormulaModel formulaModel;

	public HtmlImageUrlParser(String parametr, ImageView imageView, FormulaModel formulaModel) {
		this.parametr = parametr;
		this.imageView = imageView;
		this.formulaModel = formulaModel;
	}

	public HtmlImageUrlParser(String parametr, FormulaModel formulaModel) {
		this.parametr = parametr;
		this.formulaModel = formulaModel;
	}

	@Override
	public void run() {
		try {

			imageUrl = "http://chart.apis.google.com/chart?cht=tx&chl=" + URLEncoder.encode("\\" + parametr, "UTF-8");
			if (imageView != null) {
				ImageLoader.getInstance().bind(imageUrl, imageView);
			}
			formulaModel.setUrl(imageUrl);

		} catch (Exception e) {

			Log.e(TAG, e.toString());
		}

	}

}
