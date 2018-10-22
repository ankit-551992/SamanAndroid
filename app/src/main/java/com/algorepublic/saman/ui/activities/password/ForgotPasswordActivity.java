package com.algorepublic.saman.ui.activities.password;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.BuildConfig;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity  extends BaseActivity implements PasswordContractor.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_email)
    EditText emailEditText;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    PasswordPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        presenter=new PasswordPresenter(this);
        toolbarTitle.setText(getString(R.string.forgot_pass));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @OnClick(R.id.button_recoveryEmail)
    public void recoveryEmailButton(){
        String email=emailEditText.getText().toString();
        if(isDataValid(email)) {
            presenter.recoveryEmail(email);
        }
    }

    private boolean isDataValid(String email) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.email_required));
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void changeResponse(User user) {
    }

    @Override
    public void error(String message) {
        Constants.showAlert(getString(R.string.forgot_pass),message,getString(R.string.try_again),ForgotPasswordActivity.this);
    }

    @Override
    public void forgotResponse(String message) {
        Intent intent=new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void resetResponse(String message) {

    }
}
