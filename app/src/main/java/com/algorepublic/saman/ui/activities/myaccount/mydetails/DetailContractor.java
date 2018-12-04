package com.algorepublic.saman.ui.activities.myaccount.mydetails;

import com.algorepublic.saman.data.model.User;

import org.json.JSONObject;

public class DetailContractor {

    public interface View{
        void showProgress();
        void hideProgress();
        void updateResponse(boolean success);
    }


    public interface Presenter{

        void updateUser(int id,String fName,String lName,String gender,String country,JSONObject address,String dob);
        void destroy();

    }
}
