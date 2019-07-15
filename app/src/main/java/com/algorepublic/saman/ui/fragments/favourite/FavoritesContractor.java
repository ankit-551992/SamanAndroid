package com.algorepublic.saman.ui.fragments.favourite;

import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.Product;

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
