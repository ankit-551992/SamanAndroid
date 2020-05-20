package com.qtech.saman.ui.activities.register;

import android.util.Log;

import com.google.gson.Gson;
import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDataInteractor implements RegisterData {

    @Override
    public void registerUser(String fName, String lName, String email, String password, String deviceToken, String gender, String country, String address, String dob, String phone, String region, final OnResponseListener responseListener) {

        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.register(fName, lName, email, password, deviceToken, gender, country, address, dob, phone, region, new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                responseListener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void getCountries(OnResponseListener responseListener) {
//        responseListener.setCountries(Constants.countriesList());
    }
}
