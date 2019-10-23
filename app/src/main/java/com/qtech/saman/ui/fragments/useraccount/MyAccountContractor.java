package com.qtech.saman.ui.fragments.useraccount;

import com.qtech.saman.data.model.User;

public class MyAccountContractor {

    interface View {
        void response(User user);
        void error(String message);
    }

    interface Presenter {
        void getUsetInfoApi(int userID);
        void destroy();
    }
}
