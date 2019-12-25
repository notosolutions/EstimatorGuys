package com.estimatorguys.app.android.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.estimatorguys.app.android.R;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ThankyouActivity extends Activity implements View.OnClickListener {


    @BindView(R.id.tv_done)
    TextView tv_done;
    @BindView(R.id.thankstitle)
    TextView tv_thankstitle;
    @BindView(R.id.thanksdetails)
    TextView tv_thanksdetails;
    @BindView(R.id.tv_back)
    TextView tv_back;

    View contaionerview = null;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR, WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        setContentView(R.layout.activity_thankyou);
        ButterKnife.bind(this);

        Typeface font1 = Typeface.createFromAsset(this.getAssets(), "AvenirNext-DemiBold.ttf");
        Typeface font2 = Typeface.createFromAsset(this.getAssets(), "Roboto-Regular.ttf");
        tv_thankstitle.setTypeface(font1);
        tv_thanksdetails.setTypeface(font2);
        tv_done.setTypeface(font1);
        tv_back.setVisibility(View.GONE);

        tv_done.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_done:
                this.finish();
                break;
        }
    }
}

