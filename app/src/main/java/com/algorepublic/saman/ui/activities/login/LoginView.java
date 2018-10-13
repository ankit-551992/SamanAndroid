package com.algorepublic.saman.ui.activities.login;

import com.algorepublic.saman.data.model.User;

public interface LoginView {

    void showProgress();

    void hideProgress();

    void loginResponse(User user);

    void loginError(String message);
}
