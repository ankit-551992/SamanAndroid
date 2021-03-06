package com.qtech.saman.utils;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.qtech.saman.db.MySQLiteHelper;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.security.MessageDigest;
import java.util.Locale;

public class SamanApp extends MultiDexApplication {

//    public final String TWITTER_KEY = "zYtdb8neQYt8ph1XoyPmwNWIR";
//    public final String TWITTER_SECRET = "a5Jo7xEwsNh4BhuHodycFOILfV0OEBkNOHNikt5Cf3VhABSxnp";
//    public final String TWITTER_KEY = "znuy2ISz592d0rOCumQ1vSA96";
//    public final String TWITTER_SECRET = "XXpiOWuV2DUKyPyAvXDZDViu90oTHe7DObtDbBNtVSubBXMscU";

    //new key
    public final String TWITTER_KEY = "gsNtS7EDk2EjnLMjxJOpaPgiK";
    public final String TWITTER_SECRET = "mkZ38c5qTuaOzn5WK71wc426qw7ns0em8D6zf1vyCNEgB0rP55";

    private static SamanApp instance;
    public static TinyDB db;
    public static MySQLiteHelper localDB;
    public static boolean isEnglishVersion;
    public static boolean isScreenOpen = false;

    public static SamanApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        db = new TinyDB(this);
        localDB = new MySQLiteHelper(this);
        instance = this;
        isEnglishVersion = true;

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

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        printHashKey();
        FirebaseAnalytics.getInstance(this);
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash Key", hashKey);
            }
        } catch (Exception e) {
            Log.e("HashKey", "Error", e);
        }
    }

}
