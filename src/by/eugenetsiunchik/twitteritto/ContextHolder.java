package by.eugenetsiunchik.twitteritto;

import android.content.Context;

public class ContextHolder {

private static ContextHolder instance;
private Context mContext;
	
	public static ContextHolder getInstance() {
		if (instance == null) {
			instance = new ContextHolder();
		}
		return instance;
	}
	
	

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
	
	public Context getContext() {
		return mContext;
	}

}
