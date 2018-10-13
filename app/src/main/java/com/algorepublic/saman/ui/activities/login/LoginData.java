package com.algorepublic.saman.ui.activities.login;

import com.algorepublic.saman.data.model.UserResponse;

public interface LoginData {

    interface OnResponseListener {
        void onResponse(UserResponse response);
    }

    void loginUser(String email, String password, OnResponseListener responseListener);

}
