package by.eugenetsiunchik.twitteritto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import by.eugenetsiunchik.twitteritto.models.FormulaModel;
import by.eugenetsiunchik.twitteritto.tasks.HtmlImageUrlParser;

public class FormulaActivity extends Activity {

	private EditText editText;
	private String text;
	private final String TAG = getClass().getSimpleName();
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
		if(formulaModel.getUrl() != null){
		intent.putExtra("imageUrl", formulaModel.getUrl());
		}else {
			new Thread(new HtmlImageUrlParser(text,formulaModel)).start();
		}
		setResult(RESULT_OK, intent);
		editText.setText(null);
		finish();
	}

}
