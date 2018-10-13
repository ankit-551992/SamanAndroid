package com.algorepublic.saman.ui.activities.login;

import com.algorepublic.saman.data.model.UserResponse;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;

public class LoginPresenterImpl implements LoginPresenter,LoginData.OnResponseListener {

    private LoginView loginView;
    private LoginDataInteractor loginDataInteractor;

    public LoginPresenterImpl(LoginView loginView, LoginDataInteractor loginDataInteractor) {
        this.loginView = loginView;
        this.loginDataInteractor = loginDataInteractor;
    }

    @Override
    public void onResponse(UserResponse response) {
        if (loginView != null) {
            loginView.hideProgress();
            if(response.getSuccess()==1) {
                loginView.loginResponse(response.getUser());
            }else if(response.getSuccess()==0){
                loginView.loginError(response.getMessage());
            }
        }
    }

    @Override
    public void loginUser(String email, String password) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginDataInteractor.loginUser(email,password,this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }
}
