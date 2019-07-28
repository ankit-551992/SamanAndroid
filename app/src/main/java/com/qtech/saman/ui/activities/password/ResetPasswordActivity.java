package com.qtech.saman.ui.activities.password;

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

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.User;
import com.qtech.saman.utils.AsteriskPasswordTransformationMethod;
import com.qtech.saman.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity  extends BaseActivity implements PasswordContractor.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_token)
    EditText tokenEditText;
    @BindView(R.id.editText_newPassword)
    EditText newPasswordEditText;
    @BindView(R.id.iv_newPassword_visible)
    ImageView newPasswordVisibilityImageView;
    @BindView(R.id.editText_confirmPassword)
    EditText confirmNewPasswordEditText;
    @BindView(R.id.iv_confirm_password_visible)
    ImageView confirmNewPasswordVisibilityImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    boolean isShowing=false;
    boolean isConfirmShowing = false;

    PasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        presenter=new PasswordPresenter(this);
        newPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        confirmNewPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        toolbarTitle.setText(getString(R.string.reset_password));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
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

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_resetPassword)
    public void resetPasswordButton(){
        String token=tokenEditText.getText().toString();
        String newPassword=newPasswordEditText.getText().toString();
        String confirmPassword = confirmNewPasswordEditText.getText().toString();
        if(isDataValid(token,newPassword,confirmPassword)){
            presenter.resetPassword(token,newPassword);
        }
    }

    private boolean isDataValid(String oldPassword, String newPassword,String confirm) {
        if (TextUtils.isEmpty(oldPassword)) {
            tokenEditText.setError(getString(R.string.enter_token));
            return false;
        }else if (TextUtils.isEmpty(newPassword)) {
            newPasswordEditText.setError(getString(R.string.new_password_required));
            return false;
        }else if (newPassword.length() < 6) {
            Constants.showAlert(getString(R.string.reset_password), getString(R.string.password_short), getString(R.string.okay), ResetPasswordActivity.this);
            return false;
        }else if (TextUtils.isEmpty(confirm)) {
            confirmNewPasswordEditText.setError(getString(R.string.confirm_new_password_required));
            return false;
        }else if (!confirm.equals(newPassword)) {
            confirmNewPasswordEditText.setError(getString(R.string.not_matched));
            return false;
        }else {
            return true;
        }
    }

    @OnClick(R.id.iv_newPassword_visible)
    public void setNewPasswordVisibility(){
        if(!isShowing){
            isShowing=true;
            newPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            newPasswordEditText.setTransformationMethod(null);
            newPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        }else {
            isShowing=false;
            newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            newPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
        }
        newPasswordEditText.setSelection(newPasswordEditText.length());
    }

    @OnClick(R.id.iv_confirm_password_visible)
    public void setConfirmPasswordVisibility() {
        if (!isConfirmShowing) {
            isConfirmShowing = true;
            confirmNewPasswordEditText.setTransformationMethod(null);
            confirmNewPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        } else {
            isConfirmShowing = false;
            confirmNewPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            confirmNewPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
        }
        confirmNewPasswordEditText.setSelection(confirmNewPasswordEditText.length());
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
        Constants.showAlert(getString(R.string.reset_password),getString(R.string.invalid_token),getString(R.string.try_again),ResetPasswordActivity.this);
    }

    @Override
    public void forgotResponse(String message){
    }

    @Override
    public void resetResponse(String message) {
        Constants.showAlertWithActivityFinish(getString(R.string.reset_password),message,getString(R.string.Okay),ResetPasswordActivity.this);
    }
}
