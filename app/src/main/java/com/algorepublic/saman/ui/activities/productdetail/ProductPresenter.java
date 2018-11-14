package com.algorepublic.saman.ui.activities.productdetail;

import com.algorepublic.saman.data.model.apis.GetProduct;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;
import retrofit2.Call;
import retrofit2.Response;

public class ProductPresenter implements ProductContractor.Presenter {
    private ProductContractor.View view;


    public ProductPresenter(ProductContractor.View view) {
        this.view = view;
    }

    @Override
    public void getProductData(int productID) {

        view.showProgress();
        WebServicesHandler.instance.getProductDetail(String.valueOf(productID), new retrofit2.Callback<GetProduct>() {
            @Override
            public void onResponse(Call<GetProduct> call, Response<GetProduct> response) {
                GetProduct getProduct = response.body();
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
    public void markFavorite(int userID, int productID) {
        WebServicesHandler.instance.markFavourite(userID, productID, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                SimpleSuccess simpleSuccess = response.body();
                if (simpleSuccess != null) {
                    if (simpleSuccess.getSuccess() == 1) {
                        if (view != null) {
                            view.markFavoriteResponse(true);
                        }
                    }else {
                        if (view != null) {
                            view.markFavoriteResponse(false);
                        }
                    }
                }else {
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
    public void markUnFavorite(int userID, int productID) {
        WebServicesHandler.instance.markUnFavourite(userID, productID, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                SimpleSuccess simpleSuccess = response.body();
                if (simpleSuccess != null) {
                    if (simpleSuccess.getSuccess() == 1) {
                        if (view != null) {
                            view.markUnFavoriteResponse(true);
                        }
                    }else {
                        if (view != null) {
                            view.markUnFavoriteResponse(false);
                        }
                    }
                }else {
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

}
