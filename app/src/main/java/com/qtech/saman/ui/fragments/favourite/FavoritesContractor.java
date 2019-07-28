package com.qtech.saman.ui.fragments.favourite;

import com.qtech.saman.data.model.Product;

import java.util.List;

public class FavoritesContractor {


    interface View {

        void showProgress();
        void hideProgress();
        void response(List<Product> product);
        void error(String message);
    }

    interface Presenter {
        void getFavoritesData(int userID,int pageIndex,int pageSize,boolean showProgress);
        void destroy();
    }

}
