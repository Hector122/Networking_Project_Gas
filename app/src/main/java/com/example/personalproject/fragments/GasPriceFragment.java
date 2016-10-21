package com.example.personalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.personalproject.R;
import com.example.personalproject.adapters.CustomFuelArrayAdapter;
import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;
import com.example.personalproject.networking.ParserXmlMic;
import com.example.personalproject.networking.VolleyHttpClient;
import com.example.personalproject.utilitys.Utilitys;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hector castillo on 10/20/16.
 */

public class GasPriceFragment extends Fragment implements OnRefreshListener {
    private static final String TAG_ERROR = "TAG_EXCEPTION";
    private static final String TAG_INFO = "TAG_INFORMATION";

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

    // Reference the Activity Context.
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gas_price, container, false);

        initializerVariables(view);
        getRssMicFromDataSource();

        return view;
    }

    /**
     * Initializer the variable.
     */

    private void initializerVariables(View view) {
        // Reference to the activity caller
        mContext = getActivity();

        // Reference the recycle view.
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycler_view_list);
        mRecycleView.setHasFixedSize(true);

        // Set the layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);

        // Initializer swipe to refresh.
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.yellow, R.color.red, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        // Initializer progress bar.
        mSpinner = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    /***
     * Check is user have a internet connection to make the request
     */
    private void getRssMicFromDataSource() {
        if (Utilitys.isConnected(mContext)) {
            downloadRss();

        } else {
            Toast.makeText(mContext, R.string.not_internet_connection, Toast.LENGTH_SHORT).show();
            //TODO: Add image for this message, very friendly
        }
    }

    @Override
    public void onRefresh() {
        getRssMicFromDataSource();
        mSwipeRefreshLayout.setRefreshing(false);
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

    /***
     * @param mData
     */
    private void setListViewCustomAdapter(final RssFeedMic mData) {
        CustomListViewValues = mData.getCombustibles();

        getTitleHeaderDate(mData.getTitle());

        // Create Custom Adapter
        mAdapter = new CustomFuelArrayAdapter(mContext, CustomListViewValues);
        mRecycleView.setAdapter(mAdapter);

        //TODO: put the animation creating the list.
        //Old
        //listView.setAdapter(mAdapter);

        //Animation the list view wrapping the adapter.
//        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mAdapter);
//        animationAdapter.setAbsListView(mListView);
//        mListView.setAdapter(animationAdapter);

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
            Log.e(TAG_ERROR, e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            Log.e(TAG_ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    /***
     * Get the XML data from MIC "Ministerio De Industria y Comercio" of the Dominican Republic.
     * using Android Volley library.
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
                Log.e(TAG_ERROR, error.getMessage());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.i(TAG_INFO, String.valueOf(response.statusCode));

                return super.parseNetworkResponse(response);
            }
        };
        //TODO: implement a custom cache for tree minute independent of the response changes or not.
        // http://stackoverflow.com/questions/17541498/android-volley-override-cache-timeout-for-json-request

        // Get the singleton instance
        VolleyHttpClient volleyHttpClient = VolleyHttpClient.getInstance(getActivity());

        // Add request to the request queue
        volleyHttpClient.addRequestQueue(stringRequest);
    }
}
