package com.algorepublic.saman.ui.activities.home;


public class DashboardContractor {

    interface View {

        void setupNavigationDrawer();
        void updateUserDetails();
    }

    interface Presenter {
        void destroy();
        void getUserData();
    }
}
