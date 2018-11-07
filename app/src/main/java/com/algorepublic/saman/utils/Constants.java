package com.algorepublic.saman.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Constants {


    public static String CARD_LIST="Payment_methods";

    public enum Fragment {
        Home,Store,Favorite,Bag,MyAccount;
    }

    public interface URLS {
//        String BaseURLApis = "https://petradiamond.herokuapp.com/";
        String BaseURLApis = "https://www.saman.om/api/";
        String BaseURLImages = "https://www.saman.om";
        String returnPolicy = "https://www.algorepublic.com/";
        String terms = "https://www.algorepublic.com/services/";
        String privacyPolicy = "https://www.algorepublic.com/services/";
        String contactUs = "https://www.algorepublic.com/contact-us/";
    }

    public static boolean isNetworkAvailable(Context c){
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static ArrayList<String> countriesList(){
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            Log.e("IOS",""+loc.getCountry());
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        return countries;
    }

    public static void showAlert(String title,String message,String buttonText,Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public static void showAlertWithActivityFinish(String title, String message, String buttonText, final Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((Activity)context).finish();
                    }
                });
        alertDialog.show();
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static ProgressDialog mSpinner;

    public static void showSpinner(String title,Context context) {
        if(context!=null) {
            mSpinner = new ProgressDialog(context);
            mSpinner.setTitle(title);
            mSpinner.setMessage("Please wait....");
            mSpinner.show();
            mSpinner.setCancelable(false);
            mSpinner.setCanceledOnTouchOutside(false);
        }
    }

    public static void dismissSpinner(){
        if(mSpinner!=null){
            mSpinner.dismiss();
        }
    }


    public static String strSeparator = ",";
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
