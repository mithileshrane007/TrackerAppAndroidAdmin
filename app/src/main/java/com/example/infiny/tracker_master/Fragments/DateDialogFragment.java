package com.example.infiny.tracker_master.Fragments;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infiny.tracker_master.Activities.AddTarget;
import com.example.infiny.tracker_master.Activities.Home;
import com.example.infiny.tracker_master.Activities.MapsActivity;
import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Helpers.ErrorVolleyHandler;
import com.example.infiny.tracker_master.Helpers.SessionManager;
import com.example.infiny.tracker_master.R;
import com.example.infiny.tracker_master.TrackerMaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by infiny on 21/3/17.
 */

public class DateDialogFragment extends DialogFragment {

    TextView tvFromDate, tvToDate;
    Button btSubmit;
    private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
    private Calendar newDate, newDate1;
    private SimpleDateFormat dateFormatter;
    int status;
    SessionManager sessionManager;
    SparseArray<String> logDataArray;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_select_fragment, container, false);
        getDialog().setTitle("Please select dates");

        logDataArray = new SparseArray<>();
        sessionManager = new SessionManager(getActivity());
        tvFromDate = (TextView) view.findViewById(R.id.tvFromDate);
        tvToDate = (TextView) view.findViewById(R.id.tvToDate);
        btSubmit = (Button) view.findViewById(R.id.btSubmit);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvFromDate.setText(dateFormatter.format(newDate.getTime()));
                tvFromDate.setError(null);
                tvFromDate.setTextColor(Color.BLACK);
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });
        Calendar newCalendar1 = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate1 = Calendar.getInstance();
                newDate1.set(year, monthOfYear, dayOfMonth);
                tvToDate.setText(dateFormatter.format(newDate1.getTime()));
                tvToDate.setError(null);
                tvToDate.setTextColor(Color.BLACK);
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, newCalendar1.get(Calendar.YEAR), newCalendar1.get(Calendar.MONTH), newCalendar1.get(Calendar.DAY_OF_MONTH));


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForm();
                if (status == 0) {
                    startActivity(new Intent(getActivity(),MapsActivity.class));
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    public void checkForm() {
        status = 0;

        if (TextUtils.isEmpty(tvFromDate.getText())) {
            tvFromDate.setError("Please select a date");
            tvFromDate.setHintTextColor(Color.RED);
            status = 1;
        }

        if (TextUtils.isEmpty(tvToDate.getText())) {
            tvToDate.setError("Please select a date");
            tvToDate.setHintTextColor(Color.RED);
            status = 1;
        }
    }

    public void getReport(){
        String url = Config.BASE_URL + "api/v1/getLogswithDate";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
//                                    logDataArray.append(jsonResult.getJSONObject(i).get(""));
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
                        new ErrorVolleyHandler(getActivity()).onErrorResponse(error);
                        pDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("start_date", tvFromDate.getText().toString().toLowerCase().trim());
                params.put("end_date", tvToDate.getText().toString().trim());
                params.put("tracking_id","");

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

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TrackerMaster.getInstance().addToRequestQueue(request);
    }
}
