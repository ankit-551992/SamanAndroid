package com.algorepublic.saman.ui.activities.register;


import com.algorepublic.saman.data.model.apis.UserResponse;

import java.util.ArrayList;

public class RegisterPresenterImpl implements RegisterPresenter,RegisterData.OnResponseListener {


    private RegisterView registerView;
    private RegisterDataInteractor registerDataInteractor;

    public RegisterPresenterImpl(RegisterView registerView, RegisterDataInteractor registerDataInteractor) {
        this.registerView = registerView;
        this.registerDataInteractor = registerDataInteractor;
    }

    @Override
    public void getCountries(){
        registerDataInteractor.getCountries(this);
    }

    @Override
    public void onResponse(UserResponse response) {
        if (registerView != null) {
            registerView.hideProgress();
            if(response!=null) {
                if (response.getSuccess() == 1) {
                    registerView.registerResponse(response.getUser());
                } else if (response.getSuccess() == 0) {
                    registerView.registerError(response.getMessage());
                }
            }else {
                registerView.registerError("Internal Server Error");
            }
        }
    }

    @Override
    public void setCountries(ArrayList<String> countries) {
        if(registerView!=null){
            registerView.countries(countries);
        }
    }

    @Override
    public void registerUser(String fName,String lName,String email,String password,String deviceToken,String gender,String country,String address) {
        if (registerView != null) {
            registerView.showProgress();
        }

        registerDataInteractor.registerUser(fName,lName,email,password,deviceToken,gender,country,address,this);
    }

    @Override
    public void onDestroy() {
        registerView=null;
    }


}
