package com.example.personalproject.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

//TODO:Implement okHttp , or retrofit.
public class Client {
    /* Time in milliseconds */
    private static final int TIMEOUT_READ = 10000;
    private static final int TIMEOUT_CONNECT = 15000;
    private static final String DEBUG_TAG = "DEBUG_TAG";

    /***
     * Make a Http GET request to url provider.
     *
     * @param myUrl String with the rss url.
     * @return String in XML format with the data.
     */

    public String getRSS(String myUrl) {
        InputStream inputStream = null;
        String result = "";

        int len = 1024 * 40;

        try {
            URL url = new URL(myUrl);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setReadTimeout(TIMEOUT_READ);
            connection.setConnectTimeout(TIMEOUT_CONNECT);
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            // Initializer the connection.
            connection.connect();

            int response = connection.getResponseCode();
            Log.d(DEBUG_TAG, "The response is " + response);

            if (response == HttpURLConnection.HTTP_OK) {
                // Convert the inputString to string
                inputStream = connection.getInputStream();
                result = convertInputStreamToString(inputStream);

            } else {
                inputStream = connection.getErrorStream();
                Log.d(DEBUG_TAG, "The error response is: " +
                        convertInputStreamToString(inputStream));
            }

            connection.disconnect();

            if (inputStream != null) {
                inputStream.close();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(DEBUG_TAG, "XML_String: " + result.trim());

        return result.trim();
    }

    /***
     * Convert InputStream to String in format the xml encode &gt, &lt
     *
     * @param inputStream
     * @return
     * @throws IOException
     */

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));

        String line = "";

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        inputStream.close();

        return stringBuilder.toString();
    }

    /**
     * Check is the user have a active internet connectivity.
     *
     * @param context
     * @return true is have internet connection.
     */

    public boolean isConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
