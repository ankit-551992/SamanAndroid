package com.qtech.saman.ui.activities.password;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.User;
import com.qtech.saman.ui.activities.country.CountriesListingActivity;
import com.qtech.saman.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity implements PasswordContractor.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.option_tv)
    TextView selectedOptionTextView;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_email)
    EditText emailEditText;
    @BindView(R.id.ccp)
    EditText ccp;
    @BindView(R.id.editText_phone)
    EditText phoneEditText;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.number_layout)
    LinearLayout numberLinearLayout;

    PasswordPresenter presenter;

    enum SelectedOption {EMAIL, PHONE}

    SelectedOption selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        presenter = new PasswordPresenter(this);
        toolbarTitle.setText(getString(R.string.forgot_pass));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        showPopUp();
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
    public void recoveryEmailButton() {
        if (selectedOption == SelectedOption.EMAIL) {
            String email = emailEditText.getText().toString();
            if (isDataValid(email)) {
                presenter.recoveryEmail(email, null);
            }
        } else if (selectedOption == SelectedOption.PHONE) {
            String phone = ccp.getText().toString() + phoneEditText.getText().toString();
            if (isDataValidPhone(phone)) {
                phone = ccp.getText().toString() + "-" + phoneEditText.getText().toString();
                presenter.recoveryEmail(null, phone);
            }
        }
    }


    @OnClick(R.id.ccp)
    public void phoneCode() {
        Intent intent = new Intent(ForgotPasswordActivity.this, CountriesListingActivity.class);
        intent.putExtra("GetCode", true);
        startActivityForResult(intent, 2021);
    }

    private boolean isDataValid(String email) {
        if (TextUtils.isEmpty(email)) {
            Constants.showAlert(getString(R.string.forgot_password), getString(R.string.email_required), getString(R.string.okay), ForgotPasswordActivity.this);
            return false;
        } else {
            return true;
        }
    }

    private boolean isDataValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Constants.showAlert(getString(R.string.forgot_password), getString(R.string.Phone_Number_Required), getString(R.string.okay), ForgotPasswordActivity.this);
            return false;
        } else if (phone.length() < 10) {
            Constants.showAlert(getString(R.string.forgot_password), getString(R.string.Not_Valid), getString(R.string.okay), ForgotPasswordActivity.this);
            return false;
        } else {
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
        Constants.showAlert(getString(R.string.forgot_pass), message, getString(R.string.try_again), ForgotPasswordActivity.this);
    }

    @Override
    public void forgotResponse(String message) {
        Log.e("FORGOT111", "--forge--response--message----" + message);
        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void resetResponse(String message) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2021) {
            if (resultCode == RESULT_OK) {
                String code = data.getExtras().getString("Code");
                ccp.setText("+" + code);
            }
        }
    }

    Dialog dialog;

    private void showPopUp() {
        dialog = new Dialog(ForgotPasswordActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_forgot_password_options);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_pop_up_close);
        Button phoneNumberButton = (Button) dialog.findViewById(R.id.button_select_phone);
        Button emailButton = (Button) dialog.findViewById(R.id.button_select_email);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = SelectedOption.EMAIL;
                setSelectedView();
                dialog.dismiss();
            }
        });

        phoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = SelectedOption.PHONE;
                setSelectedView();
                dialog.dismiss();
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(ForgotPasswordActivity.this,
                R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    private void setSelectedView() {
        if (selectedOption == SelectedOption.PHONE) {
            emailEditText.setVisibility(View.GONE);
            numberLinearLayout.setVisibility(View.VISIBLE);
            selectedOptionTextView.setText(getString(R.string.mobile_number));
        } else if (selectedOption == SelectedOption.EMAIL) {
            numberLinearLayout.setVisibility(View.GONE);
            emailEditText.setVisibility(View.VISIBLE);
            selectedOptionTextView.setText(getString(R.string.email_address));
        }
    }

}
