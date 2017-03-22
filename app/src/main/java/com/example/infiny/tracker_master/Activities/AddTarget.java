package com.example.infiny.tracker_master.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infiny.tracker_master.BuildConfig;
import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Helpers.ErrorVolleyHandler;
import com.example.infiny.tracker_master.Helpers.ExifUtils;
import com.example.infiny.tracker_master.Helpers.SessionManager;
import com.example.infiny.tracker_master.R;
import com.example.infiny.tracker_master.TrackerMaster;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTarget extends AppCompatActivity {

    private EditText etEmail;
    private EditText etFirstName;
    private EditText etPhone;
    private EditText etLastName;
    private EditText etTimeInterval;
    private EditText etTimeOut;
    private TextInputLayout email;
    private TextInputLayout firstName;
    private TextInputLayout phone;
    private TextInputLayout lastName;
    private TextInputLayout timeInterval;
    private TextInputLayout timeOut;
    private ImageView ivProfilePic;
    private Button btSave;
    private Bitmap profileImage;
    private int status;
    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 2;
    String[] perms = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;
    SessionManager sessionManager;
    private Uri uriSavedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_target);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManager = new SessionManager(this);

        btSave = (Button) findViewById(R.id.btSave);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etTimeInterval = (EditText) findViewById(R.id.etTimeInterval);
        etTimeOut = (EditText) findViewById(R.id.etTimeOut);

        email = (TextInputLayout) findViewById(R.id.email);
        firstName = (TextInputLayout) findViewById(R.id.firstName);
        phone = (TextInputLayout) findViewById(R.id.phone);
        lastName = (TextInputLayout) findViewById(R.id.lastName);
        timeInterval = (TextInputLayout) findViewById(R.id.timeInterval);
        timeOut = (TextInputLayout) findViewById(R.id.timeOut);

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(perms, permsRequestCode);
                    }

                } catch (NoSuchMethodError e) {
                    e.printStackTrace();
                }
                selectImage();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
                if (status == 0) {
                    new AlertDialog.Builder(AddTarget.this)
                            .setMessage("Add Target?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addTarget();
                                }
                            }).setNegativeButton("No", null).show();
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

        if (TextUtils.isEmpty(firstName.getEditText().getText().toString().trim())) {
            firstName.setError("Please enter First Name");
            status = 1;

            etFirstName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    firstName.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    firstName.setError(null);
                    status = 0;
                }
            });
        }

        if (TextUtils.isEmpty(lastName.getEditText().getText().toString().trim())) {
            lastName.setError("Please enter Last Name");
            status = 1;

            etLastName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    lastName.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    lastName.setError(null);
                    status = 0;
                }
            });
        }

        if (TextUtils.isEmpty(timeInterval.getEditText().getText().toString().trim())) {
            timeInterval.setError("Please enter Time Interval");
            status = 1;

            etTimeInterval.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    timeInterval.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    timeInterval.setError(null);
                    status = 0;
                }
            });
        }

        if (TextUtils.isEmpty(timeOut.getEditText().getText().toString().trim())) {
            timeOut.setError("Please enter Time Out");
            status = 1;

            etTimeOut.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    timeOut.setError(null);
                    status = 0;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    timeOut.setError(null);
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

    public void addTarget() {
        String url = Config.BASE_URL + "api/v1/create_targets";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Adding target...");
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
                                Toast.makeText(AddTarget.this,"Target added successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddTarget.this,Home.class));
                                finish();
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
                        new ErrorVolleyHandler(AddTarget.this).onErrorResponse(error);
                        pDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email.getEditText().getText().toString().toLowerCase().trim());
                params.put("first_name", firstName.getEditText().getText().toString().trim());
                params.put("phone_no", phone.getEditText().getText().toString().trim());
                params.put("last_name", lastName.getEditText().getText().toString().trim());
                params.put("track_time_interval", timeInterval.getEditText().getText().toString().trim());
                params.put("track_time_out", timeOut.getEditText().getText().toString().trim());
                params.put("image", convertToBase64(profileImage));

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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTarget.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    try {
                        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Tracker-Master/Pictures/ProfilePics");
                            imagesFolder.mkdirs();
                            File image = new File(imagesFolder, "IMG_" + timeStamp + ".jpg");
                            uriSavedImage = Uri.fromFile(image);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        } else {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            uriSavedImage = FileProvider.getUriForFile(AddTarget.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    createImageFile());
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        }
                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                    dialog.dismiss();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    ivProfilePic.setImageBitmap(getBitmapFromUri(data.getData(),AddTarget.this));
                    profileImage = getBitmapFromUri(data.getData(),AddTarget.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == CAMERA_REQUEST) {
                ivProfilePic.setImageBitmap(getBitmapFromUri(uriSavedImage,AddTarget.this));
                profileImage = getBitmapFromUri(uriSavedImage,AddTarget.this);
            }
        }
    }

    public static String convertToBase64(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        byte[] byteArrayImage = baos.toByteArray();

        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        return encodedImage;

    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            Bitmap bitmap = ExifUtils.rotateBitmap(ExifUtils.getPath(context, uri), image);
            parcelFileDescriptor.close();
            return bitmap;
        } catch (Exception e) {
            Log.e("Ex", "Failed to load image.", e);
            return null;
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOEx", "Error closing ParcelFile Descriptor");
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(new Date());
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Android/data/com.infiny.tracker-master/files/Pictures/ProfilePics");
        File image = new File(storageDir, "IMG_" + timeStamp + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
