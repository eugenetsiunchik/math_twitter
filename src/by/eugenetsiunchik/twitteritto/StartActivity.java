package by.eugenetsiunchik.twitteritto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.http.HttpManagering;
import by.eugenetsiunchik.twitteritto.oauth.Consumer;
import by.eugenetsiunchik.twitteritto.oauth.OAUTH;
import by.eugenetsiunchik.twitteritto.tasks.CredentialsTask;
import by.eugenetsiunchik.twitteritto.utility.ConnectionDetector;
import by.eugenetsiunchik.twitteritto.utility.Constants;

public class StartActivity extends Activity implements OnClickListener {

	private SharedPreferences mSettings;
	private String mToken;
	private String mSecret;
	private String TAG = getClass().getSimpleName();
	private static final int OAUH = 1;
	private Button loginButton;
	private Button logoutButton;
	private ProgressBar progressBar;
	private LinearLayout inOutBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_activity);
		loginButton = (Button) findViewById(R.id.door);
		logoutButton = (Button) findViewById(R.id.logoutButton);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		inOutBar = (LinearLayout) findViewById(R.id.inOutBar);

		// removeTokens();

		isAuthorisation();

		loginButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
	}

	private boolean isAuthorisation() {
		mSettings = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
		if (ConnectionDetector.getInstance().isConnectingToInternet(
				getApplicationContext())) {

			if (mSettings != null) {
				if (mSettings.contains(OAUTH.USER_TOKEN)

				&& mSettings.contains(OAUTH.USER_SECRET)) {
					mToken = mSettings.getString(OAUTH.USER_TOKEN, null);
					mSecret = mSettings.getString(OAUTH.USER_SECRET, null);
					if (!(mToken == null || mSecret == null)) {
						Log.i(TAG, mToken + " " + mSecret);

						progressBar.setVisibility(View.GONE);
						inOutBar.setVisibility(View.GONE);
						new CredentialsTask(this, HttpManagering.getInstance()
								.getClient(), Consumer.getInstance()
								.getConsumer(mSettings), progressBar).execute();
						return true;
					} else {
						Toast.makeText(getApplicationContext(),
								"Authentification error", Toast.LENGTH_SHORT)
								.show();
						progressBar.setVisibility(View.GONE);
						inOutBar.setVisibility(View.VISIBLE);
						Log.i(TAG, "token or secret null");
						return false;

					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Authentification error", Toast.LENGTH_SHORT)
							.show();
					progressBar.setVisibility(View.GONE);
					inOutBar.setVisibility(View.VISIBLE);
					Log.i(TAG, "token or secret null");
					return false;

				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Authentification error", Toast.LENGTH_SHORT).show();
				Log.i(TAG, "token or secret null");
				return false;

			}
		} else {
			Toast.makeText(getApplicationContext(), "CONNECTION ERROR",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == OAUH) {
				isAuthorisation();

			}
		} else {
			Log.d(TAG, "onActivityResult relultCode != RESULT_OK");
		}

	}

	public void onClick(View v) {
		if (loginButton.equals(v)) {
			if (ConnectionDetector.getInstance().isConnectingToInternet(
					getApplicationContext())) {
				startActivityForResult(new Intent(this, OAUTH.class), OAUH);
			}else
				Toast.makeText(getApplicationContext(),
						"CONNECTION INTERNET ERROR", Toast.LENGTH_SHORT).show();
		} 

		if (logoutButton.equals(v)) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					getApplicationContext().getContentResolver().delete(
							Contract.TweetsColumns.HOME_URI, null, null);

				}
			}).start();

			removeTokens();
		}
	}

	private void removeTokens() {
		mSettings = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = mSettings.edit();
		editor.remove(OAUTH.USER_TOKEN);
		editor.remove(OAUTH.USER_SECRET);
		editor.commit();

		Log.i(TAG, "PREFS CLEAN ");
		Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT)
				.show();

	}

}
