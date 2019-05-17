package tk.hiddenname.formulatheory.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;

public class NetworkUtil {

   public static boolean isNetworkConnected(@NotNull Context context) {
	  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	  //should check null because in airplane mode it will be null
	  return (netInfo != null && netInfo.isConnected());
   }

   static boolean isInternetConnected() {
	  boolean status = false;
	  try {
		 InetAddress address = InetAddress.getByName(HttpClient.BASE_URL);
		 Log.d("UpdateDataService", "inetaddr is:" + address.toString());
		 if (address != null) status = true;
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return status;
   }
}
