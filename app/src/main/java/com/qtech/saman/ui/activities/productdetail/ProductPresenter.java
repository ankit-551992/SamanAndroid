package com.qtech.saman.ui.activities.productdetail;

import android.util.Log;

import com.qtech.saman.data.model.apis.GetProduct;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPresenter implements ProductContractor.Presenter {
    private ProductContractor.View view;


    public ProductPresenter(ProductContractor.View view) {
        this.view = view;
    }

    @Override
    public void getProductData(int productID, int userID) {

        view.showProgress();
        WebServicesHandler.instance.getProductDetail(String.valueOf(productID), String.valueOf(userID), new retrofit2.Callback<GetProduct>() {
            @Override
            public void onResponse(Call<GetProduct> call, Response<GetProduct> response) {
                GetProduct getProduct = response.body();
                Log.e("PRODUCT00", "--getProduct--" + getProduct);
                if (getProduct != null) {
                    if (getProduct.getSuccess() == 1) {
                        if (getProduct.getProduct() != null) {
                            if (view != null) {
                                view.hideProgress();
                                view.response(getProduct.getProduct());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProduct> call, Throwable t) {
            }
        });
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void markFavorite(int userID, int productID, String[] optionIDs, int quantity) {
        WebServicesHandler.instance.markFavourite(userID, productID, optionIDs, quantity, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                SimpleSuccess simpleSuccess = response.body();
                if (simpleSuccess != null) {
                    if (simpleSuccess.getSuccess() == 1) {
                        if (view != null) {
                            view.markFavoriteResponse(true);
                        }
                    } else {
                        if (view != null) {
                            view.markFavoriteResponse(false);
                        }
                    }
                } else {
                    if (view != null) {
                        view.markFavoriteResponse(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
            }
        });
    }

    @Override
    public void markUnFavorite(int userID, int productID, String[] optionIDs) {
        WebServicesHandler.instance.markUnFavourite(userID, productID, optionIDs, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                SimpleSuccess simpleSuccess = response.body();
                if (simpleSuccess != null) {
                    if (simpleSuccess.getSuccess() == 1) {
                        if (view != null) {
                            view.markUnFavoriteResponse(true);
                        }
                    } else {
                        if (view != null) {
                            view.markUnFavoriteResponse(false);
                        }
                    }
                } else {
                    if (view != null) {
                        view.markUnFavoriteResponse(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {

            }
        });
    }

    @Override
    public void addProduct(int userID, int productID) {
        WebServicesHandler.instance.notifyAddProduct(userID, productID, new Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                SimpleSuccess simpleSuccess = response.body();
                Log.e("NOTIFY000", "--NOTIFY000--onResponse===--simpleSuccess-----" + simpleSuccess);

                if (simpleSuccess.getSuccess() == 1) {
                    Log.e("NOTIFY000", "--NOTIFY000----simpleSuccess-----" + simpleSuccess.getResult());
                    if (view != null) {
                        view.addProductNotifyResponse(simpleSuccess);
                    }
                } else {
                    if (view != null) {
                        view.addProductNotifyResponse(simpleSuccess);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                Log.e("NOTIFY000", "--NOTIFY000----onFailure-----" + t.getMessage());
            }
        });
    }
}
