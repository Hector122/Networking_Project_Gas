package com.example.personalproject.utilitys;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hector castillo on 10/18/16.
 */

public class Utilitys {

    /**
     * Check is the user have a active internet connectivity.
     *
     * @param context Activity context
     * @return true is have internet connection.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
