package com.qtech.saman.ui.activities.myaccount.mydetails;

import android.util.Log;

import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDetailsPresenter implements DetailContractor.Presenter {

    private DetailContractor.View view;

    public MyDetailsPresenter(DetailContractor.View view) {
        this.view = view;
    }

    @Override
    public void updateUser(int id, String fName, String lName, String gender, String country, JSONObject address, String dob, String phone, String region) {
        if (view != null) {
            view.showProgress();
        }
        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.updateUser(id, fName, lName, gender, country, address, dob, phone, region, new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                UserResponse simpleSuccess = response.body();
                if (simpleSuccess != null) {
                    if (simpleSuccess.getSuccess() == 1) {
                        if (simpleSuccess.getUser() != null) {
                            Log.e("IMAGEW000", "--userResponse.getUser()-----" + simpleSuccess.getUser());

                            if (view != null) {
                                view.hideProgress();
                                view.updateResponse(true,simpleSuccess.getUser());
                            }
                        }
                    } else {
                        if (view != null) {
                            view.hideProgress();
                            view.updateResponseFail(false);
                        }
                    }
                } else {
                    if (view != null) {
                        view.hideProgress();
                        view.updateResponseFail(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

                view.hideProgress();
                view.updateResponseFail(false);
            }
        });
    }

    @Override
    public void destroy() {

    }
}
