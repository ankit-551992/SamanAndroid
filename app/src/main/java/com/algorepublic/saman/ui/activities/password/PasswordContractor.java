package com.algorepublic.saman.ui.activities.password;

import com.algorepublic.saman.data.model.User;

public class PasswordContractor {

    interface View{
        void showProgress();

        void hideProgress();

        void changeResponse(User user);

        void error(String message);

        void forgotResponse(String message);

        void resetResponse(String message);
    }


    interface Presenter{
        void changePassword(int userId,String oldPassword, String newPassword);
        void resetPassword(String token, String newPassword);
        void recoveryEmail(String email,String phone);
        void onDestroy();
    }
}
