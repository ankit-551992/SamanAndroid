package com.algorepublic.saman.ui.activities.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.ui.activities.PoliciesActivity;
import com.algorepublic.saman.ui.activities.country.CountriesActivity;
import com.algorepublic.saman.ui.activities.myaccount.mydetails.MyDetailsActivity;
import com.algorepublic.saman.ui.activities.order.checkout.CheckoutOrderActivity;
import com.algorepublic.saman.ui.activities.password.ChangePasswordActivity;
import com.algorepublic.saman.ui.activities.register.RegisterActivity;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;
import com.thefinestartist.finestwebview.FinestWebView;

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
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        for (int i = 0; i<GlobalValues.countries.size(); i++){
            if(GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(SettingsActivity.this))){
                selectedCountry=GlobalValues.countries.get(i);
                Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
            }
        }
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent=new Intent(SettingsActivity.this,CountriesActivity.class);
        startActivityForResult(intent,1299);
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.tv_change_password)
    void changePassword(){
        Intent intent=new Intent(SettingsActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_privacy_policy)
    void privacy(){
        Intent intent=new Intent(SettingsActivity.this,PoliciesActivity.class);
        intent.putExtra("type",0);
        startActivity(intent);
//        new FinestWebView.Builder(SettingsActivity.this).show(Constants.URLS.privacyPolicy);
    }

    @OnClick(R.id.tv_terms_of_uses)
    void termsOfUses(){
        Intent intent=new Intent(SettingsActivity.this,PoliciesActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
//        new FinestWebView.Builder(SettingsActivity.this).show(Constants.URLS.terms);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                for (int i = 0; i<GlobalValues.countries.size(); i++){
                    if(GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(SettingsActivity.this))){
                        selectedCountry=GlobalValues.countries.get(i);
                        Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                    }
                }
            }
        }
    }
}
