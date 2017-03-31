package com.example.infiny.tracker_master;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.example.infiny.tracker_master.Helpers.ConnectivityReceiver;
import com.example.infiny.tracker_master.Helpers.LruBitmapCache;
import io.fabric.sdk.android.Fabric;

/**
 * Created by infiny on 24/2/17.
 */

public class TrackerMaster  extends MultiDexApplication {
    public static final String TAG = TrackerMaster.class
            .getSimpleName();

    private static TrackerMaster mInstance;
    public static RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        // printHashKey();
       // ACRA.init(this);
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public static synchronized TrackerMaster getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
