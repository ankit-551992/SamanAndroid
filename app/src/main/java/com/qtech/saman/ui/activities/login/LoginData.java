package com.qtech.saman.ui.activities.login;

import com.qtech.saman.data.model.apis.UserResponse;

public interface LoginData {

    interface OnResponseListener {
        void onResponse(UserResponse response);
    }

    void loginUser(String email, String password,String token, OnResponseListener responseListener);

}
