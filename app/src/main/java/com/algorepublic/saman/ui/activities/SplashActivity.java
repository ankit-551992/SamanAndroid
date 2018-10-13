package com.algorepublic.saman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.login.LoginActivity;
import com.algorepublic.saman.utils.GlobalValues;

public class SplashActivity extends BaseActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(GlobalValues.getUserLoginStatus(SplashActivity.this)){
                    Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(mainIntent);
                    finish();

                }else {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
