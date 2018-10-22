package com.algorepublic.saman.network;

import com.algorepublic.saman.data.model.SimpleSuccess;
import com.algorepublic.saman.data.model.StoreCategories;
import com.algorepublic.saman.data.model.UserResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;



public interface WebServices {


    @FormUrlEncoded
    @POST("User/Login")
    Call<UserResponse> login(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("User/Register")
    Call<UserResponse> register(@FieldMap Map<String, String> parameters);


    @FormUrlEncoded
    @POST("User/ForgetPassword")
    Call<SimpleSuccess> forgetPassword(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("User/ChangePassword")
    Call<SimpleSuccess> resetPassword(@FieldMap Map<String, String> parameters);


    @FormUrlEncoded
    @POST("User/ChangePassword")
    Call<StoreCategories> getStoreCategories();

}
