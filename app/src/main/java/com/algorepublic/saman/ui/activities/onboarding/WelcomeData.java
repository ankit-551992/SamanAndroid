package com.algorepublic.saman.ui.activities.onboarding;

import com.algorepublic.saman.ui.activities.register.RegisterData;


public interface WelcomeData {

    interface OnResponseListener {
//        void onFacebookResponse();
//        void onTwitterResponse();
        void onGoogleResponse();
//        void onServerResponse();
    }


    void registerUser(String name,String email,String gender,String country,String address,String password,RegisterData.OnResponseListener responseListener);

}
