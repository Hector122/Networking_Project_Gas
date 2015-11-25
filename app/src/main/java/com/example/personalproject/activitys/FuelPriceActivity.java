package com.example.personalproject.activitys;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalproject.R;
import com.example.personalproject.utilitys.ActivityConstants;
import com.example.personalproject.adapters.CustomFuelArrayAdapter;
import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;
import com.example.personalproject.networking.Client;
import com.example.personalproject.networking.MemoryCache;
import com.example.personalproject.networking.ParseXmlMic;

public class FuelPriceActivity extends Activity {
    // List view to content XML layout
    private ListView listView;

    // Hold XML Parse
    private RssFeedMic mData;

    // TextView that hold the title to show
    private TextView mTitle, mSubTitle;


    // Custom Adapter to show the row
    CustomFuelArrayAdapter adapter;

    // List with all parse.
    private ArrayList<Combustible> CustomListViewValuesArr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main_activity);

        initializerVariables();
        getRssMicAsyncTask();

    }

    private void getRssMicAsyncTask() {
        new DownloadRSS().execute();
    }

    /**
     * Initializer the variable.
     */

    private void initializerVariables() {
        listView = (ListView) findViewById(R.id.list_combustible);

        mTitle = (TextView) findViewById(R.id.txtv_title);
        mTitle.setVisibility(View.INVISIBLE);

        mSubTitle = (TextView) findViewById(R.id.txtv_sub_title);
        mSubTitle.setVisibility(View.INVISIBLE);


    }

    private void setListViewCustomAdapter() {
        CustomListViewValuesArr = (ArrayList<Combustible>) mData
                .getCombustibles();

        setTitle(mData.getTitle());

        // Create Custom Adapter
        adapter = new CustomFuelArrayAdapter(this, CustomListViewValuesArr);

        // AlphaAnimationAdapter animationAdapter = new AlphaAnimationAdapter(adapter);
        listView.setAdapter(adapter);
    }

    private void setTitle(String text) {

        if (text.contains(":")) {
            String[] splitTitle = text.split(":");

            //Set the title
            mTitle.setText(splitTitle[0]);
            mSubTitle.setText(splitTitle[1]);
        }

        mTitle.setVisibility(View.VISIBLE);
        mSubTitle.setVisibility(View.VISIBLE);
    }


    /**
     * @param mPosition
     */

    //TODO: check this
    // This function used by adapter
    public void onItemClick(int mPosition) {
        Combustible tempValues = CustomListViewValuesArr
                .get(mPosition);

        // SHOW ALERT
        Toast.makeText(
                this,
                " Code:" + tempValues.getCode() + "Price:"
                        + tempValues.getPrice(), Toast.LENGTH_LONG).show();
    }

    /**
     * Save the data receive into the memory cache.
     *
     * @param data String with the xml to save.
     */

    private void setXmlInMemeoryCache(String data) {
        MemoryCache cache = MemoryCache.getInstance();

        cache.putValueInCache(ActivityConstants.EXTRA_XML_MIC_CACHE, data);
    }

    /**
     * AsyncTask that get the XML and parse the XML form MIC
     */

    private class DownloadRSS extends AsyncTask<Void, Void, String> {
        MemoryCache cache = MemoryCache.getInstance();

        ProgressBar spinner;

        @Override
        protected void onPreExecute() {
            spinner = (ProgressBar) findViewById(R.id.progress_bar);
        }

        @Override
        protected String doInBackground(Void... params) {
            Client client = new Client();

            //TODO: cache the data receiver.
//            if (cache
//                    .checkIfCacheContainsKey(ActivityConstants.EXTRA_XML_MIC_CACHE)) {
//                return cache
//                        .getValueFromCache(ActivityConstants.EXTRA_XML_MIC_CACHE);
//            } else {
            return client.getRSS(getResources().getString(R.string.url));
            //}
        }

        @Override
        protected void onPostExecute(String result) {
            ParseXmlMic parser = new ParseXmlMic();

            spinner.setVisibility(View.GONE);

            try {
                // TODO:Not cache for know
                // setXmlInMemoryCache(result);

                mData = parser.readEntry(result); // parser.readEntry(result);


                setListViewCustomAdapter();

            } catch (XmlPullParserException e) {
                Log.e("FuelPriceActivity.XmlPullParserException --> ",
                        e.getMessage());
                e.printStackTrace();

            } catch (IOException e) {
                Log.e("FuelPriceActivity.IOException -->", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
