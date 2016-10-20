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


    /***
     * Return a string with the dominican currency prefix RD$ for human readable
     *
     * @param money
     * @return
     */
    public static String setNumberFormatToShow(double money) {
        String moneySymbol = "RD$";
        String numberFormatted;

        String number = String.valueOf(money);

        if (number.contains(".")) {
            String[] numberSplit = number.split("\\.");
            String decimalDigit = numberSplit[1];

            if (decimalDigit.length() < 2) {
                decimalDigit += "0";

                numberFormatted = numberSplit[0] + "." + decimalDigit;
            } else if (decimalDigit.length() > 2) {
                decimalDigit = decimalDigit.substring(0, 2);

                numberFormatted = numberSplit[0] + "." + decimalDigit;
            } else {
                numberFormatted = number;
            }

        } else {
            numberFormatted = number + ".00";
        }

        return moneySymbol + numberFormatted;
    }
}
