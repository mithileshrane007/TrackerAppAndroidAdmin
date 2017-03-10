package com.example.infiny.tracker_master.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Helpers.ErrorVolleyHandler;
import com.example.infiny.tracker_master.R;
import com.example.infiny.tracker_master.TrackerMaster;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etPhone;
    private EditText etOrganisationName;
    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout phone;
    private TextInputLayout organisationName;
    private int status;
    private CheckBox cbTAC;
    private Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        phone = (TextInputLayout) findViewById(R.id.phone);
        organisationName = (TextInputLayout) findViewById(R.id.organisationName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etOrganisationName = (EditText) findViewById(R.id.etOrganisationName);
        cbTAC = (CheckBox) findViewById(R.id.cbTAC);
        btSignUp = (Button) findViewById(R.id.btSignUp);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
                if (status == 0) {
                    if (cbTAC.isChecked()) {
                        signUp();
                    } else {
                        Toast.makeText(Register.this, "Please accept Terms of Use", Toast.LENGTH_SHORT).show();
                    }
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

    public void submitForm() {
        status = 0;

        if (TextUtils.isEmpty(email.getEditText().getText().toString().trim())) {
            email.setError("Please enter your Email ID");
            status = 1;

            etEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    email.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    email.setError(null);
                    status = 0;
                }
            });
        }

        if (!isValidEmail(email.getEditText().getText().toString().trim())) {
            email.setError("Please enter valid Email ID");
            status = 1;
        }

        if (TextUtils.isEmpty(phone.getEditText().getText().toString().trim())) {
            phone.setError("Please enter your Phone No.");
            status = 1;

            etPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    phone.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    phone.setError(null);
                    status = 0;
                }
            });
        }

        if (!isValidMobile(phone.getEditText().getText().toString().trim())) {
            phone.setError("Please enter valid Phone No.");
            status = 1;
        }

        if (TextUtils.isEmpty(password.getEditText().getText().toString().trim()) || password.getEditText().getText().toString().trim().length() < 8) {
            password.setError("Password must have minimum 8 characters");
            status = 1;

            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    password.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    password.setError(null);
                    status = 0;
                }
            });
        }

        if (TextUtils.isEmpty(organisationName.getEditText().getText().toString().trim())) {
            organisationName.setError("Please enter your Organisation Name");
            status = 1;

            etOrganisationName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    organisationName.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    organisationName.setError(null);
                    status = 0;
                }
            });
        }

    }

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public void signUp() {
        String url = Config.BASE_URL + "api/v1/create_users";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing Up ...");
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
                                String userId = jsonObject.getString("id");
                                String email = jsonObject.getString("email");
                                String phoneNo = jsonObject.getString("phone_no");
                                String organisationName = jsonObject.getString("organisation_name");
                                String authToken = jsonObject.getString("auth_token");
                                Toast.makeText(Register.this, "User Id : " + userId + "\n" + "Email : " + email + "\n" + "Phone No. : " + phoneNo + "\n"
                                        + "Organisation Name : " + organisationName + "\n" + "Auth Token : " + authToken, Toast.LENGTH_LONG).show();
                                pDialog.dismiss();
                                finish();
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
                        new ErrorVolleyHandler(Register.this).onErrorResponse(error);
                        pDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email.getEditText().getText().toString().toLowerCase().trim());
                params.put("password", password.getEditText().getText().toString().trim());
                params.put("phone_no", phone.getEditText().getText().toString().trim());
                params.put("organisation_name", organisationName.getEditText().getText().toString().trim());

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
                return mHeaders;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TrackerMaster.getInstance().addToRequestQueue(request);

    }

}
