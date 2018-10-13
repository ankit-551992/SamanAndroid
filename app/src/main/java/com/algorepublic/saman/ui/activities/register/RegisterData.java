package com.algorepublic.saman.ui.activities.register;

import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.UserResponse;

import java.util.ArrayList;

public interface RegisterData {

    interface OnResponseListener {
        void onResponse(UserResponse response);
        void setCountries(ArrayList<String> countries);
    }

    void registerUser(String fName,String lName,String email,String password,String deviceToken,String gender,String country,String address,RegisterData.OnResponseListener responseListener);

    void getCountries(RegisterData.OnResponseListener responseListener);
}
