package com.qtech.saman.ui.activities.productdetail;

import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.apis.SimpleSuccess;

public class ProductContractor {

    interface View {
        void showProgress();

        void hideProgress();

        void response(Product product);

        void error(String message);

        void markFavoriteResponse(boolean success);

        void markUnFavoriteResponse(boolean success);

        void addProductNotifyResponse(SimpleSuccess simpleSuccess);
    }

    interface Presenter {
        void getProductData(int productID, int userID);

        void destroy();

        void markFavorite(int userID, int productID, String[] optionIDs, int quantity);

        void markUnFavorite(int userID, int productID, String[] optionIDs);

        void addProduct(int userID, int productID);
    }
}
