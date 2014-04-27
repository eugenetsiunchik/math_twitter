package by.eugenetsiunchik.twitteritto.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import by.eugenetsiunchik.twitteritto.ContextHolder;

public class ConnectionDetector {
	public static Context mContext;
	private static ConnectionDetector instance;
	private Context context;
	
	public ConnectionDetector (){
		context = ContextHolder.getInstance().getContext();
	       
	}
	public static ConnectionDetector getInstance(){
		if (instance == null) {
		instance = new ConnectionDetector();
		}
		return instance;
		
	}
	
	public boolean isConnectingToInternet(){
		 ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
	public boolean isConnectingToInternet(Context mContext){
		 ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
         if (connectivity != null)
         {
             NetworkInfo[] info = connectivity.getAllNetworkInfo();
             if (info != null)
                 for (int i = 0; i < info.length; i++)
                     if (info[i].getState() == NetworkInfo.State.CONNECTED)
                     {
                         return true;
                     }

         }
         return false;
   }
}
