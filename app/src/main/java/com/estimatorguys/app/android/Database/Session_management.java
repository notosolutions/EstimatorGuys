package com.estimatorguys.app.android.Database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.estimatorguys.app.android.Activities.LandingActivity;

import java.util.HashMap;

import static com.estimatorguys.app.android.utils.Config.IS_LOGIN;
import static com.estimatorguys.app.android.utils.Config.KEY_CREATED;
import static com.estimatorguys.app.android.utils.Config.KEY_EMAIL;
import static com.estimatorguys.app.android.utils.Config.KEY_ID;
import static com.estimatorguys.app.android.utils.Config.KEY_IMAGE;
import static com.estimatorguys.app.android.utils.Config.KEY_MAKE;
import static com.estimatorguys.app.android.utils.Config.KEY_MODEL;
import static com.estimatorguys.app.android.utils.Config.KEY_MODIFIED;
import static com.estimatorguys.app.android.utils.Config.KEY_NAME;
import static com.estimatorguys.app.android.utils.Config.KEY_NOTE;
import static com.estimatorguys.app.android.utils.Config.KEY_PASSWORD;
import static com.estimatorguys.app.android.utils.Config.KEY_ROLE_ID;
import static com.estimatorguys.app.android.utils.Config.KEY_STATUS;
import static com.estimatorguys.app.android.utils.Config.KEY_USER_NAME;
import static com.estimatorguys.app.android.utils.Config.KEY_VIN;
import static com.estimatorguys.app.android.utils.Config.KEY_YEAR;
import static com.estimatorguys.app.android.utils.Config.PREFS_NAME;
import static com.estimatorguys.app.android.utils.Config.PREFS_NAME2;
import static com.estimatorguys.app.android.utils.Config.PREFS_NAME3;

/**
 * Created by Bhuvnesh on 04/10/2019.
 */

public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;
    SharedPreferences prefs3;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor3;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();

        prefs3 = context.getSharedPreferences(PREFS_NAME3, PRIVATE_MODE);
        editor3 = prefs3.edit();
    }

    public void createLoginSession(String user_id, String full_name, String email, String role_id
            , String user_name, String status, String created, String modified) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, user_id);
        editor.putString(KEY_NAME, full_name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE_ID, role_id);
        editor.putString(KEY_USER_NAME, user_name);
        editor.putString(KEY_STATUS, status);
        editor.putString(KEY_CREATED, created);
        editor.putString(KEY_MODIFIED, modified);

        editor.commit();
    }

    public void checkLogin(Activity activity) {

        if (!this.isLoggedIn()) {

            Intent loginsucces = new Intent(context, LandingActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_USER_NAME, prefs.getString(KEY_USER_NAME, null));
        user.put(KEY_ROLE_ID, prefs.getString(KEY_ROLE_ID, null));
        user.put(KEY_STATUS, prefs.getString(KEY_STATUS, null));

        return user;
    }

    /**
     * Update stored session data
     */
    public void updateData(String name, String username, String email
            , String roleid, String status, String image) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USER_NAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE_ID, roleid);
        editor.putString(KEY_STATUS, status);

        editor.apply();
    }

    public void updatePassword(String password) {
        //editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_PASSWORD, password);

        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();
        Intent logout = new Intent(context, LandingActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();

        cleardatetime();
        Intent logout = new Intent(context, LandingActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
    }


    public void createRemember(String email, String password){
        editor3.putString(KEY_EMAIL, email);
        editor3.putString(KEY_PASSWORD, password);
        editor3.commit();

    }

    public HashMap<String, String> getUserCredentials() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_EMAIL, prefs3.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, prefs3.getString(KEY_PASSWORD, null));
        user.put(KEY_ID, prefs.getString(KEY_ID, null));

        return user;
    }

    public void createInvoice(String year, String make, String model, String vin, String note, String image) {
        editor2.putString(KEY_YEAR, year);
        editor2.putString(KEY_MAKE, make);
        editor2.putString(KEY_MODEL, model);
        editor2.putString(KEY_VIN, vin);
        editor2.putString(KEY_NOTE, note);
        editor2.putString(KEY_IMAGE, image);
        editor2.commit();
    }

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

    public HashMap<String, String> getYearMakeModel() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_YEAR, prefs2.getString(KEY_YEAR, null));
        user.put(KEY_MAKE, prefs2.getString(KEY_MAKE, null));
        user.put(KEY_MODEL, prefs2.getString(KEY_MODEL, null));

        return user;
    }
}
