package by.eugenetsiunchik.twitteritto.fragments;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import by.eugenetsiunchik.twitteritto.FormulaActivity;
import by.eugenetsiunchik.twitteritto.R;
import by.eugenetsiunchik.twitteritto.http.HttpManagering;
import by.eugenetsiunchik.twitteritto.oauth.Consumer;
import by.eugenetsiunchik.twitteritto.tasks.PostTasks;
import by.eugenetsiunchik.twitteritto.tasks.UploadStatusTask;
import by.eugenetsiunchik.twitteritto.tasks.UploadStatusWithMediaTask;
import by.eugenetsiunchik.twitteritto.utility.Downloader;

public class MessageFragment extends Fragment implements OnClickListener {

	private static final String TAG = MessageFragment.class.getSimpleName();
	private static final int POST_TWEET = 0;
	private EditText editTextPost;
	private Button buttonPost;
	private Button buttonLaTexExpression;
	private String message;
	private LinearLayout ll;
	private boolean haveImage = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);

		editTextPost = (EditText) rootView.findViewById(R.id.editPost);
		buttonPost = (Button) rootView.findViewById(R.id.buttonPost);
		ll = (LinearLayout) rootView.findViewById(R.id.postLayout);
		buttonLaTexExpression = (Button) rootView.findViewById(R.id.buttonLaTexExpression);

		buttonPost.setOnClickListener(this);
		buttonLaTexExpression.setOnClickListener(this);

		return rootView;

	}

	@Override
	public void onClick(View v) {
		if (v.equals(buttonPost)) {
			if (editTextPost.getTextSize() != 0) {
				if (!haveImage) {
					new UploadStatusTask(getActivity(), Consumer.getInstance().getConsumer()).execute(editTextPost
							.getText().toString());

				} else {
					new UploadStatusWithMediaTask(getActivity(), Consumer.getInstance().getConsumer()).execute(
							editTextPost.getText().toString(), message);

				}

				editTextPost.setText(null);
			}

		}
		if (v.equals(buttonLaTexExpression)) {
			Log.i(TAG, "btn click work in if");
			this.startActivityForResult(new Intent(getActivity(), FormulaActivity.class), POST_TWEET);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {

			if (requestCode == POST_TWEET) {
				message = data.getStringExtra("imageUrl");
				Log.d(TAG, message);
				if (message != null) {
					editTextPost.setText(editTextPost.getText().toString());
					haveImage = true;
				}
			} else {
				Log.i(TAG, "post error");
			}
		}

	}
}
