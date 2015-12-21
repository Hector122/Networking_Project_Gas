package com.example.personalproject.activitys;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalproject.R;
import com.example.personalproject.adapters.CustomFuelArrayAdapter;
import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;
import com.example.personalproject.networking.Client;
import com.example.personalproject.networking.MemoryCache;
import com.example.personalproject.networking.ParseXmlMic;
import com.example.personalproject.utilitys.ActivityConstants;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class FuelPriceActivity extends Activity {
    private static final String TAG = "TAG_EXCEPTION";

    // List view to content XML layout
    private ListView mListView;

    // Hold XML Parse
    private RssFeedMic mData;

    // TextView that hold the title to show
    private TextView mTitle;

    // Custom Adapter to show the row
    private CustomFuelArrayAdapter mAdapter;

    // Reference the header view
    private LinearLayout mHeaderView;

    // String to hold the hearView message
    private TextView mMessageHeader;

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
        mListView = (ListView) findViewById(R.id.list_combustible);

        mTitle = (TextView) findViewById(R.id.text_view_title);
        mTitle.setVisibility(View.INVISIBLE);
    }

    private void setListViewCustomAdapter() {
        CustomListViewValuesArr = (ArrayList<Combustible>) mData
                .getCombustibles();

        //
        setTitle(mData.getTitle());


        // Create Custom Adapter
        mAdapter = new CustomFuelArrayAdapter(this, CustomListViewValuesArr);


        //Old
        //listView.setAdapter(mAdapter);

        //Animation the list view wrapping the adapter.
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);

    }

    /***
     * set the title
     *
     * @param text String with the title to show
     */

    private void setTitle(String text) {
        if (text.contains(":")) {
            String[] splitTitle = text.split(":");

            //Set the title
            mTitle.setText(splitTitle[0]);

            //set list header row
            setListHeaderRow(splitTitle[1]);
        }

        //Set visibility
        mTitle.setVisibility(View.VISIBLE);
    }

    public void setListHeaderRow(String textInformation) {
        if (mListView.getHeaderViewsCount() == 0) {

            mHeaderView = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.list_view_information_date_header_view, mListView, false);

            mMessageHeader = (TextView) mHeaderView.findViewById(R.id.text_header);
            mMessageHeader.setText(textInformation);

            //set header view on ListView
            mListView.setTag(ActivityConstants.HEADER_VIEW);

            mListView.addHeaderView(mHeaderView, null, false);
        }
    }


    /**
     * This function used by adapter
     *
     * @param mPosition position row id.
     */

    //TODO: check this
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

            // cache the data receiver is not exist.
            if (cache
                    .checkIfCacheContainsKey(ActivityConstants.EXTRA_XML_MIC_CACHE)) {
                return cache
                        .getValueFromCache(ActivityConstants.EXTRA_XML_MIC_CACHE);
            } else {
                return client.getRSS(getResources().getString(R.string.url));
            }
        }

        @Override
        protected void onPostExecute(String result) {
            ParseXmlMic parser = new ParseXmlMic();

            spinner.setVisibility(View.GONE);

            try {
                // TODO:Not cache for know
                // setXmlInMemoryCache(result);

                mData = parser.readEntry(result);

                setListViewCustomAdapter();

            } catch (XmlPullParserException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
