package com.algorepublic.saman.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.data.model.StoreCategory;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GlobalValues {



    public static List<StoreCategory> storeCategories;
    public static List<Country> countries;

    public static boolean orderPlaced=false;

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


    public static void setSelectedCountry(Context ctx, String countryCode){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SelectedCountry",countryCode);
        editor.apply();
    }

    public static String getSelectedCountry(Context ctx){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getString("SelectedCountry", "OM");
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

    public static String convertHashToString(HashMap<String, Object> hashMap){
        Gson gson=new Gson();
        String Json=gson.toJson(hashMap);
        return Json;
    }

    public static HashMap<String,Object> stringToHashMap(String storedHashMapString){
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> hashMap = new Gson().fromJson(storedHashMapString, type);
        return hashMap;
    }

    public static void markFavourite(int userID,int productId){
        WebServicesHandler.instance.markFavourite(userID,productId,new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                if (response!=null) {
                    Log.e("RES", response.toString());
                }
            }
            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {

            }
        });
    }

    public static void markUnFavourite(int userID,int productId){
        WebServicesHandler.instance.markUnFavourite(userID,productId,new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                if (response!=null) {
                    Log.e("RES", response.toString());
                }
            }
            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {

            }
        });
    }
}