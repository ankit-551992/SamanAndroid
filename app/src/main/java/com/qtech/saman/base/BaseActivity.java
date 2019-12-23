package com.qtech.saman.base;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.v7.app.AppCompatActivity;

import com.qtech.saman.R;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.utils.GlobalValues;

import java.util.Locale;

import static com.qtech.saman.utils.Constants.is_firebase_msgnotify;

public abstract class BaseActivity extends AppCompatActivity {


    public void startActivity(Intent intent, boolean withAnimation) {
        startActivity(intent);
        if (withAnimation) {
            overridePendingTransition(R.anim.slide_from_right, R.anim.stable);
        }
    }

    @Override
    public void onBackPressed() {
        onBackPressed(true);
    }

    public void onBackPressed(boolean withAnimation) {
        if (is_firebase_msgnotify) {
            is_firebase_msgnotify = false;
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }

        if (withAnimation) {
            overridePendingTransition(R.anim.stable, R.anim.slide_to_left);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            if (GlobalValues.getAppLanguage(newBase).equals("")) {
                super.attachBaseContext(wrap(newBase, Locale.getDefault().getLanguage()));
            } else {
                super.attachBaseContext(wrap(newBase, GlobalValues.getAppLanguage(newBase)));
            }
        } else {
            super.attachBaseContext(newBase);
        }
    }

    public ContextWrapper wrap(Context context, String language) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        Locale newLocale = new Locale(language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }

        /*Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);

        //store current language in prefrence
        //prefData.setCurrentLanguage(language);

        //With new configuration start activity again
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();*/
        return new ContextWrapper(context);
    }
}
