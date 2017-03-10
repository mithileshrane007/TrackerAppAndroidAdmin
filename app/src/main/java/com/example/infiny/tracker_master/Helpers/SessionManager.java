package com.example.infiny.tracker_master.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.infiny.tracker_master.Activities.Login;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class SessionManager {
    private static final String CURR_LAT = "latitude";
    private static final String CURR_LONG = "longitude";
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Login";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String email = "email";
    public static final String auth_token = "auth_token";
    public static final String company = "company";
    public static final String phone_number = "phone_number";
    public static final String user_id = "user_id";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public String getAuth_token() {
        return pref.getString(auth_token, "");
    }

    public String getEmail() {
        return pref.getString(email, "");
    }

    public String getCompany() {
        return pref.getString(company, "");
    }

    public String getPhone_number() {
        return pref.getString(phone_number, "");
    }

    public String getUserID() {
        return pref.getString(user_id, "");
    }


    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String user_id, String email, String phone_number, String company, String auth_token) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(SessionManager.email, email);
        editor.putString(SessionManager.phone_number, phone_number);
        editor.putString(SessionManager.company, company);
        editor.putString(SessionManager.user_id, user_id);
        editor.putString(SessionManager.auth_token,auth_token);
        editor.commit();
    }

    public void saveUserId(String id) {
        editor.putString(user_id, id);
        editor.commit();
    }

    public HashMap<String, String> getData() {
        HashMap<String, String> b;
        b = (HashMap<String, String>) pref.getAll();

        return b;

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);            // Add new1 Flag to start new1 Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities

        // Add new1 Flag to start new1 Activity

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void storeLocation(double lat, double longi) {
        editor.putString(CURR_LAT, String.valueOf(lat));
        editor.putString(CURR_LONG, String.valueOf(longi));
        editor.commit();

    }

    public LatLng getStoredLocation() {
        return new LatLng(Double.valueOf(pref.getString(CURR_LAT, null).trim()), Double.valueOf(pref.getString(CURR_LONG, null).trim()));
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}