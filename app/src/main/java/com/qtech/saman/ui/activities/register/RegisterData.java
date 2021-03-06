package com.qtech.saman.ui.activities.register;

import com.qtech.saman.data.model.apis.UserResponse;

import java.util.ArrayList;

public interface RegisterData {

    interface OnResponseListener {
        void onResponse(UserResponse response);
        void setCountries(ArrayList<String> countries);
    }

    void registerUser(String fName,String lName,String email,String password,String deviceToken,String gender,String country,String address,String dob,String phone,String region,RegisterData.OnResponseListener responseListener);

    void getCountries(RegisterData.OnResponseListener responseListener);
}
