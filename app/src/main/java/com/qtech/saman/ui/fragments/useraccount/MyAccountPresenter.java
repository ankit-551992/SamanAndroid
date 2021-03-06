package com.qtech.saman.ui.fragments.useraccount;

import android.util.Log;

import com.google.gson.Gson;
import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountPresenter implements MyAccountContractor.Presenter {

    MyAccountContractor.View view;

    public MyAccountPresenter(MyAccountContractor.View view) {
        this.view = view;
    }

    @Override
    public void getUsetInfoApi(int userID) {

        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.getUserInfo(userID, new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if (userResponse.getSuccess() == 1) {
                        if (userResponse.getUser() != null) {
                            if (view != null) {
                                view.response(userResponse.getUser());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void destroy() {

    }
}
