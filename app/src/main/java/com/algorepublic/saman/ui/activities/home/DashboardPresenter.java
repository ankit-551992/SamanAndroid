package com.algorepublic.saman.ui.activities.home;


public class DashboardPresenter implements DashboardContractor.Presenter {

    private DashboardContractor.View view;


    public DashboardPresenter(DashboardContractor.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void getUserData() {
        view.setupNavigationDrawer();
        view.updateUserDetails();
    }
}
