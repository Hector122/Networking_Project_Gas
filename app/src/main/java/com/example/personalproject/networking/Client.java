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
import android.util.Log;

public class Client {
	/* Time in milliseconds */
	private static final int TIMEOUT_READ = 30000;
	private static final int TIMEOUT_CONNECT = 50000;

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

			// start the query
			connection.connect();

			int response = connection.getResponseCode();

			if (response == HttpURLConnection.HTTP_OK) {
				// convert the inputString to string
				inputStream = connection.getInputStream();
				result = readIt(inputStream, len);
				//result = convertInputStreamToString(inputStream);

			} else {
				inputStream = connection.getErrorStream();

				Log.d("DEBUG_TAG", "The response is: " + response);
				result = "The response is: " + response + " "
						+ readIt(inputStream, len);

			}

			connection.disconnect();

			if (inputStream != null)
				inputStream.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i("XML MIC: ", result.trim());

		return result.trim();
	}

	private static String readIt(InputStream stream, int len)
			throws IOException, UnsupportedEncodingException {

		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);

		return new String(buffer); // .replaceAll("&lt;",
									// "<").replaceAll("&gt;",
									// ">").replaceAll("\\s+", "");
	}

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

	// Check is the user have connectivity
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
