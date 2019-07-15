package com.algorepublic.saman.ui.fragments.home;

import com.algorepublic.saman.data.model.HomeScreenData;

public class HomeContractor {

    interface View {

        void showProgress();
        void hideProgress();
        void response(HomeScreenData screenApi);
        void error(String message);
    }

    interface Presenter {
        void getHomeData(int userID);
        void destroy();
    }

}
