package com.example.infiny.tracker_master.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.example.infiny.tracker_master.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MarkerOptions markers[] = new MarkerOptions[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom((new LatLng(19.063959, 72.997155)), 12);
        mMap.animateCamera(cameraUpdate);

        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(19.056954, 73.003602))
                .add(new LatLng(19.058232, 73.003805))  // North of the previous point, but at the same longitude
                .add(new LatLng(19.061413, 73.002444))  // Same latitude, and 30km to the west
                .add(new LatLng(19.063482, 72.999215))
                .add(new LatLng(19.063959, 72.997155))
                .add(new LatLng(19.065241, 72.996033))
                .add(new LatLng(19.065577, 72.997915))
                .add(new LatLng(19.065726, 72.998836))
                .add(new LatLng(19.065609, 73.000071))
                .add(new LatLng(19.064798, 73.002269));  // Same longitude, and 16km to the south
//                .add(new LatLng(37.35, -122.0)); // Closes the polyline.

        MarkerOptions m1 = new MarkerOptions().position(new LatLng(19.056954, 73.003602));
        MarkerOptions m2 = new MarkerOptions().position(new LatLng(19.058232, 73.003805));
        MarkerOptions m3 = new MarkerOptions().position(new LatLng(19.061413, 73.002444));
        MarkerOptions m4 = new MarkerOptions().position(new LatLng(19.063482, 72.999215));
        MarkerOptions m5 = new MarkerOptions().position(new LatLng(19.063959, 72.997155));
        MarkerOptions m6 = new MarkerOptions().position(new LatLng(19.065241, 72.996033));
        MarkerOptions m7 = new MarkerOptions().position(new LatLng(19.065577, 72.997915));
        MarkerOptions m8 = new MarkerOptions().position(new LatLng(19.065726, 72.998836));
        MarkerOptions m9 = new MarkerOptions().position(new LatLng(19.065609, 73.000071));
        MarkerOptions m10 = new MarkerOptions().position(new LatLng(19.064798, 73.002269));

        mMap.addMarker(new MarkerOptions().position(new LatLng(19.056954, 73.003602)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.058232, 73.003805)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.061413, 73.002444)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.063482, 72.999215)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.063959, 72.997155)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.065241, 72.996033)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.065577, 72.997915)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.065726, 72.998836)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.065609, 73.000071)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(19.064798, 73.002269)));

// Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mMap.setInfoWindowAdapter(new CustomAdapter());
                return false;
            }
        });

    }

    public class CustomAdapter implements GoogleMap.InfoWindowAdapter {

        int titleSize;

//        public CustomAdapter(int titleSize) {
//            this.titleSize = titleSize;
//        }

        @Override
        public View getInfoWindow(Marker marker) {
//            final String title = marker.getTitle();
//            final String vicinity = marker.getSnippet();

//            SpannableString styledString = new SpannableString(vicinity);

//            styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, titleSize, 0);
//            styledString.setSpan(new RelativeSizeSpan(1.2f), 0, titleSize, 0);

            View view = getLayoutInflater().inflate(R.layout.marker_info, null);

            TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);

            TextView tvTime = (TextView) view.findViewById(R.id.tvTime);

//            tvDescription.setText(title);
//            tvTime.setText(styledString);

            return view;

        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
