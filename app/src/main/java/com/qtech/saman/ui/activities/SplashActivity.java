package com.qtech.saman.ui.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.ApiViewCount;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.data.model.User;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.login.LoginActivity;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.qtech.saman.utils.SamanApp.isEnglishVersion;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.imageView_logo)
    ImageView logo;

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2500;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (GlobalValues.getAppLanguage(getApplicationContext()).equals("")) {
            GlobalValues.changeLanguage(Locale.getDefault().getLanguage(), getApplicationContext());
            if (Locale.getDefault().getLanguage().equals("ar")) {
                Log.e("TAG", "onCreate:--if " + isEnglishVersion);
                isEnglishVersion = false;
            }
        } else {
            GlobalValues.changeLanguage(GlobalValues.getAppLanguage(getApplicationContext()), getApplicationContext());
            if (GlobalValues.getAppLanguage(getApplicationContext()).equals("ar")) {
                Log.e("TAG", "onCreate:--else " + isEnglishVersion);
                isEnglishVersion = false;
            }
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getCountries();

        setAppViewCountApi();

        Animation animation;
        animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_fade_in);
        logo.startAnimation(animation);
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.qtech.saman",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        ShortcutBadger.applyCount(this,10);
//        ShortcutBadger.removeCount(this);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (GlobalValues.getUserLoginStatus(SplashActivity.this)) {
                    GlobalValues.setGuestLoginStatus(SplashActivity.this, false);
                    User user = GlobalValues.getUser(SplashActivity.this);
                    Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                    if (user.getPhoneNumber() == null || user.getShippingAddress().getAddressLine1() == null || user.getCountry() == null) {
                        mainIntent.putExtra("NavItem", 0);
                        mainIntent.putExtra("OpenDetails", true);
                    } else if (user.getPhoneNumber().isEmpty() || user.getShippingAddress().getAddressLine1().isEmpty() || user.getCountry().isEmpty()
                            || user.getPhoneNumber().equalsIgnoreCase("") || user.getShippingAddress().getAddressLine1().equalsIgnoreCase("") || user.getCountry().equalsIgnoreCase("")) {
                        mainIntent.putExtra("NavItem", 0);
                        mainIntent.putExtra("OpenDetails", true);
                    }
                    startActivity(mainIntent);
                    finish();
                } else {
                    if (GlobalValues.getGuestLoginStatus(SplashActivity.this)) {
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

    private void setAppViewCountApi() {

        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.getAppViewCount(new Callback<ApiViewCount>() {
            @Override
            public void onResponse(Call<ApiViewCount> call, Response<ApiViewCount> response) {
                Log.e("onSuccess", "response-----" + response.body());
            }

            @Override
            public void onFailure(Call<ApiViewCount> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
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
}
