package com.algorepublic.saman.ui.activities.myaccount.mydetails;

import android.util.Log;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;

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
    public void updateUser(int id, String fName, String lName, String gender, String country, JSONObject address) {
        if (view != null) {
            view.showProgress();
        }

        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.updateUser(id, fName, lName, gender, country, address, new Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                SimpleSuccess simpleSuccess = response.body();
                if (simpleSuccess != null) {
                    if (simpleSuccess.getSuccess() == 1) {
                        if (view != null) {
                            view.hideProgress();
                            view.updateResponse(true);
                        }
                    } else {
                        if (view != null) {
                            view.hideProgress();
                            view.updateResponse(false);
                        }
                    }
                } else {
                    if (view != null) {
                        view.hideProgress();
                        view.updateResponse(false);
                    }
                }
            }
            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }

    @Override
    public void destroy() {

    }
}
