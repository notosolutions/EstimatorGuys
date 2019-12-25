package com.estimatorguys.app.android.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.estimatorguys.app.android.R;
import com.estimatorguys.app.android.fragments.fragmentsignin;
import com.estimatorguys.app.android.fragments.fragmentsignup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener{


    @BindView(R.id.rootview)
    RelativeLayout root_view;
    @BindView(R.id.tv_signup)
    TextView btn_signup;
    @BindView(R.id.tv_signin)
    TextView tv_signin;
    @BindView(R.id.landingcontent)
    RelativeLayout Relay_landing;
    @BindView(R.id.fragment_container)
    FrameLayout frameLayout_container;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.text4)
    TextView text4;

    fragmentsignup signupfragment;
    fragmentsignin signinfragment;
    FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR, WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);

        Typeface font = Typeface.createFromAsset(getAssets(), "Helvetica-Neue-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "AvenirNext-DemiBold.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

        btn_signup.setTypeface(font1);
        tv_signin.setTypeface(font2);
        text1.setTypeface(font2);
        text2.setTypeface(font2);
        text3.setTypeface(font2);
        text4.setTypeface(font2);

        setsigninclickabletext(tv_signin);
        btn_signup.setOnClickListener(this);
    }

    public void setsigninclickabletext(TextView tv_signin){

        SpannableString ss = new SpannableString(tv_signin.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                transaction = getSupportFragmentManager().beginTransaction();
                signinfragment = new fragmentsignin();
                transaction.add(R.id.fragment_container,signinfragment,"signingfragment").addToBackStack(null).commit();
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
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_signup: {
                transaction = getSupportFragmentManager().beginTransaction();
                signupfragment = new fragmentsignup();
                transaction.add(R.id.fragment_container,signupfragment,"signupfragment").addToBackStack(null).commit();
                //Relay_landing.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }


    public void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}