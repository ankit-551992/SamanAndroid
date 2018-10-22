package com.algorepublic.saman.ui.activities.home;


import android.util.Log;

import com.algorepublic.saman.data.model.SimpleSuccess;
import com.algorepublic.saman.data.model.StoreCategories;
import com.algorepublic.saman.network.WebServicesHandler;

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

        apiClient.getStoreCategories( new Callback<StoreCategories>() {
            @Override
            public void onResponse(Call<StoreCategories> call, Response<StoreCategories> response) {
                StoreCategories storeCategories = response.body();
            }
            @Override
            public void onFailure(Call<StoreCategories> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }
}
