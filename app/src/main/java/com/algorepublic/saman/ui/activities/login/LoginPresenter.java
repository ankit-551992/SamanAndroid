package com.algorepublic.saman.ui.activities.login;

public interface LoginPresenter {

    void loginUser(String email, String password, String token);
    void onDestroy();
}
