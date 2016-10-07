package com.example.personalproject.activitys;

import android.os.AsyncTask;
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

import com.example.personalproject.R;
import com.example.personalproject.adapters.CustomFuelArrayAdapter;
import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;
import com.example.personalproject.networking.Client;
import com.example.personalproject.networking.MemoryCache;
import com.example.personalproject.networking.ParserXmlMic;
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

    //Async Task Download XML
    private DownloadRss downloadRss;

    //TODO: remove.
    // View that hold the title to show.
    //private TextView mTitle;

    //private TabLayout tabs;

    // Reference the header view
    //private LinearLayout mHeaderView;

    // String to hold the hearView message
    //private TextView mMessageHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view_activity);

        initializerVariables();
        getRssMicAsyncTask();
    }

    /**
     * Initializer the variable.
     */

    private void initializerVariables() {
        //Reference the recycle view.
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view_list);
        mRecycleView.setHasFixedSize(true);

        //Set the layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecycleView.setLayoutManager(layoutManager);

        //Initializer and set the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        //TODO: Add TABS set color maybe.
        //Reference to the header title.
//        mTitle = (TextView) findViewById(R.id.text_view_title);
//        mTitle.setVisibility(View.INVISIBLE);

//        tabs = (TabLayout) findViewById(R.id.tabs);
//        tabs.addTab(tabs.newTab().setText("testing 1"));
//        tabs.addTab(tabs.newTab().setText("testing 2"));
//        tabs.addTab(tabs.newTab().setText("testing 3"));
//
//        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Listener for the refresh view.
        mSwipeRefreshLayout.setOnRefreshListener(new MyOnRefreshListeners());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.yellow, R.color.red, R.color.blue);
    }

    private void getRssMicAsyncTask() {
        if (new Client().isConnected(this)) {
            downloadRss = new DownloadRss();
            downloadRss.execute();
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

//    public void setListHeaderRow(String textInformation) {
//        if (mListView.getHeaderViewsCount() == 0) {
//
//            mHeaderView = (LinearLayout) getLayoutInflater().inflate(
//                    R.layout.list_view_information_date_header_view, mListView, false);
//
//            mMessageHeader = (TextView) mHeaderView.findViewById(R.id.text_header);
//            mMessageHeader.setText(textInformation);
//
//            //set header view on ListView
//            mListView.setTag(ActivityConstants.HEADER_VIEW);
//
//            mListView.addHeaderView(mHeaderView, null, false);
//        }
//    }


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
            if (downloadRss.getStatus() != AsyncTask.Status.RUNNING) {
                getRssMicAsyncTask();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * AsyncTask that get the XML and parse the XML form MIC
     */

    private class DownloadRss extends AsyncTask<Void, Void, String> {
        MemoryCache cache;
        ProgressBar spinner;

        @Override
        protected void onPreExecute() {
            spinner = (ProgressBar) findViewById(R.id.progress_bar);
            cache = MemoryCache.getInstance();
        }

        @Override
        protected String doInBackground(Void... params) {
            Client client = new Client();
            String result = null;

            if (!isCancelled()) {
                // cache the data receiver is not exist.
                if (cache.checkIfCacheContainsKey(ActivityConstants.EXTRA_XML_MIC_CACHE)) {
                    return cache.getValueFromCache(ActivityConstants.EXTRA_XML_MIC_CACHE);
                } else {
                    return client.getRSS(getResources().getString(R.string.url));
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            ParserXmlMic parser = new ParserXmlMic();

            spinner.setVisibility(View.GONE);

            try {
                // TODO:Not cache for know, cache the data for five  minutes.
                //setXmlInMemoryCache(result);

                // XML Parse
                RssFeedMic mData = parser.readEntry(result);

                setListViewCustomAdapter(mData);

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
