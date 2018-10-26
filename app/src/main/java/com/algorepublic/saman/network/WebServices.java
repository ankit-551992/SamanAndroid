package com.algorepublic.saman.network;

import com.algorepublic.saman.data.model.apis.GetProduct;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.GetCategoriesList;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.data.model.apis.GetStores;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


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


    @GET("Catalog/Categories")
    Call<GetCategoriesList> getStoreCategories();

    @GET("Seller/GetByCategory?")
//    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID, @Query("page") String page);
    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID);

    @GET("Seller")
    Call<GetStores> getAllStores();

    @GET("Product/Get/{id}")
    Call<GetProduct> getProductDetail(@Path("id") String productId);

}
