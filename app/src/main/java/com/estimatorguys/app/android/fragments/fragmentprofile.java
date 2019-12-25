package com.estimatorguys.app.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.estimatorguys.app.android.Activities.MainActivity;
import com.estimatorguys.app.android.Activities.SplashActivity;
import com.estimatorguys.app.android.Database.Session_management;
import com.estimatorguys.app.android.R;
import com.estimatorguys.app.android.utils.AppController;
import com.estimatorguys.app.android.utils.BaseURL;
import com.estimatorguys.app.android.utils.CustomVolleyJsonRequest;
import com.estimatorguys.app.android.utils.progressdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.estimatorguys.app.android.utils.Config.KEY_EMAIL;
import static com.estimatorguys.app.android.utils.Config.KEY_ID;

public class fragmentprofile extends Fragment implements View.OnClickListener{

    @BindView(R.id.tv_update)
    TextView tv_update;
    @BindView(R.id.firstname)
    TextView et_firstname;
    @BindView(R.id.username)
    TextView et_username;
    @BindView(R.id.email)
    TextView et_email;
    @BindView(R.id.password)
    TextView et_password;
    @BindView(R.id.signupheading)
    TextView tv_heading;
    @BindView(R.id.marketing)
    SwitchCompat sc_marketing;
    @BindView(R.id.marketing_consent)
    TextView marketing_consent;


    String role_id="0";
    private String android_id=null;
    String userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Helvetica-Neue-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "AvenirNext-DemiBold.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        //tv_back.setTypeface(font);
        tv_update.setTypeface(font1);
        et_firstname.setTypeface(font2);
        et_username.setTypeface(font2);
        et_email.setTypeface(font2);
        et_password.setTypeface(font2);
        sc_marketing.setTypeface(font2);
        marketing_consent.setTypeface(font2);
        tv_heading.setTypeface(font2);


        //tv_back.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        et_password.setTransformationMethod(new PasswordTransformationMethod());
        sc_marketing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    role_id = "1";
                } else {
                    role_id = "0";
                }
            }
        });

        android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Session_management sessionManagement = new Session_management(getActivity());
        userid = sessionManagement.getUserCredentials().get(KEY_ID);
        getAccountDetails(userid);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:;

                break;
            case R.id.tv_update:
                if (!TextUtils.isEmpty(et_firstname.getText()) && !TextUtils.isEmpty(et_username.getText()) && !TextUtils.isEmpty(et_password.getText())) {
                    if (isValidEmail(et_email.getText())) {
                        makeupdateAccountRequest(userid);
                    }else{
                        Toast.makeText(getActivity(), "Please provide a valid email", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please provide complete information", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void getAccountDetails(String user_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        progressdialog.showwaitingdialog(getActivity());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GETPROFILE, params, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                progressdialog.dismisswaitdialog();
                try {
                    JSONObject obj = new JSONObject(response.getString("data"));
                    et_firstname.setText(obj.getString("name"));
                    et_username.setText(obj.getString("username"));
                    et_email.setText(obj.getString("email"));
                    et_password.setText("");
                    if (obj.getInt("marketing_consent")==1) {
                        sc_marketing.setChecked(true);
                    }else {
                        sc_marketing.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressdialog.dismisswaitdialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void makeupdateAccountRequest(String user_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("username", et_username.getText().toString());
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());
        params.put("name", et_firstname.getText().toString());
        params.put("marketing_consent", role_id);

        progressdialog.showwaitingdialog(getActivity());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.EDITPROFILE, params, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                progressdialog.dismisswaitdialog();
                try {
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    if (response.getString("message").equals("User has been updated successfully!")){
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                progressdialog.dismisswaitdialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}

