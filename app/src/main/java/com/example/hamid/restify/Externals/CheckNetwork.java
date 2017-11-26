package com.example.hamid.restify.Externals;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*This class is used to check internet connection availability
* on a device*/
public class CheckNetwork {

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) //no internet connection
            return false;
        else if (info.isConnected()) //internet is available
            return true;
        else //internet connection as well
            return true;
    }
}
