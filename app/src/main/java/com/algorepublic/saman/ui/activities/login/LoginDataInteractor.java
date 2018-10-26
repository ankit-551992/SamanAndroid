package com.algorepublic.saman.ui.activities.login;

import android.util.Log;

import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDataInteractor implements LoginData {

    @Override
    public void loginUser(String email, String password, final LoginData.OnResponseListener responseListener) {
        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.login(email, password,"Test", new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                responseListener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("onFailure",""+t.getMessage());
            }
        });
    }
}