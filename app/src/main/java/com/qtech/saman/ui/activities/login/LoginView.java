package com.qtech.saman.ui.activities.login;

import com.qtech.saman.data.model.User;

public interface LoginView {

    void showProgress();

    void hideProgress();

    void loginResponse(User user);

    void loginError(String message);
}
