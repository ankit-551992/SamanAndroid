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
import com.algorepublic.saman.ui.activities.order.checkout.CheckoutOrderActivity;
import com.algorepublic.saman.ui.activities.password.ChangePasswordActivity;
import com.algorepublic.saman.utils.Constants;
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
    }


    @OnClick(R.id.tv_change_password)
    void changePassword(){
        Intent intent=new Intent(SettingsActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_privacy_policy)
    void privacy(){
        new FinestWebView.Builder(SettingsActivity.this).show(Constants.URLS.privacyPolicy);
    }

    @OnClick(R.id.tv_terms_of_uses)
    void termsOfUses(){
        new FinestWebView.Builder(SettingsActivity.this).show(Constants.URLS.terms);
    }

}
