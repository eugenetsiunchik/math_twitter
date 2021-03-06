package by.eugenetsiunchik.twitteritto.adapters.feed;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import by.eugenetsiunchik.twitteritto.R;
import by.eugenetsiunchik.twitteritto.database.Contract;
import by.eugenetsiunchik.twitteritto.image.ImageLoader;
import by.eugenetsiunchik.twitteritto.utility.DateUtility;

public class TweetsFeedCursorAdapter extends CursorAdapter {

	private static final String TAG = TweetsFeedCursorAdapter.class
			.getSimpleName();
	//private URL url;
	//private String tweetIdString;

	public TweetsFeedCursorAdapter(Context context, Cursor c,
			boolean autoRequery) {

		super(context, c, true);

	}

	@Override
	public void bindView(View view, Context ctx, Cursor cursor) {
		// TODO Auto-generated method stub
		ImageView authorImage = (ImageView) view.findViewById(R.id.authorIcon);
		TextView authorName = (TextView) view.findViewById(R.id.who);
		TextView authorText = (TextView) view.findViewById(R.id.filling);
		TextView date = (TextView) view.findViewById(R.id.when);
		//ImageView imageStatus = (ImageView) view.findViewById(R.id.imageStatus);
		
		String howLong = cursor.getString(cursor
				.getColumnIndex(Contract.TweetsColumns.DATE));
		// tweet_id.setText(cursor.getString(cursor
		// .getColumnIndex(Contract.TweetsColumns.TWEET_ID)));
		authorName.setText(cursor.getString(cursor
				.getColumnIndex(Contract.TweetsColumns.NAME)));
		date.setText(DateUtility.getDateDifference(howLong));
		String status = cursor.getString(cursor
				.getColumnIndex(Contract.TweetsColumns.TEXT));
	//	String statusImgUrl = cursor.getString(cursor
	//			.getColumnIndex(Contract.TweetsColumns.STATUS_IMG_URL));
		

//		String [] parts = status.split("\\s");
 
        //for( String item : parts ) try {
          //  url = new URL(item);
         //   Log.i(TAG, item);    
        //} catch (MalformedURLException e) {
        //		e.printStackTrace();
        //}
        //if(!url.equals(null)){
        //	ImageLoader.getInstance().bind(url.toString(), imageStatus);
        //}else {
		//imageStatus.setVisibility(View.GONE);
       // }
//		if(!statusImgUrl.equals(null)){
	//		ImageLoader.getInstance().bind(statusImgUrl, imageStatus);
		//	imageStatus.setVisibility(View.VISIBLE);
		//}
			//ll.setVisibility(View.GONE);
		//}
		authorText.setText(status);
		// Log.i(TAG, cursor.getString(cursor
		// .getColumnIndex(Contract.TweetsColumns.IMG_URL)));

		//authorText.setAutoLinkMask(Linkify.WEB_URLS);

		// authorText.setLinkTextColor(R.color.linkColor);

		ImageLoader.getInstance().bind(
				cursor.getString(cursor
						.getColumnIndex(Contract.TweetsColumns.IMG_URL)),
				authorImage, this);

	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View newView(Context ctx, Cursor cursor, ViewGroup arg2) {
		return View.inflate(ctx, R.layout.item_adapter, null);
	}
	
}
