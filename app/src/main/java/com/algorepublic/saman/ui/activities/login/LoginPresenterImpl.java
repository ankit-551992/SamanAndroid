package com.algorepublic.saman.ui.activities.login;

import com.algorepublic.saman.data.model.apis.UserResponse;
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
            if(response!=null) {
                if (response.getSuccess() == 1) {
                    loginView.loginResponse(response.getUser());
                } else if (response.getSuccess() == 0) {
                    loginView.loginError(response.getMessage());
                }
            }else {
                loginView.loginError("Internal Server Error");
            }
        }
    }

    @Override
    public void loginUser(String email, String password,String token) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginDataInteractor.loginUser(email,password,token,this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }
}
