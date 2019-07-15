package com.algorepublic.saman.ui.fragments.store.Tab;

import com.algorepublic.saman.data.model.Store;

import java.util.List;

public class TabContractor{


    interface View {
        void showProgress();
        void hideProgress();
        void response(List<Store> getStores);
        void error(String message);
    }

    interface Presenter {
        void getStoresByCategory(int categoryID);
        void destroy();
    }

}
