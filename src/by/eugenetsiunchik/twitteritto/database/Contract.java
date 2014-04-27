package by.eugenetsiunchik.twitteritto.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
	public static final String AUTHORITY = "by.eugenetsiunchik.twitteritto.database.TwitterProvider";

    public Contract() {
    }

    public static final class TweetsColumns implements BaseColumns {

            private TweetsColumns() {
            }
            public static final String HOME_PATH = "home";

            
            public static final Uri HOME_URI = Uri.parse("content://" + AUTHORITY + "/" + HOME_PATH);

            public static final String HOME_TYPE = "vnd.android.cursor.dir/" + HOME_PATH;
            


            public static final String TWEET_ID = _ID;
            public static final String TWEET_TYPE = "TYPE";
            public static final String NAME = "NAME";
            public static final String DATE = "DATE";
            public static final String TEXT = "TEXT";
            public static final String IMG_URL = "IMG_URL";
            public static final String STATUS_IMG_URL = "STATUS_IMG_URL";
               
            
    }


}
