package com.example.infiny.tracker_master.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infiny.tracker_master.Adapters.TargetListAdapter;
import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Helpers.ConnectivityReceiver;
import com.example.infiny.tracker_master.Helpers.ErrorVolleyHandler;
import com.example.infiny.tracker_master.Helpers.SessionManager;
import com.example.infiny.tracker_master.Interfaces.OnItemClickListener;
import com.example.infiny.tracker_master.Models.Target;
import com.example.infiny.tracker_master.R;
import com.example.infiny.tracker_master.TrackerMaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class Home extends AppCompatActivity {
    MaterialTapTargetPrompt mFabPrompt;
    SessionManager sessionManager;
    MenuItem menuItem;
    View view;
    ArrayList<Target> targetList;
    RecyclerView rvTargetList;
    TargetListAdapter targetListAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        targetList = new ArrayList<>();
        rvTargetList = (RecyclerView) findViewById(R.id.rvTargetList);

        targetListAdapter = new TargetListAdapter(this, targetList, new OnItemClickListener() {
            @Override
            public void onChatItemClick(Target targetItem) {
                Intent intent = new Intent(Home.this, TargetDetails.class);
                intent.putExtra("target",targetItem);
                startActivity(intent);
            }
        });

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvTargetList.setLayoutManager(mLayoutManager);
        rvTargetList.setItemAnimator(new DefaultItemAnimator());
        rvTargetList.setHasFixedSize(true);
        rvTargetList.setAdapter(targetListAdapter);

        new MaterialTapTargetPrompt.Builder(Home.this)
                .setTarget(findViewById(R.id.search_box))
                .setBackgroundColour(Color.parseColor("#009688"))
                .setPrimaryText("Here is an interesting feature")
                .setSecondaryText("Tap here and you will be surprised")
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener(){
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        //TODO: Store in SharedPrefs so you don't show this prompt again.
                    }
                    @Override
                    public void onHidePromptComplete(){
                        showSearchPrompt(view);
                    }
                })
                .show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menuItem =menu.findItem(R.id.add_contact);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_contact) {
            startActivity(new Intent(Home.this,AddTarget.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSearchPrompt(View view) {
        new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText("Add contacts")
                .setSecondaryText("")
                .setBackgroundColour(Color.parseColor("#009688"))
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_add_circle_outline_white_48dp)
                .setTarget(R.id.add_contact)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {

                    }

                    @Override
                    public void onHidePromptComplete() {
                        getTargets();

                    }
                })
                .show();
    }

    public void getTargets(){
        String url = Config.BASE_URL + "api/v1/targets_show_targets";
        if (ConnectivityReceiver.isConnected()) {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Getting targets...");
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
                                if (error == false) {
                                    JSONArray jsonResult = jsonObject.getJSONArray("result");
                                    if (jsonResult.length() == 0) {
//                                        nodata.setVisibility(View.VISIBLE);
                                    }
                                    for (int i = 0; i < jsonResult.length(); i++) {
                                        Target target = new Target();
                                        target.setFirstName(jsonResult.getJSONObject(i).getString("first_name"));
                                        target.setLastName(jsonResult.getJSONObject(i).getString("last_name"));
                                        target.setTrackingId(jsonResult.getJSONObject(i).getString("tracking_id"));
                                        target.setProfilePic(jsonResult.getJSONObject(i).getString("image").replaceAll("\\\\", ""));
                                        target.setPhoneNo(jsonResult.getJSONObject(i).getString("phone_no"));
                                        target.setEmail(jsonResult.getJSONObject(i).getString("email"));
                                        target.setTimeInterval(jsonResult.getJSONObject(i).getString("track_time_interval"));
                                        target.setTimeOut(jsonResult.getJSONObject(i).getString("track_time_out"));

                                        targetList.add(target);
                                        targetListAdapter.notifyDataSetChanged();
                                        if (targetListAdapter.getItemCount() > 1) {
                                            rvTargetList.getLayoutManager().smoothScrollToPosition(rvTargetList, null, 0);
                                        }
                                    }
                                    pDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                pDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            new ErrorVolleyHandler(Home.this).onErrorResponse(error);
                            pDialog.dismiss();
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> mHeaders = new ArrayMap<String, String>();
                    mHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    mHeaders.put("Accept", "application/json");
                    mHeaders.put("token", sessionManager.getAuth_token());

                    return mHeaders;
                }
            };

            TrackerMaster.getInstance().addToRequestQueue(request);

        } else {
            showSnack(ConnectivityReceiver.isConnected);
        }
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Connected to Internet.";
            color = Color.WHITE;
        } else {
            message = "Please check your internet connection & try again";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}
