package com.example.infiny.tracker_master.Helpers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.infiny.tracker_master.R;


/**
 * Created by infiny on 13/12/16.
 */

public class ErrorVolleyHandler implements Response.ErrorListener  {


    private final Context mContext;

    public ErrorVolleyHandler(Context context)
    {
        this.mContext=context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        error.printStackTrace();
        if (error instanceof TimeoutError)
        {
            Toast.makeText(mContext,"Ooops !!! Slow Internet Connection", Toast.LENGTH_SHORT).show();
        }
        if( error instanceof NetworkError) {
            Toast.makeText(mContext,"Ooops !!! Problem with Internet Connection", Toast.LENGTH_SHORT).show();

        }
        if( error instanceof ServerError) {
            Toast.makeText(mContext, R.string.some_went_wrong_only, Toast.LENGTH_SHORT).show();

        }
        if( error instanceof AuthFailureError) {
            Toast.makeText(mContext,R.string.some_went_wrong_only, Toast.LENGTH_SHORT).show();

        }
        if( error instanceof ParseError) {
            Toast.makeText(mContext,R.string.some_went_wrong_only, Toast.LENGTH_SHORT).show();

        }
        if( error instanceof NoConnectionError) {
            Toast.makeText(mContext,"No Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }
}
