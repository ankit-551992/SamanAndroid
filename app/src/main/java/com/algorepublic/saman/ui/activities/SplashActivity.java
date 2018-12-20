package com.algorepublic.saman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.login.LoginActivity;
import com.algorepublic.saman.ui.activities.register.RegisterActivity;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        getCountries();
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(GlobalValues.getUserLoginStatus(SplashActivity.this)){
                    GlobalValues.setGuestLoginStatus(SplashActivity.this,false);
                    Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else {
                    if(GlobalValues.getGuestLoginStatus(SplashActivity.this)) {
                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }else {
                        GlobalValues.setGuestLoginStatus(SplashActivity.this,false);
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        mainIntent.putExtra("GuestTry",false);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    private void getCountries(){
        GlobalValues.countries=new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(Constants.loadJSONFromAsset(getApplicationContext()));
            JSONArray jsonArray = obj.getJSONArray("countries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Country country=new Country();

                country.setId(jsonObject.getInt("id"));
                country.setSortname(jsonObject.getString("sortname"));
                country.setName(jsonObject.getString("name"));
                country.setFlag("http://algorepublic-001-site2.etempurl.com/Flags/flag_"+jsonObject.getString("sortname").toLowerCase()+".png");
                country.setPhoneCode(jsonObject.getInt("phoneCode"));

                GlobalValues.countries.add(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
