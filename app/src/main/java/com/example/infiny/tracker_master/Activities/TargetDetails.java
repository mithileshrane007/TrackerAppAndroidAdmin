package com.example.infiny.tracker_master.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.infiny.tracker_master.Fragments.DateDialogFragment;
import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Models.Target;
import com.example.infiny.tracker_master.R;
import com.squareup.picasso.Picasso;

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
                    new DateDialogFragment().show(getFragmentManager(), "Please select dates");
                }
            }
        });

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

}
