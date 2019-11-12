package com.qtech.saman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.login.LoginActivity;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.imageView_logo)
    ImageView logo;

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    public static boolean isEnglishVersion;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getCountries();

        Animation animation;
        animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_fade_in);
        logo.startAnimation(animation);

//        ShortcutBadger.applyCount(this,10);
//        ShortcutBadger.removeCount(this);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//              changelanguage();
                if (GlobalValues.getUserLoginStatus(SplashActivity.this)) {
                    GlobalValues.setGuestLoginStatus(SplashActivity.this, false);
                    Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    if (GlobalValues.getGuestLoginStatus(SplashActivity.this)) {
                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        GlobalValues.setGuestLoginStatus(SplashActivity.this, false);
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        mainIntent.putExtra("GuestTry", false);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

        // http://twitter.com/status/1234
//        Uri data = getIntent().getData();
//        Uri data = Uri.parse("http://test.com/status/1234");
//        Log.d("TAG", data.toString());
//        String scheme = data.getScheme(); // "http"
//        Log.d("TAG", scheme);
//        String host = data.getHost(); // "twitter.com"
//        Log.d("TAG", host);
//        String inurl = data.toString();
//        List<String> params = data.getPathSegments();
//        String first = params.get(0); // "status"
//        String second = params.get(1); // "1234"
//        Log.d("TAG", first);
//        Log.d("TAG", second);
    }

    private void changelanguage() {
        isEnglishVersion = true;

        if (GlobalValues.getAppLanguage(getApplicationContext()).equals("")) {
            GlobalValues.changeLanguage(Locale.getDefault().getLanguage(), getApplicationContext());
            if (Locale.getDefault().getLanguage().equals("ar")) {
                isEnglishVersion = false;
            }
        } else {
            GlobalValues.changeLanguage(GlobalValues.getAppLanguage(getApplicationContext()), getApplicationContext());
            if (GlobalValues.getAppLanguage(getApplicationContext()).equals("ar")) {
                isEnglishVersion = false;
            }
        }
    }

    private void getCountries() {
        GlobalValues.countries = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(Constants.loadJSONFromAsset(getApplicationContext()));
            JSONArray jsonArray = obj.getJSONArray("countries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Country country = new Country();

                country.setId(jsonObject.getInt("id"));
                country.setSortname(jsonObject.getString("sortname"));
                country.setName(jsonObject.getString("name"));
                country.setName_AR(jsonObject.getString("name_AR"));
                country.setFlag("https://www.saman.om/Flags/flag_" + jsonObject.getString("sortname").toLowerCase() + ".png");
                country.setPhoneCode("" + jsonObject.getInt("phoneCode"));

                GlobalValues.countries.add(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCountriesAPI() {
        GlobalValues.countries = new ArrayList<>();
        WebServicesHandler.instance.getCountries(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject JObject = new JSONObject(response.body().string());
                    int status = 0;
                    status = JObject.getInt("success");
                    if (status == 0) {
                        getCountriesAPI();
                    } else if (status == 1) {
                        if (JObject.has("result")) {
                            JSONArray jsonArray = JObject.getJSONArray("result");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Country country = new Country();

                                country.setId(jsonObject.getInt("ID"));
                                String flagURL = jsonObject.getString("FlagURL");

                                String[] array = flagURL.split("/");
                                String[] array2 = array[array.length - 1].split("\\.");
                                String[] array3 = array2[0].split("_");
                                String shortNameCode = array3[array3.length - 1];

                                country.setSortname(shortNameCode);
                                country.setName(jsonObject.getString("CountryName"));
                                country.setFlag(Constants.URLS.BaseURLImages + flagURL);
                                country.setPhoneCode(jsonObject.getString("CountryCode"));

                                GlobalValues.countries.add(country);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


}
