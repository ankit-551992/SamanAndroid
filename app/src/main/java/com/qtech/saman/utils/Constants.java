package com.qtech.saman.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.login.LoginActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Constants {

    public static Dialog dialog;
    public static String CARD_LIST = "Payment_methods";
    public static boolean isLoginRequest = false;
    public static boolean viewBag = false;
    public static int USERSUPPORT_REQUEST_CODE = 100;


    public enum Fragment {
        Home, Store, Favorite, Bag, MyAccount;
    }

    public static boolean is_firebase_msgnotify = false;

    public interface URLS {
        //        String BaseURLApis = "https://petradiamond.herokuapp.com/";
        // String BaseURLApis = "https://www.saman.om/api/";

        //String BaseURLApis = "http://96.127.174.114/plesk-site-preview/staging.saman.om/api/";
        // String BaseURLImages = "https://www.saman.om";
        //String BaseURLImages = "http://96.127.174.114/plesk-site-preview/staging.saman.om";

        //new staging App Login
//        String BaseURLApis = "https://staging.saman.om/api/";
        String BaseURLApis = "https://samanapp.azurewebsites.net/api/";
        String BaseURLImages = "https://staging.saman.om";
        String Invoice_url = "https://staging.saman.om/Order/Invoice/";


        String GeoCodeApis = "https://maps.googleapis.com/maps/api/geocode/";
        String returnPolicy = "https://www.algorepublic.com/";
        String terms = "https://www.algorepublic.com/services/";
        String privacyPolicy = "https://www.algorepublic.com/services/";
        String contactUs = "https://www.algorepublic.com/contact-us/";
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static ArrayList<String> countriesList() {
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            Log.e("IOS", "" + loc.getCountry());
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        return countries;
    }

    public static void showLoginDialog(final Context mContext) {
        dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_message);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);
        Button continueLogin = (Button) dialog.findViewById(R.id.button_continue);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        continueLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                isLoginRequest = true;
                Intent mainIntent = new Intent(mContext, LoginActivity.class);
                mainIntent.putExtra("GuestTry", true);
                mContext.startActivity(mainIntent);
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    public static void showAlert(String title, String message, String buttonText, Context context) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(message);
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();

        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_customer_support);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Animation animation;
            animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);

            ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        }
        dialog.show();

        TextView titleTextView = dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = dialog.findViewById(R.id.tv_pop_up_message);
        ImageView close = dialog.findViewById(R.id.iv_pop_up_close);
        Button nextButton = dialog.findViewById(R.id.button_pop_next);

        nextButton.setText(buttonText);
//      titleTextView.setText(context.getString(R.string.error));
        if (title != null && !title.equals("")) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        } else {
            titleTextView.setVisibility(View.GONE);
        }

        messageTextView.setText(message);
//      messageTextView.setText(context.getString(R.string.missing_options));

        close.setOnClickListener(view -> dialog.dismiss());

        nextButton.setOnClickListener(view -> dialog.dismiss());
    }

    public static void showAlertWithActivityFinish(String title, String message, String buttonText, final Context context) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(message);
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        ((Activity) context).finish();
//                    }
//                });
//        alertDialog.show();
        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_customer_support);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView titleTextView = dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = dialog.findViewById(R.id.tv_pop_up_message);
        ImageView close = dialog.findViewById(R.id.iv_pop_up_close);
        Button nextButton = dialog.findViewById(R.id.button_pop_next);

        nextButton.setText(buttonText);
//        titleTextView.setText(context.getString(R.string.error));
        if (title.equals("")) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }

        messageTextView.setText(message);
//      messageTextView.setText(context.getString(R.string.missing_options));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
                dialog.dismiss();
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();

    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
//            InputStream is = context.getAssets().open("countries_old.json");
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

    public static void showSpinner(String title, Context context) {
        if (context != null) {
            mSpinner = new ProgressDialog(context);
            mSpinner.setTitle(title);
            mSpinner.setMessage(context.getString(R.string.please_wait));
            mSpinner.show();
            mSpinner.setCancelable(false);
            mSpinner.setCanceledOnTouchOutside(false);
        }
    }

    public static void dismissSpinner() {
        if (mSpinner != null && mSpinner.isShowing()) {
            mSpinner.dismiss();
        }
    }

    public static String strSeparator = ",";

    public static String convertArrayToString(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            // Do not append comma at the end of last element
            if (i < array.length - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static String[] convertStringToArray(String str) {
        String[] arr = str.split(strSeparator);
        return arr;
    }

    public static void showCustomPopUp(Context mContext, String title, String message, String closeButtonText, String nextButtonText, final int type, int is_continue) {
        // view favourite & add to cart dialog
        dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_information_pop_up);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.iv_pop_up_close);
        Button closePopUp = dialog.findViewById(R.id.button_close_pop_up);
        Button nextButton = dialog.findViewById(R.id.button_pop_next);
        TextView titleTextView = dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = dialog.findViewById(R.id.tv_pop_up_message);

        titleTextView.setText(title);
        messageTextView.setText(message);
        closePopUp.setText(closeButtonText);
        nextButton.setText(nextButtonText);

        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_continue == 1) {
                    dialog.dismiss();
                } else if (is_continue == 0) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("NavItem", 0);
                    ((Activity) mContext).startActivity(intent);
                } else {
                    dialog.dismiss();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    dialog.dismiss();
                    Constants.viewBag = true;
                    Intent intent = new Intent(mContext, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("NavItem", 3);
                    ((Activity) mContext).startActivity(intent);
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("NavItem", 2);
                    ((Activity) mContext).startActivity(intent);
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(mContext,
                R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    public static void showErrorPopUp(Context context, String title, String message, String nextButtonText) {
        // show error for select all option
        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_customer_support);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView titleTextView = dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = dialog.findViewById(R.id.tv_pop_up_message);
        ImageView close = dialog.findViewById(R.id.iv_pop_up_close);
        Button nextButton = dialog.findViewById(R.id.button_pop_next);

        nextButton.setText(nextButtonText);
//        titleTextView.setText(context.getString(R.string.error));
        if (title == null) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
        messageTextView.setText(message);
//        messageTextView.setText(context.getString(R.string.missing_options));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    /*@Override
    public void onDialogClick(Context context, String title, String message,String closeButtonText, String nextButtonText, final int type) {
        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_information_pop_up);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_pop_up_close);
        Button closePopUp = (Button) dialog.findViewById(R.id.button_close_pop_up);
        Button nextButton = (Button) dialog.findViewById(R.id.button_pop_next);
        TextView titleTextView = (TextView) dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.tv_pop_up_message);

        titleTextView.setText("Are you sure?");
        messageTextView.setText("Do you really want to place order?");
        closePopUp.setText(closeButtonText);
        nextButton.setText(nextButtonText);

        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new ShoppingCartActivity().OnResponseDialogClick(context,"nextclick");
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(context,
                R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }
*/
}
