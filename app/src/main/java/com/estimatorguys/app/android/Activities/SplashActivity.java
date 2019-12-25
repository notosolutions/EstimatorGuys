package com.estimatorguys.app.android.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.estimatorguys.app.android.Database.Session_management;
import com.estimatorguys.app.android.R;

import static com.estimatorguys.app.android.utils.Config.KEY_EMAIL;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR, WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        setContentView(R.layout.activity_splash);

        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)
                    /*Intent openstartingpoint=new Intent("x.y.z.START");
                    startActivity(openstartingpoint);*/
                    Session_management sessionManagement = new Session_management(SplashActivity.this);
                    String email = sessionManagement.getUserCredentials().get(KEY_EMAIL);
                    if (email!=null){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
