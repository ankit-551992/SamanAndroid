package com.algorepublic.saman.network;

import com.algorepublic.saman.data.model.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.GetCategoriesList;
import com.algorepublic.saman.data.model.UserResponse;
import com.algorepublic.saman.data.model.apis.GetStores;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface WebServices {


    @FormUrlEncoded
    @POST("api/User/Login")
    Call<UserResponse> login(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("api/User/Register")
    Call<UserResponse> register(@FieldMap Map<String, String> parameters);


    @FormUrlEncoded
    @POST("api/User/ForgetPassword")
    Call<SimpleSuccess> forgetPassword(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("api/User/ChangePassword")
    Call<SimpleSuccess> resetPassword(@FieldMap Map<String, String> parameters);


    @GET("Catalog/Categories")
    Call<GetCategoriesList> getStoreCategories();

    @GET("Seller/GetByCategory?")
//    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID, @Query("page") String page);
    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID);

    @GET("Seller")
    Call<GetStores> getAllStores();

}
