package com.algorepublic.saman.ui.activities.register;

public interface RegisterPresenter {


    void registerUser(String fName,String lName,String email,String password,String deviceToken,String gender,String country,String address);
    void onDestroy();
    void getCountries();

}
