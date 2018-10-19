package com.algorepublic.saman.ui.activities.password;

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

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.utils.AsteriskPasswordTransformationMethod;
import com.algorepublic.saman.utils.Constants;

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
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    boolean isShowing=false;

    PasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        presenter=new PasswordPresenter(this);
        newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
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
    public void back(){
        finish();
    }

    @OnClick(R.id.button_resetPassword)
    public void resetPasswordButton(){
        String token=tokenEditText.getText().toString();
        String newPassword=newPasswordEditText.getText().toString();
        if(isDataValid(token,newPassword)){
            presenter.resetPassword(token, newPassword);
        }
    }

    private boolean isDataValid(String oldPassword, String newPassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            tokenEditText.setError(getString(R.string.enter_token));
            return false;
        }else if (TextUtils.isEmpty(newPassword)) {
            newPasswordEditText.setError(getString(R.string.new_password_required));
            return false;
        }
        else {
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
        Constants.showAlert(getString(R.string.reset_password),message,getString(R.string.try_again),ResetPasswordActivity.this);
    }

    @Override
    public void forgotResponse(String message){
    }

    @Override
    public void resetResponse(String message) {
        Constants.showAlertWithActivityFinish(getString(R.string.reset_password),message,getString(R.string.Okay),ResetPasswordActivity.this);
    }
}
