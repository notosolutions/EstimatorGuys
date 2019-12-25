package com.estimatorguys.app.android.fragments;



import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.estimatorguys.app.android.Activities.MainActivity;
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

public class fragmentsignin extends Fragment implements View.OnClickListener{

    View contaionerview = null;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_forgot)
    TextView tv_forgot;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.email)
    TextView et_email;
    @BindView(R.id.password)
    TextView et_password;
    @BindView(R.id.rememberme)
    CheckBox cb_rememberme;



    fragmentforgot forgotfragment;
    FragmentTransaction transaction;

    String user_id, full_name, email, role_id, user_name, status, created, modified;
    boolean checked = false;
    int marketingchecked = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contaionerview == null) {
            contaionerview = inflater.inflate(R.layout.activity_loginactivity, container, false);

            ButterKnife.bind(this, contaionerview);
            et_password.setTransformationMethod(new PasswordTransformationMethod());

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Helvetica-Neue-Bold.ttf");
            Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "AvenirNext-DemiBold.ttf");
            Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
            tv_back.setTypeface(font);
            et_email.setTypeface(font2);
            et_password.setTypeface(font2);
            cb_rememberme.setTypeface(font2);
            tv_submit.setTypeface(font1);
            tv_forgot.setTypeface(font2);

            tv_back.setOnClickListener(this);
            tv_submit.setOnClickListener(this);
            tv_forgot.setOnClickListener(this);
            cb_rememberme.setOnClickListener(this);
        }
        return contaionerview;

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.tv_forgot:
                /*transaction = getActivity().getSupportFragmentManager().beginTransaction();
                forgotfragment = new fragmentforgot();
                transaction.replace(R.id.fragment_container,forgotfragment);
                //transaction.addToBackStack("signingfragment");
                transaction.commit();*/

                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                forgotfragment = new fragmentforgot();
                transaction.add(R.id.fragment_container, forgotfragment, "forgotfragment").addToBackStack(null).commit();
                break;
            case R.id.tv_submit:
                if (isValidEmail(et_email.getText())) {
                    makeLoginAccountRequest();
                }else{
                    Toast.makeText(getActivity(), "Please provide a valid email", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rememberme:
                if (cb_rememberme.isChecked()){
                    checked = true;
                }else {
                    checked = false;
                }
        }
    }

    private void makeLoginAccountRequest() {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());


        progressdialog.showwaitingdialog(getActivity());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.LOGIN_USER, params, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                progressdialog.dismisswaitdialog();
                try {
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    if (response.getString("message").equals("Login successfully!!")){

                        JSONObject obj = new JSONObject(response.getString("user"));
                        user_id = obj.getString("user_id");
                        full_name = obj.getString("name");
                        email = obj.getString("email");
                        role_id = obj.getString("role_id");
                        user_name = obj.getString("username");
                        status = obj.getString("status");
                        created = obj.getString("created");
                        modified = obj.getString("modified");
                        Session_management sessionManagement = new Session_management(getActivity());
                        sessionManagement.createLoginSession(user_id, full_name, email, role_id, user_name, status, created, modified);
                        if (checked){
                            sessionManagement.createRemember(email, et_password.getText().toString());
                        }
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
