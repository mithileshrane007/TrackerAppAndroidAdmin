package com.example.infiny.tracker_master.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Models.Target;
import com.example.infiny.tracker_master.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TargetDetails extends AppCompatActivity {

    private TextView tvFirstName,tvLastName,tvEmail,tvPhone;
    private TextView tvReports;
    Target target;
    ImageView ivBackground, ivProfileImage;
    SharedPreferences prefTrackingId;
    SharedPreferences.Editor editorTrackingId;
    RelativeLayout rlReports;
    private SimpleDateFormat dateFormatter;
    Calendar newDate;
    private AlertDialog alertDialog;
    private TextView tvFromDate;
    private TextView tvToDate;
    int status;
    private Button btSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvReports = (TextView) findViewById(R.id.tvReports);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        rlReports = (RelativeLayout) findViewById(R.id.rlReports);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


        target = (Target) getIntent().getSerializableExtra("target");

        prefTrackingId = this.getSharedPreferences("TrackingId", MODE_PRIVATE);
        prefTrackingId.edit().putString("TrackingId",target.getTrackingId()).apply();

        tvFirstName.setText(target.getFirstName());
        tvLastName.setText(target.getLastName());
        tvEmail.setText(target.getEmail());
        tvPhone.setText(target.getPhoneNo());

        Picasso.with(this)
                .load(Config.BASE_URL + target.getProfilePic())
                .placeholder(R.drawable.gradient)
                .into(ivBackground);

        Picasso.with(this)
                .load(Config.BASE_URL + target.getProfilePic())
                .placeholder(R.drawable.ic_person_36dp)
                .into(ivProfileImage);

        rlReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(target.getIsOnline()){
                    Intent intent = new Intent(TargetDetails.this,MapsActivity.class);
                    intent.putExtra("date",new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
                    startActivity(intent);
                }else{

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TargetDetails.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = TargetDetails.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.date_select_fragment, null);
                    dialogBuilder.setView(dialogView);

                    tvFromDate = (TextView) dialogView.findViewById(R.id.tvFromDate);
                    tvFromDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectDate(tvFromDate);
                        }
                    });

                    tvToDate = (TextView) dialogView.findViewById(R.id.tvToDate);
                    tvToDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectDate(tvToDate);
                        }
                    });

                    btSubmit = (Button) dialogView.findViewById(R.id.btSubmit);
                    btSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkForm();
                            if (status == 0) {
                                Intent intent = new Intent(TargetDetails.this,Reports.class);
                                intent.putExtra("fromDate",tvFromDate.getText().toString().toLowerCase().trim());
                                intent.putExtra("toDate",tvToDate.getText().toString().trim());
                                startActivity(intent);
                                alertDialog.dismiss();
                            }
                        }
                    });

                    alertDialog = dialogBuilder.create();
                    alertDialog.setTitle("Select dates");
                    alertDialog.show();
//                    new DateDialogFragment().show(getFragmentManager(), "dateFrag");
                }
            }
        });

    }

    public void selectDate(final TextView tvHolder){
        Calendar newCalendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvHolder.setText(dateFormatter.format(newDate.getTime()));
                tvHolder.setError(null);
                tvHolder.setTextColor(Color.BLACK);
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void checkForm() {
        status = 0;

        if (TextUtils.isEmpty(tvFromDate.getText())) {
            tvFromDate.setError("Please select a date");
            tvFromDate.setHintTextColor(Color.RED);
            status = 1;
        }else if(!TextUtils.isEmpty(tvToDate.getText())){
            try {
                if(dateFormatter.parse(tvFromDate.getText().toString()).after(dateFormatter.parse(tvToDate.getText().toString()))){
                    tvFromDate.setError("");
                    status = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(tvToDate.getText())) {
            tvToDate.setError("Please select a date");
            tvToDate.setHintTextColor(Color.RED);
            status = 1;
        }else if(!TextUtils.isEmpty(tvFromDate.getText())){
            try {
                if(dateFormatter.parse(tvToDate.getText().toString()).before(dateFormatter.parse(tvFromDate.getText().toString()))){
                    tvFromDate.setError("");
                    status = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        finish();
    }

}
