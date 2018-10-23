package com.algorepublic.saman.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.algorepublic.saman.data.model.StoreCategory;
import com.algorepublic.saman.data.model.User;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class GlobalValues {



    public static List<StoreCategory> storeCategories;

    public static void setUserLoginStatus(Context ctx, boolean isLogin){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("UserLoginStatus",isLogin);
        editor.apply();
    }

    public static boolean getUserLoginStatus(Context ctx){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getBoolean("UserLoginStatus", false);
    }


    public static void saveUser(Context context, User user){
        SharedPreferences  mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("User", json);
        prefsEditor.apply();
    }

    public static User getUser(Context context){
        SharedPreferences  mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        return  gson.fromJson(json, User.class);
    }


    public static void setAppLanguage(Context ctx, String language){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AppLanguage",language);
        editor.apply();
    }

    public static String getAppLanguage(Context ctx){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getString("AppLanguage", "");
    }

    public static void changeLanguage(String language,Context context){
        String languageToLoad  = language; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }
    public static String convertListToString(List myLit){
        Gson gson=new Gson();
        String Json=gson.toJson(myLit);
        return Json;
    }
}
