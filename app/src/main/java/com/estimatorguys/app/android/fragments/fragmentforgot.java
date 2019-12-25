package com.estimatorguys.app.android.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
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

public class fragmentforgot extends Fragment implements View.OnClickListener {

    View contaionerview = null;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.email)
    TextView et_email;
    @BindView(R.id.tv_submit)
    TextView tv_submit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contaionerview == null) {
            contaionerview = inflater.inflate(R.layout.activity_forgotactivity, container, false);

            ButterKnife.bind(this, contaionerview);

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Helvetica-Neue-Bold.ttf");
            Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "AvenirNext-DemiBold.ttf");
            Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

            tv_back.setTypeface(font);
            tv_submit.setTypeface(font1);
            et_email.setTypeface(font2);


            tv_back.setOnClickListener(this);
            tv_submit.setOnClickListener(this);
        }
        return contaionerview;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.tv_submit:
                if (isValidEmail(et_email.getText())) {
                    makeForgotPasswordRequest("5");
                }else{
                    Toast.makeText(getActivity(), "Please provide a valid email", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void makeForgotPasswordRequest(String user_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", et_email.getText().toString());

        progressdialog.showwaitingdialog(getActivity());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.FORGOT_PASSWORD, params, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                progressdialog.dismisswaitdialog();
                try {
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    if (response.getString("message").equals("New password send has been sent to your email!!")){
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
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
