package com.qtech.saman.ui.activities.login;

import android.util.Log;

import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDataInteractor implements LoginData {

    @Override
    public void loginUser(String email, String password,String token, final LoginData.OnResponseListener responseListener) {
        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.login(email, password,token, new Callback<UserResponse>() {
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