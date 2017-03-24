package com.example.infiny.tracker_master.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.ArrayMap;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Helpers.ErrorVolleyHandler;
import com.example.infiny.tracker_master.Helpers.SessionManager;
import com.example.infiny.tracker_master.Models.LogCoordinates;
import com.example.infiny.tracker_master.R;
import com.example.infiny.tracker_master.TrackerMaster;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SessionManager sessionManager;
    ArrayList<LogCoordinates> coordinatesArrayList;
    LatLngBounds.Builder markerBoundsBuilder;
    LatLngBounds latLngBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sessionManager = new SessionManager(this);
        coordinatesArrayList = new ArrayList<>();
        markerBoundsBuilder = new LatLngBounds.Builder();

        getCoordinates();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions();
        for (int i = 0; i < coordinatesArrayList.size(); i++) {
            markerBoundsBuilder.include(new LatLng(Double.parseDouble(coordinatesArrayList.get(i).getLatitude()), Double.parseDouble(coordinatesArrayList.get(i).getLongitude())));
            rectOptions.add(new LatLng(Double.parseDouble(coordinatesArrayList.get(i).getLatitude()), Double.parseDouble(coordinatesArrayList.get(i).getLongitude())));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(coordinatesArrayList.get(i).getLatitude()), Double.parseDouble(coordinatesArrayList.get(i).getLongitude()))).title("Time : " + coordinatesArrayList.get(i).getTime()));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(markerBoundsBuilder.build(), 14));

// Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
                return false;
            }
        });

    }

    public void getCoordinates() {
        String url = Config.BASE_URL + "api/v1/getLogswithStatus";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error) {
                                JSONArray jsonResult = jsonObject.getJSONArray("result");
                                for (int i = 0; i < jsonResult.length(); i++) {
                                    LogCoordinates logCoordinates = new LogCoordinates();
                                    logCoordinates.setLatitude(jsonResult.getJSONObject(i).getString("latitude"));
                                    logCoordinates.setLongitude(jsonResult.getJSONObject(i).getString("longitude"));
                                    logCoordinates.setTime(jsonResult.getJSONObject(i).getString("time"));
                                    coordinatesArrayList.add(logCoordinates);
                                }
                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);
                                mapFragment.getMapAsync(MapsActivity.this);
                                pDialog.dismiss();
                            } else {
                                pDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        new ErrorVolleyHandler(MapsActivity.this).onErrorResponse(error);
                        pDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("date", getIntent().getStringExtra("date"));
                params.put("tracking_id", getApplicationContext().getSharedPreferences("TrackingId", Context.MODE_PRIVATE).getString("TrackingId", null));

                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new ArrayMap<String, String>();
                mHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                mHeaders.put("Accept", "application/json");
                mHeaders.put("token", sessionManager.getAuth_token());
                return mHeaders;
            }
        };

//        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TrackerMaster.getInstance().addToRequestQueue(request);
    }

}
