package com.algorepublic.saman.network;

import com.algorepublic.saman.data.model.apis.GetProduct;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.apis.HomeScreenAPI;
import com.algorepublic.saman.data.model.apis.OrderHistoryAPI;
import com.algorepublic.saman.data.model.apis.PlaceOrderResponse;
import com.algorepublic.saman.data.model.apis.PromoVerify;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.GetCategoriesList;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.ui.activities.login.LoginData;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @POST("User/UpdateProfile")
    Call<SimpleSuccess> updateProfile(@FieldMap Map<String, Object> parameters);


    @FormUrlEncoded
    @POST("User/ForgetPassword")
    Call<SimpleSuccess> forgetPassword(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("User/ResetPassword")
    Call<SimpleSuccess> resetPassword(@FieldMap Map<String, String> parameters);


    @FormUrlEncoded
    @POST("User/ChangePassword")
    Call<UserResponse> changePassword(@FieldMap Map<String, Object> parameters);



    @GET("Home/GetHomeScreenData?")
    Call<HomeScreenAPI> getHomeScreenData(@Query("userID") int userID);


    @GET("Order/GetByUserID/{id}")
    Call<OrderHistoryAPI> getOrders(@Path("id") int productId);

    @GET("Product/GetFavoriteList")
    Call<GetProducts> getFavoriteList(@Query("userID") int userID,@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);


    @FormUrlEncoded
    @POST("Order/PlaceOrder")
    Call<PlaceOrderResponse> placeOrder(@FieldMap Map<String, Object> parameters);


    @GET("Catalog/Categories")
    Call<GetCategoriesList> getStoreCategories();

    @GET("Seller/GetByCategory?")
//    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID, @Query("page") String page);
    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID);


    @GET("Product/GetListByStore?")
    Call<GetProducts> getProductsByStore(@Query("storeID") int storeID,@Query("userID") int userID,@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);


    @GET("Product/GetLatestProducts?")
    Call<GetProducts> getLatestProducts(@Query("userID") int userID,@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);

    @GET("Product/Search?")
    Call<GetProducts> getSearchProducts(@Query("userID") int userID,@Query("q") String q,@Query("sortType") int sortType,@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);


    @GET("Coupon/Verify?")
    Call<PromoVerify> applyPromo(@Query("code") String code);

    @FormUrlEncoded
    @POST("Product/MarkAsFavorite")
    Call<SimpleSuccess> markFavorite(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Product/MarkAsUnfavorite")
    Call<SimpleSuccess> markUnFavorite(@FieldMap Map<String, Object> parameters);

    @GET("Seller")
    Call<GetStores> getAllStores();

    @GET("Product/Get/{id}?")
    Call<GetProduct> getProductDetail(@Path("id") String productId,@Query("userID") String userID);

    @Multipart
    @POST("User/UpdateProfilePicture?")
    Call<UserResponse> postImage(@Query("userID") int userID, @Part MultipartBody.Part image);

    @Multipart
    @POST("User/UpdateProfilePicture?")
    Call<SimpleSuccess> uploadToSupport(@Query("userID") int userID,
                                        @Query("subject") String subject,
                                        @Query("message") String message,
                                        @Part MultipartBody.Part[] images);

}
