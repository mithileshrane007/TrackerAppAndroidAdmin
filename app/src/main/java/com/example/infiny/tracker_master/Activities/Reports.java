package com.example.infiny.tracker_master.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infiny.tracker_master.Adapters.ReportListAdapter;
import com.example.infiny.tracker_master.Adapters.TargetListAdapter;
import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Helpers.ErrorVolleyHandler;
import com.example.infiny.tracker_master.Helpers.SessionManager;
import com.example.infiny.tracker_master.Interfaces.OnItemClickListener;
import com.example.infiny.tracker_master.Models.LogHours;
import com.example.infiny.tracker_master.Models.Target;
import com.example.infiny.tracker_master.R;
import com.example.infiny.tracker_master.TrackerMaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reports extends AppCompatActivity {

    RecyclerView rvReportList;
    ReportListAdapter reportListAdapter;
    private LinearLayoutManager mLayoutManager;
    ArrayList<LogHours> logHourData;
    SessionManager sessionManager;
    ArrayList<LogHours>  dataArray;
    SharedPreferences prefTrackingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dataArray = new ArrayList<>();
        sessionManager = new SessionManager(this);
        rvReportList = (RecyclerView) findViewById(R.id.rvLogHourList);


        reportListAdapter = new ReportListAdapter(this, dataArray , new OnItemClickListener() {
            @Override
            public void onTargetItemClick(Target targetItem) {

            }

            @Override
            public void onReportItemClick(LogHours reportItem) {
                Intent intent = new Intent(Reports.this, MapsActivity.class);
                intent.putExtra("date", reportItem.getDate());
                startActivity(intent);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        rvReportList.setLayoutManager(mLayoutManager);
        rvReportList.setItemAnimator(new DefaultItemAnimator());
        rvReportList.setHasFixedSize(true);
        rvReportList.setAdapter(reportListAdapter);

        getReport();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getReport(){
        String url = Config.BASE_URL + "api/v1/getLogswithDate";

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
                                for (int i = 0; i < jsonResult.length(); i++){
                                    LogHours logHours = new LogHours();
                                    logHours.setDate(jsonResult.getJSONObject(i).getString("date"));
                                    logHours.setHours(jsonResult.getJSONObject(i).getString("log_hour"));
                                    dataArray.add(logHours);
                                    reportListAdapter.notifyDataSetChanged();

                                }
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
                        new ErrorVolleyHandler(Reports.this).onErrorResponse(error);
                        pDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("start_date", getIntent().getStringExtra("fromDate"));
                params.put("end_date", getIntent().getStringExtra("toDate"));
                params.put("tracking_id",getApplicationContext().getSharedPreferences("TrackingId", Context.MODE_PRIVATE).getString("TrackingId",null));

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
                mHeaders.put("token",sessionManager.getAuth_token());
                return mHeaders;
            }
        };

//        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TrackerMaster.getInstance().addToRequestQueue(request);
    }


}
