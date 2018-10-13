package com.algorepublic.saman.ui.activities.register;

import com.algorepublic.saman.data.model.User;

import java.util.ArrayList;

public interface RegisterView {

    void showProgress();
    void hideProgress();
    void registerResponse(User user);
    void registerError(String message);
    void countries(ArrayList<String> countriesList);
}
