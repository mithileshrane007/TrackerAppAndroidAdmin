package com.example.infiny.tracker_master.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Models.Target;
import com.example.infiny.tracker_master.R;
import com.squareup.picasso.Picasso;

public class TargetDetails extends AppCompatActivity {

    private TextView tvFirstName,tvLastName,tvEmail,tvPhone;
    private TextView tvReports;
    Target target;
    ImageView ivBackground, ivProfileImage;


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

        target = (Target) getIntent().getSerializableExtra("target");

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

        tvReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TargetDetails.this,MapsActivity.class));
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
