package com.algorepublic.saman.ui.activities.settings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.ui.activities.PoliciesActivity;
import com.algorepublic.saman.ui.activities.country.CountriesActivity;
import com.algorepublic.saman.ui.activities.password.ChangePasswordActivity;
import com.algorepublic.saman.ui.activities.register.RegisterActivity;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    Country selectedCountry;

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

        for (int i = 0; i < GlobalValues.countries.size(); i++) {
            if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(SettingsActivity.this))) {
                selectedCountry = GlobalValues.countries.get(i);
                Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
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

        if(GlobalValues.getAppLanguage(this).equals("ar")){
            languageTextView.setText(getString(R.string.arabic));
        }else {
            languageTextView.setText(getString(R.string.english));
        }

    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(SettingsActivity.this, CountriesActivity.class);
        startActivityForResult(intent, 1299);
    }

    @OnClick(R.id.layout_language)
    public void languageSelection() {
        selectLanguage();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.tv_change_password)
    void changePassword() {
        Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
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

        if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                for (int i = 0; i < GlobalValues.countries.size(); i++) {
                    if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(SettingsActivity.this))) {
                        selectedCountry = GlobalValues.countries.get(i);
                        Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                    }
                }
            }
        }
    }

    Dialog dialog;
    String selectedLanguage = "";

    private void selectLanguage() {
        dialog = new Dialog(SettingsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_language_selection);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView done = (TextView) dialog.findViewById(R.id.tv_done);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                if (radioButton.isChecked()) {
                    if(radioButton.getId()==R.id.radio_arabic){
                        GlobalValues.setAppLanguage(getApplicationContext(), "ar");
                    }else{
                        GlobalValues.setAppLanguage(getApplicationContext(), "en");
                    }
                    selectedLanguage = radioButton.getText().toString();
                    languageTextView.setText(radioButton.getText().toString());
                    dialog.dismiss();

                    Constants.showAlert(getString(R.string.title_settings),getString(R.string.app_language),getString(R.string.okay),SettingsActivity.this);
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(SettingsActivity.this,
                R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }
}
