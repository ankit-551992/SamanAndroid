package com.qtech.saman.ui.fragments.favourite;

import android.util.Log;

import com.qtech.saman.data.model.apis.GetProducts;
import com.qtech.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesPresenter implements FavoritesContractor.Presenter {


    private FavoritesContractor.View view;


    public FavoritesPresenter(FavoritesContractor.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }


    @Override
    public void getFavoritesData(int userID,int pageIndex,int pageSize,boolean showProgress) {
        if (view != null && showProgress) {
            view.showProgress();
        }

        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.getFavoriteList(userID,pageIndex,pageSize,new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                GetProducts getProducts = response.body();
                if(getProducts !=null) {
                    if(getProducts.getSuccess()==1) {
                        if (getProducts.getProduct() != null) {
                            if (view != null) {
                                view.hideProgress();
                                view.response(getProducts.getProduct());
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
            public void onFailure(Call<GetProducts> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }


}
