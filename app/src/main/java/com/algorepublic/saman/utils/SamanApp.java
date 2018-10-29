package com.algorepublic.saman.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.algorepublic.saman.BuildConfig;
import com.algorepublic.saman.db.MySQLiteHelper;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import io.fabric.sdk.android.Fabric;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SamanApp extends MultiDexApplication {

    public final String TWITTER_KEY = "zYtdb8neQYt8ph1XoyPmwNWIR";
    public final String TWITTER_SECRET = "a5Jo7xEwsNh4BhuHodycFOILfV0OEBkNOHNikt5Cf3VhABSxnp";

    public static TinyDB db;
    public static MySQLiteHelper localDB;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Crashlytics crashlytics=new Crashlytics.Builder().core(core).build();
        Fabric.with(this, crashlytics);
        db=new TinyDB(this);
        localDB=new MySQLiteHelper(this);

//        if(GlobalValues.getAppLanguage(getApplicationContext()).equals("")){
            GlobalValues.setAppLanguage(getApplicationContext(), Locale.getDefault().getLanguage());
//        }

        GlobalValues.changeLanguage(GlobalValues.getAppLanguage(getApplicationContext()),getApplicationContext());


//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "font/neo_sans.ttf");


        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
    }

    private void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash Key",hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("HashKey", "Error", e);
        } catch (Exception e) {
            Log.e("HashKey", "Error", e);
        }
    }
}
