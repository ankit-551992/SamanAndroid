package com.algorepublic.saman.ui.activities.home;


import android.util.Log;

import com.algorepublic.saman.data.model.apis.GetCategoriesList;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.utils.GlobalValues;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardPresenter implements DashboardContractor.Presenter {

    private DashboardContractor.View view;

    public DashboardPresenter(DashboardContractor.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void getUserData() {
        view.setupNavigationDrawer();
        view.updateUserDetails();

        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.getStoreCategories( new Callback<GetCategoriesList>() {
            @Override
            public void onResponse(Call<GetCategoriesList> call, Response<GetCategoriesList> response) {
                Log.e("SIGNUP_URL", "----sign---up---onResponse---" + new Gson().toJson(response));
                GetCategoriesList getCategoriesList = response.body();
                if(getCategoriesList!=null) {
                    if(getCategoriesList.getSuccess()==1) {
                        if (getCategoriesList.getCategories() != null) {
                            GlobalValues.storeCategories=getCategoriesList.getCategories();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<GetCategoriesList> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }
}
