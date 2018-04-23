package com.monsordi.gotravel.api;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by diego on 12/04/18.
 */

public class AppController extends Application {

    public static final String BASE_URL="http://192.168.0.31:8080/";

    private static final String TAG = "AppController";
    private static AppController mInstance;

    public static synchronized AppController getInstance(){
        return mInstance;
    }

    private RequestQueue mRequestQueue;

    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(Object tag){
        if(mRequestQueue!=null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
