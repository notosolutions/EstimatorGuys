package com.estimatorguys.app.android.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
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

public class fragmentsignup extends Fragment implements View.OnClickListener {

    View contaionerview=null;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_signin)
    TextView tv_signin;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
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

    fragmentsignin signinfragment;
    FragmentTransaction transaction;

    String role_id=null;
    private String android_id=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contaionerview == null) {
            contaionerview = inflater.inflate(R.layout.activity_signupactivity, container, false);
            ButterKnife.bind(this, contaionerview);

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Helvetica-Neue-Bold.ttf");
            Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "AvenirNext-DemiBold.ttf");
            Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
            tv_back.setTypeface(font);
            tv_submit.setTypeface(font1);
            et_firstname.setTypeface(font2);
            et_username.setTypeface(font2);
            et_email.setTypeface(font2);
            et_password.setTypeface(font2);
            sc_marketing.setTypeface(font2);
            marketing_consent.setTypeface(font2);
            tv_signin.setTypeface(font2);
            tv_heading.setTypeface(font2);

            setsigninclickabletext(tv_signin);
            tv_back.setOnClickListener(this);
            tv_submit.setOnClickListener(this);

            Spanned text = Html.fromHtml("<a href='http://notosolutions.net/estimator/marketingconsent'>Marketing Consent</a>");
            marketing_consent.setMovementMethod(LinkMovementMethod.getInstance());
            marketing_consent.setHighlightColor(Color.TRANSPARENT);
            marketing_consent.setText(text);

            stripUnderlines(marketing_consent);

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
            android_id = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        }
        return contaionerview;
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.tv_submit:
                if (!TextUtils.isEmpty(et_firstname.getText()) && !TextUtils.isEmpty(et_username.getText()) && !TextUtils.isEmpty(et_password.getText())) {
                    if (isValidEmail(et_email.getText())) {
                        makeRegisterAccountRequest("5");
                    }else{
                        Toast.makeText(getActivity(), "Please provide a valid email", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please provide complete information", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void setsigninclickabletext(TextView tv_signin) {

        SpannableString ss = new SpannableString(tv_signin.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                signinfragment = new fragmentsignin();
                transaction.add(R.id.fragment_container, signinfragment, "signingfragment").addToBackStack(null).commit();
                //Relay_landing.setVisibility(View.GONE);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.white));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 1, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_signin.setText(ss);
        tv_signin.setMovementMethod(LinkMovementMethod.getInstance());
        tv_signin.setHighlightColor(Color.TRANSPARENT);
    }




    private void makeRegisterAccountRequest(String id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());
        params.put("name", et_firstname.getText().toString());
        params.put("device_id", android_id);
        params.put("role_id", role_id);
        params.put("marketing_consent", role_id);
        params.put("username", et_username.getText().toString());
        params.put("status", "1");

        progressdialog.showwaitingdialog(getActivity());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REGISTER_USER, params, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                progressdialog.dismisswaitdialog();
                try {
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    if (response.getString("message").equals("User has been saved!!")){
                        String user_id,full_name,email,user_name,status,created,modified;

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