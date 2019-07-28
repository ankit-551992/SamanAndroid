package com.qtech.saman.ui.activities.myaccount.mydetails;

import org.json.JSONObject;

public class DetailContractor {

    public interface View{
        void showProgress();
        void hideProgress();
        void updateResponse(boolean success);
    }


    public interface Presenter{

        void updateUser(int id,String fName,String lName,String gender,String country,JSONObject address,String dob,String phone,String region);
        void destroy();

    }
}
