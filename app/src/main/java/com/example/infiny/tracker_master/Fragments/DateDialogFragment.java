package com.example.infiny.tracker_master.Fragments;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.infiny.tracker_master.Activities.Reports;
import com.example.infiny.tracker_master.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Select Date");

        View view = inflater.inflate(R.layout.date_select_fragment, container, false);
        tvFromDate = (TextView) view.findViewById(R.id.tvFromDate);
        tvToDate = (TextView) view.findViewById(R.id.tvToDate);
        btSubmit = (Button) view.findViewById(R.id.btSubmit);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

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
                    Intent intent = new Intent(getActivity(),Reports.class);
                    intent.putExtra("fromDate",tvFromDate.getText().toString().toLowerCase().trim());
                    intent.putExtra("toDate",tvToDate.getText().toString().trim());
                    startActivity(intent);
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
}
