package com.qtech.saman.ui.activities.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.ChangeLanguage;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.PoliciesActivity;
import com.qtech.saman.ui.activities.SplashActivity;
import com.qtech.saman.ui.activities.country.CountriesListingActivity;
import com.qtech.saman.ui.activities.password.ChangePasswordActivity;
import com.qtech.saman.utils.CircleTransform;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.iv_country_flag)
    ImageView countryFlag;
    @BindView(R.id.tv_language)
    TextView languageTextView;
    @BindView(R.id.switchImage)
    SwitchCompat notificationSwitchCompat;
    @BindView(R.id.tv_notify)
    LinearLayout tv_notify;

    Country selectedCountry;

    Locale myLocale;
    String currentLanguage = "en", currentLang;
    Dialog dialog;
    String selectedLanguage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.title_settings));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        if (GlobalValues.countries != null) {
            for (int i = 0; i < GlobalValues.countries.size(); i++) {
                if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(SettingsActivity.this))) {
                    selectedCountry = GlobalValues.countries.get(i);
                    Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                }
            }
        }

        notificationSwitchCompat.setChecked(GlobalValues.getNotificationOnOff(this));
        notificationSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GlobalValues.setNotificationOnOff(SettingsActivity.this, true);
                } else {
                    GlobalValues.setNotificationOnOff(SettingsActivity.this, false);
                }
            }
        });

        if (GlobalValues.getAppLanguage(this).equals("ar")) {
            languageTextView.setText(getString(R.string.arabic));
        } else {
            languageTextView.setText(getString(R.string.english));
        }
    }

    @OnClick(R.id.tv_notify)
    public void notificationOption() {
        Intent intent = new Intent(SettingsActivity.this, NotifyOptionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(SettingsActivity.this, CountriesListingActivity.class);
        startActivityForResult(intent, 1299);
    }

    @OnClick(R.id.layout_language)
    public void languageSelection() {
        selectLanguage();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
      /*  Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);*/
    }

    @OnClick(R.id.tv_change_password)
    void changePassword() {
        User user = GlobalValues.getUser(this);

        if (user.getLogin_Type() != 2) {
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, R.string.password_cant_change, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_privacy_policy)
    void privacy() {
        Intent intent = new Intent(SettingsActivity.this, PoliciesActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
//        new FinestWebView.Builder(SettingsActivity.this).show(Constants.URLS.privacyPolicy);
    }

    @OnClick(R.id.tv_terms_of_uses)
    void termsOfUses() {
        Intent intent = new Intent(SettingsActivity.this, PoliciesActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
//        new FinestWebView.Builder(SettingsActivity.this).show(Constants.URLS.terms);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                if (GlobalValues.countries != null) {
                    for (int i = 0; i < GlobalValues.countries.size(); i++) {
                        if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(SettingsActivity.this))) {
                            selectedCountry = GlobalValues.countries.get(i);
                            Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                        }
                    }
                }
            }
        }
    }

    private void selectLanguage() {
        dialog = new Dialog(SettingsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_language_selection);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.iv_filer_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView done = dialog.findViewById(R.id.tv_done);

        RadioButton rbEnglish = dialog.findViewById(R.id.radio_english);
        RadioButton rbArabic = dialog.findViewById(R.id.radio_arabic);

        if (SamanApp.isEnglishVersion) {
            rbEnglish.setChecked(true);
            rbArabic.setChecked(false);
        } else {
            rbArabic.setChecked(true);
            rbEnglish.setChecked(false);
        }

        Log.e("isEnglishVersion", "selectLanguage: " + SamanApp.isEnglishVersion);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = dialog.findViewById(selectedId);

                if (radioButton.isChecked()) {

                    changeLanguage(radioButton);
//                    Constants.showAlert(getString(R.string.title_settings), getString(R.string.app_language), getString(R.string.okay), SettingsActivity.this);
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    private void changeLanguage(RadioButton radioButton) {

        WebServicesHandler apiClient = WebServicesHandler.instance;
        int type = 1;
        if (radioButton.getId() == R.id.radio_arabic) {
            type = 2;
        }
        User user = GlobalValues.getUser(this);
        Constants.showSpinner(getString(R.string.language), this);
        apiClient.getChangeLanguage(String.valueOf(user.getId()), type, new Callback<ChangeLanguage>() {
            @Override
            public void onResponse(Call<ChangeLanguage> call, Response<ChangeLanguage> response) {
                Constants.dismissSpinner();
                ChangeLanguage changeLanguage = response.body();
                if (changeLanguage != null && changeLanguage.result) {
                    showAlertLanguage(getString(R.string.title_settings), getString(R.string.app_language), getString(R.string.okay), SettingsActivity.this);
                    if (radioButton.getId() == R.id.radio_arabic) {

                        GlobalValues.setAppLanguage(getApplicationContext(), "ar");
                        SamanApp.isEnglishVersion = false;
                    } else {

                        GlobalValues.setAppLanguage(getApplicationContext(), "en");
                        SamanApp.isEnglishVersion = true;
                    }
                    selectedLanguage = radioButton.getText().toString();
                    languageTextView.setText(radioButton.getText().toString());
                } else {
                    Toast.makeText(SettingsActivity.this, "Please try again...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangeLanguage> call, Throwable t) {
                Constants.dismissSpinner();
                Toast.makeText(SettingsActivity.this, "Please try again...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlertLanguage(String title, String message, String buttonText, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        TextView textView = new TextView(this);

        Typeface face = Typeface.createFromAsset(getAssets(), "font/neo_sans.ttf");
        textView.setTypeface(face);
        textView.setText(title);
        if (SamanApp.isEnglishVersion) {
            textView.setPadding(20, 10, 0, 0);
        } else {
            textView.setPadding(0, 10, 20, 0);
        }
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        alertDialog.setCustomTitle(textView);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent refresh = new Intent(context, SplashActivity.class);

                        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //refresh.putExtra(currentLang, localeName);
                        startActivity(refresh);
                        finishAffinity();
                    }
                });
        alertDialog.show();
    }
}
