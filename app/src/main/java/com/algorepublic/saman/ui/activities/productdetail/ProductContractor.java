package com.algorepublic.saman.ui.activities.productdetail;

import com.algorepublic.saman.data.model.Product;

public class ProductContractor {

    interface View{
        void showProgress();
        void hideProgress();
        void response(Product product);
        void error(String message);
        void markFavoriteResponse(boolean success);
        void markUnFavoriteResponse(boolean success);
    }


    interface Presenter {
        void getProductData(int productID);
        void destroy();
        void markFavorite(int userID,int productID);
        void markUnFavorite(int userID,int productID);
    }
}
