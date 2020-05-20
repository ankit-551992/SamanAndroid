package com.qtech.saman.ui.activities.password;

import android.util.Log;

import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordPresenter implements PasswordContractor.Presenter {


    private PasswordContractor.View view;


    public PasswordPresenter(PasswordContractor.View view) {
        this.view = view;
    }

    @Override
    public void changePassword(int userID, String oldPassword, String newPassword) {
        if (view != null) {

            view.showProgress();
            WebServicesHandler apiClient = WebServicesHandler.instance;

            apiClient.ChangePassword(userID, newPassword, oldPassword, new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    UserResponse userResponse = response.body();
                    if (view != null) {
                        view.hideProgress();
                        if (userResponse != null) {
                            if (userResponse.getSuccess() == 1) {
                                view.changeResponse(userResponse.getUser());
                            } else if (userResponse.getSuccess() == 0) {
                                view.error(userResponse.getMessage());
                            }
                        } else {
                            view.error("Internal Server Error");
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                }
            });
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        if (view != null) {

            view.showProgress();
            WebServicesHandler apiClient = WebServicesHandler.instance;

            apiClient.resetPassword(token, newPassword, new Callback<SimpleSuccess>() {
                @Override
                public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {

                    SimpleSuccess simpleSuccess = response.body();
                    if (view != null) {
                        view.hideProgress();
                        if (simpleSuccess.getSuccess() == 1) {
                            view.resetResponse(simpleSuccess.getMessage());
                        } else if (simpleSuccess.getSuccess() == 0) {
                            view.error(simpleSuccess.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleSuccess> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void recoveryEmail(String email, String phone) {
        if (view != null) {
            view.showProgress();
            WebServicesHandler apiClient = WebServicesHandler.instance;

            apiClient.forgetPassword(email, phone, new Callback<SimpleSuccess>() {
                @Override
                public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {

                    SimpleSuccess simpleSuccess = response.body();
                    if (view != null) {
                        view.hideProgress();
                        if (simpleSuccess != null) {
                            if (simpleSuccess.getSuccess() == 1) {
                                view.forgotResponse(simpleSuccess.getMessage());
                            } else if (simpleSuccess.getSuccess() == 0) {
                                view.error(simpleSuccess.getMessage());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleSuccess> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
