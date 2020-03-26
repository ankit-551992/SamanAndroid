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
import com.qtech.saman.utils.GlobalValues;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements PasswordContractor.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_oldPassword)
    EditText oldPasswordEditText;
    @BindView(R.id.iv_oldPassword_visible)
    ImageView oldPasswordVisibilityImageView;
    @BindView(R.id.editText_newPassword)
    EditText newPasswordEditText;
    @BindView(R.id.iv_newPassword_visible)
    ImageView newPasswordVisibilityImageView;
    @BindView(R.id.editText_confirmPassword)
    EditText confirmPasswordEditText;
    @BindView(R.id.iv_confirm_password_visible)
    ImageView confirmPasswordVisibilityImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    boolean isOldShowing=false;
    boolean isNewShowing=false;

    PasswordPresenter presenter;

    boolean isConfirmShowing = false;

    User authenticatedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        authenticatedUser = GlobalValues.getUser(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        presenter=new PasswordPresenter(this);
        oldPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        newPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        confirmPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        toolbarTitle.setText(getString(R.string.change_password));
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

    @OnClick(R.id.button_changePassword)
    public void changePasswordButton(){
        String oldPassword=oldPasswordEditText.getText().toString();
        String newPassword=newPasswordEditText.getText().toString();
        String confirm=confirmPasswordEditText.getText().toString();
        if(isDataValid(oldPassword,newPassword,confirm)) {
            presenter.changePassword(authenticatedUser.getId(),oldPassword, newPassword);
        }
    }

    private boolean isDataValid(String oldPassword, String newPassword,String confrim) {
        if (TextUtils.isEmpty(oldPassword)) {
            oldPasswordEditText.setError(getString(R.string.old_password_required));
            return false;
        } else if (TextUtils.isEmpty(newPassword)) {
            newPasswordEditText.setError(getString(R.string.new_password_required));
            return false;
        }else if (newPassword.length() < 6) {
            Constants.showAlert(getString(R.string.change_password), getString(R.string.password_short), getString(R.string.okay), ChangePasswordActivity.this);
            return false;
        }else if (TextUtils.isEmpty(confrim)) {
            confirmPasswordEditText.setError(getString(R.string.confirm_new_password_required));
            return false;
        }else if (!confrim.equals(newPassword)) {
            confirmPasswordEditText.setError(getString(R.string.not_matched));
            return false;
        }
        else {
            return true;
        }
    }


    @OnClick(R.id.iv_oldPassword_visible)
    public void setOldPasswordVisibility(){
        if(!isOldShowing){
            isOldShowing=true;
            oldPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            oldPasswordEditText.setTransformationMethod(null);
            oldPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        }else {
            isOldShowing=false;
            oldPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            oldPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            oldPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
        }
        oldPasswordEditText.setSelection(oldPasswordEditText.length());
    }

    @OnClick(R.id.iv_newPassword_visible)
    public void setNewPasswordVisibility(){
        if(!isNewShowing){
            isNewShowing=true;
            newPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            newPasswordEditText.setTransformationMethod(null);
            newPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        }else {
            isNewShowing=false;
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
            confirmPasswordEditText.setTransformationMethod(null);
            confirmPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        } else {
            isConfirmShowing = false;
            confirmPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            confirmPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
        }
        confirmPasswordEditText.setSelection(confirmPasswordEditText.length());
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
//        GlobalValues.saveUser(ChangePasswordActivity.this,user);
        Constants.showAlertWithActivityFinish(getString(R.string.change_password),getString(R.string.password_changed),getString(R.string.Okay),ChangePasswordActivity.this);
    }

    @Override
    public void error(String message) {
        Constants.showAlert(getString(R.string.change_password),message,getString(R.string.try_again),ChangePasswordActivity.this);
    }

    @Override
    public void forgotResponse(String message) {
    }

    @Override
    public void resetResponse(String message) {

    }
}
