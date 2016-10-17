package com.example.personalproject.activitys;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.personalproject.R;
import com.example.personalproject.adapters.CustomFuelArrayAdapter;
import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;
import com.example.personalproject.networking.Client;
import com.example.personalproject.networking.MemoryCache;
import com.example.personalproject.networking.ParserXmlMic;
import com.example.personalproject.networking.VolleyHttpClient;
import com.example.personalproject.utilitys.ActivityConstants;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class FuelPriceActivity extends AppCompatActivity {
    private static final String TAG = "TAG_EXCEPTION";

    // View to show the list of item.
    private RecyclerView mRecycleView;

    // Custom Adapter
    private CustomFuelArrayAdapter mAdapter;

    // List with all parse.
    private ArrayList<Combustible> CustomListViewValues;

    // Swipe to Refreshing Layout
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Spinner for the first request
    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view_activity);

        initializerVariables();
        getRssMicFromDataSource();
    }

    /**
     * Initializer the variable.
     */

    private void initializerVariables() {
        // Reference the recycle view.
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view_list);
        mRecycleView.setHasFixedSize(true);

        // Set the layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecycleView.setLayoutManager(layoutManager);

        // Initializer and set the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializer swipe to refresh.
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // initializer progress bar.
        mSpinner = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Listener for the refresh view.
        mSwipeRefreshLayout.setOnRefreshListener(new MyOnRefreshListeners());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.yellow, R.color.red, R.color.blue);
    }

    private void getRssMicFromDataSource() {
        if (new Client().isConnected(this)) {
            downloadRss();

        } else {
            Toast.makeText(this, R.string.not_internet_connection, Toast.LENGTH_SHORT).show();
            //TODO: Add image You don't have a internet connection, friendly
        }

    }


    private void setListViewCustomAdapter(final RssFeedMic mData) {
        CustomListViewValues = mData.getCombustibles();

        getTitleHeaderDate(mData.getTitle());

        // Create Custom Adapter
        mAdapter = new CustomFuelArrayAdapter(this, CustomListViewValues);
        mRecycleView.setAdapter(mAdapter);

        //Old
        //listView.setAdapter(mAdapter);

        //Animation the list view wrapping the adapter.
//        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mAdapter);
//        animationAdapter.setAbsListView(mListView);
//        mListView.setAdapter(animationAdapter);

    }

    /***
     * set the title
     *
     * @param text String with the title to show
     */

    private String getTitleHeaderDate(String text) {
        if (text != null && text.contains(":")) {
            String[] splitTitle = text.split(":");
            return splitTitle[1];
        }

        return "";
    }

    /**
     * This function used by adapter
     *
     * @param mPosition position row id.
     */

    //TODO: check this
    public void onItemClick(int mPosition) {
        Combustible tempValues = CustomListViewValues
                .get(mPosition);

        // SHOW ALERT
        Toast.makeText(this, " Code:" + tempValues.getCode() + "Price:"
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


    private class MyOnRefreshListeners implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            downloadRss();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /***
     * Get the XML data from MIC "Ministerio De Industria y Comercio" of the Dominican Republic.
     */

    private void downloadRss() {
        final String url = getResources().getString(R.string.url);

        // Request a string response for the provider URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSpinner.setVisibility(View.INVISIBLE);
                parseXmlResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });
        //TODO: implement a custom cache for tree minute independent of the response changes or not.

        // Get the singleton instance
        VolleyHttpClient volleyHttpClient = VolleyHttpClient.getInstance(this.getApplicationContext());

        // Add request to the request queue
        volleyHttpClient.addRequestQueue(stringRequest);
    }

    /**
     * Parse the String Response.
     *
     * @param response
     */
    private void parseXmlResponse(String response) {
        try {
            RssFeedMic mRssFeedData = new ParserXmlMic().readEntry(response);
            setListViewCustomAdapter(mRssFeedData);

        } catch (XmlPullParserException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * AsyncTask that get the XML and parse the XML form MIC.
     */

    // TODO:Remove old sync task.
//    private class DownloadRss extends AsyncTask<Void, Void, String> {
//        MemoryCache cache;
//        ProgressBar mSpinner;
//
//        @Override
//        protected void onPreExecute() {
//            mSpinner = (ProgressBar) findViewById(R.id.progress_bar);
//            cache = MemoryCache.getInstance();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            Client client = new Client();
//            String result = null;
//
//            if (!isCancelled()) {
//                // cache the data receiver is not exist.
//                if (cache.checkIfCacheContainsKey(ActivityConstants.EXTRA_XML_MIC_CACHE)) {
//                    return cache.getValueFromCache(ActivityConstants.EXTRA_XML_MIC_CACHE);
//                } else {
//                    return client.getRSS(getResources().getString(R.string.url));
//                }
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            ParserXmlMic parser = new ParserXmlMic();
//
//            mSpinner.setVisibility(View.GONE);
//
//            try {
//
//                //setXmlInMemoryCache(result);
//
//                // XML Parse
//                RssFeedMic mData = parser.readEntry(result);
//
//                setListViewCustomAdapter(mData);
//
//            } catch (XmlPullParserException e) {
//                Log.e(TAG, e.getMessage());
//                e.printStackTrace();
//
//            } catch (IOException e) {
//                Log.e(TAG, e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
}
