package com.example.infiny.tracker_master.Activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Models.Target;
import com.example.infiny.tracker_master.R;
import com.squareup.picasso.Picasso;

public class TargetDetails extends AppCompatActivity {

    private TextInputLayout fName,lName,email,phone;
    private EditText etFirstName,etLastName,etEmail,etPhone;
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


        fName= (TextInputLayout) findViewById(R.id.fName);
        lName= (TextInputLayout) findViewById(R.id.lName);
        email= (TextInputLayout) findViewById(R.id.email);
        phone= (TextInputLayout) findViewById(R.id.phone);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        tvReports = (TextView) findViewById(R.id.tvReports);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        target = (Target) getIntent().getSerializableExtra("target");

        etFirstName.setText(target.getFirstName());
        etLastName.setText(target.getLastName());
        etEmail.setText(target.getEmail());
        etPhone.setText(target.getPhoneNo());

        Picasso.with(this)
                .load(Config.BASE_URL + target.getProfilePic())
                .placeholder(R.drawable.gradient)
                .into(ivBackground);

        Picasso.with(this)
                .load(Config.BASE_URL + target.getProfilePic())
                .placeholder(R.drawable.ic_person_36dp)
                .into(ivProfileImage);

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
