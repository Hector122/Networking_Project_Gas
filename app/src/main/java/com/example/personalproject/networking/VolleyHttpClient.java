package com.example.personalproject.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by hector castillo on 10/11/16.
 * Singleton Class that handler the http request.
 */

public class VolleyHttpClient {

    private static VolleyHttpClient mSingletonVolley;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    /***
     * Constructors of the class.
     *
     * @param mContext
     */
    private VolleyHttpClient(Context mContext) {
        this.mContext = mContext;
        mRequestQueue = getRequestQueue();
    }


    public static synchronized VolleyHttpClient getInstance(Context context) {
        if (mSingletonVolley == null) {
            mSingletonVolley = new VolleyHttpClient(context);
        }
        return mSingletonVolley;
    }

    /***
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            return Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /***
     * Add request to the queue FIFO
     *
     * @param request
     * @param <T>
     */
    public <T> void addRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
