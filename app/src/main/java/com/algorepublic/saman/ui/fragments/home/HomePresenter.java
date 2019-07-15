package com.algorepublic.saman.ui.fragments.home;

import android.util.Log;

import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.apis.HomeScreenAPI;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.utils.GlobalValues;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements HomeContractor.Presenter {


    private HomeContractor.View view;


    public HomePresenter(HomeContractor.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }


    @Override
    public void getHomeData(int userID) {
        if (view != null) {
            view.showProgress();
        }

        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.getHomeScreenData(userID,new Callback<HomeScreenAPI>() {
            @Override
            public void onResponse(Call<HomeScreenAPI> call, Response<HomeScreenAPI> response) {
                HomeScreenAPI homeScreenAPI = response.body();
                if(homeScreenAPI !=null) {
                    if(homeScreenAPI.getSuccess()==1) {
                        if (homeScreenAPI.getResult() != null) {
                            if (view != null) {
                                view.hideProgress();
                                view.response(homeScreenAPI.getResult());
                            }
                        }
                    }
                }else {
                    if (view != null) {
                        view.error("null");
                    }
                }
            }
            @Override
            public void onFailure(Call<HomeScreenAPI> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }


}
