package com.qtech.saman.network;

import com.qtech.saman.data.model.ApiViewCount;
import com.qtech.saman.data.model.apis.AddAddressApi;
import com.qtech.saman.data.model.apis.CustomerSupport;
import com.qtech.saman.data.model.apis.CustomerSupportListApi;
import com.qtech.saman.data.model.apis.GetAddressApi;
import com.qtech.saman.data.model.apis.GetCategoriesList;
import com.qtech.saman.data.model.apis.GetConversationApi;
import com.qtech.saman.data.model.apis.GetConversationsApi;
import com.qtech.saman.data.model.apis.GetProduct;
import com.qtech.saman.data.model.apis.GetProducts;
import com.qtech.saman.data.model.apis.GetStore;
import com.qtech.saman.data.model.apis.GetStores;
import com.qtech.saman.data.model.apis.HomeScreenAPI;
import com.qtech.saman.data.model.apis.OrderHistoryAPI;
import com.qtech.saman.data.model.apis.OrderTrackResponse;
import com.qtech.saman.data.model.apis.PhoneCodeResponse;
import com.qtech.saman.data.model.apis.PlaceOrderResponse;
import com.qtech.saman.data.model.apis.PromoVerify;
import com.qtech.saman.data.model.apis.SendMessageApi;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.data.model.apis.UserResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    @POST("User/SocialLogin")
    Call<UserResponse> socialLogin(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("User/UpdateProfile")
    Call<UserResponse> updateProfile(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("User/UpdateDeviceToken")
    Call<UserResponse> updateDeviceToken(@FieldMap Map<String, Object> parameters);


    @FormUrlEncoded
    @POST("User/ForgetPassword")
    Call<SimpleSuccess> forgetPassword(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("User/ResetPassword")
    Call<SimpleSuccess> resetPassword(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("User/ChangePassword")
    Call<UserResponse> changePassword(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Order/UpdatePaymentStatus")
    Call<SimpleSuccess> updatePaymentStatus(@FieldMap Map<String, Object> parameters);

    @GET("Home/GetHomeScreenData?")
    Call<HomeScreenAPI> getHomeScreenData(@Query("userID") int userID);

    @GET("Order/GetByUserID/{id}")
    Call<OrderHistoryAPI> getOrders(@Path("id") int userID);

    @GET("Product/GetFavoriteList")
    Call<GetProducts> getFavoriteList(@Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("Order/PlaceOrder")
    Call<PlaceOrderResponse> placeOrder(@FieldMap Map<String, Object> parameters);

    @GET("Order/isPaymentSuccessful/{id}")
    Call<SimpleSuccess> isPaymentSuccessful(@Path("id") int orderID);

    @GET("Catalog/Categories")
    Call<GetCategoriesList> getStoreCategories();

    @GET("Seller/Get/{id}")
    Call<GetStore> getStore(@Path("id") int storeId);

    @GET("Seller/GetByCategory?")
//    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID, @Query("page") String page);
    Call<GetStores> getStoresByCategoryID(@Query("categoryID") String categoryID);

    @GET("Product/GetListByStore?")
    Call<GetProducts> getProductsByStore(@Query("storeID") int storeID, @Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Product/GetListByStoreAndCategory?")
    Call<GetProducts> getProductsByStoreAndCategory(@Query("storeID") int storeID, @Query("categoryID") int categoryID, @Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Product/GetLatestProducts?")
    Call<GetProducts> getLatestProducts(@Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Product/GetLatestProducts?")
    Call<GetProducts> getAllProducts(@Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Product/GetListByCategory?")
    Call<GetProducts> getProductsByCategory(@Query("categoryID") int categoryID, @Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Product/GetSaleProducts?")
    Call<GetProducts> getSaleProducts(@Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Product/GetSaleListByCategory?")
    Call<GetProducts> getSaleListByCategory(@Query("categoryID") int categoryID, @Query("userID") int userID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Product/Search?")
    Call<GetProducts> getSearchProducts(@Query("userID") int userID, @Query("q") String q, @Query("sortType") int sortType, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("Coupon/Verify?")
    Call<PromoVerify> applyPromo(@Query("code") String code);

    @GET("Address/GetCountryList")
    Call<ResponseBody> getCountries();

    @FormUrlEncoded
    @POST("Product/MarkAsFavorite")
    Call<SimpleSuccess> markFavorite(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Product/MarkAsUnfavorite")
    Call<SimpleSuccess> markUnFavorite(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Address/Insert")
    Call<AddAddressApi> insertAddress(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Address/Update")
    Call<SimpleSuccess> updateAddress(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Address/Delete")
    Call<SimpleSuccess> deleteAddress(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Message/SendMessageInConversation")
    Call<SendMessageApi> sendMessage(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Message/UpdateMessageStatus")
    Call<SimpleSuccess> updateMessageStatus(@FieldMap Map<String, Object> parameters);

    @GET("Seller")
    Call<GetStores> getAllStores();

    @GET("Product/Get/{id}?")
    Call<GetProduct> getProductDetail(@Path("id") String productId, @Query("userID") String userID);

    @GET("Product/GetFavoriteProduct/{id}?")
    Call<GetProduct> getFavProductDetail(@Path("id") String productId, @Query("userID") String userID);

    @GET("Order/GetOrderStatus/{id}?")
    Call<OrderTrackResponse> getOrderStatus(@Path("id") int orderID);

    @GET("Address/GetListByUserID?")
    Call<GetAddressApi> getAddresses(@Query("userID") int userID);

    @GET("Message/GetListByUserID?")
    Call<GetConversationsApi> getConversationList(@Query("userID") int userID);

    @GET("Message/Get?")
    Call<GetConversationApi> getConversation(@Query("conversationID") int userID);

    @GET("Message/Delete?")
    Call<SimpleSuccess> deleteConversation(@Query("conversationID") int userID);

    @Multipart
    @POST("User/UpdateProfilePicture?")
    Call<UserResponse> postImage(@Query("userID") int userID, @Part MultipartBody.Part image);

    @Multipart
    @POST("Support/CreateTicket?")
    Call<CustomerSupport> uploadToSupport(@Query("UserID") int userID,
                                          @Query("Subject") String subject,
                                          @Query("Message") String message,
                                          @Part MultipartBody.Part[] images);

    @POST("Support/CreateTicket?")
    Call<CustomerSupport> uploadToSupport(@Query("UserID") int userID,
                                          @Query("Subject") String subject,
                                          @Query("Message") String message);

    @FormUrlEncoded
    @POST("User/SendVerificationCode")
    Call<PhoneCodeResponse> sendVerificationCode(@FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST("Order/UpdateOrderFeedback")
    Call<SimpleSuccess> updateOrderFeedback(@FieldMap Map<String, Object> parameters);

    @GET("Support/GetListByUserID?")
    Call<CustomerSupportListApi> getSupportUserList(@Query("userID") int userID);

    @GET("Order/Get?")
    Call<OrderHistoryAPI> getOrdersIdDetails(@Query("id") int orderID);

    @GET("Support/GetTicketById?")
    Call<CustomerSupportListApi> getTicketByID(@Query("Id") int ticketId);

    @POST("User/AddNotifyProduct?")
    Call<SimpleSuccess> addNotifyProduct(@Query("UserID") int userId,@Query("ProductID") int productId);

    @GET("User/GetByID?")
    Call<UserResponse> getUserInfo(@Query("userID") int userId);

    @POST("Order/UpdateOrderStatus?")
    Call<SimpleSuccess> cancelOrder(@Query("orderID") int orderId,
                                   @Query("comment") String comment,
                                   @Query("status") int orderStatus,
                                   @Query("UserId") int userId);

    @GET("product/GetProductLisByBannesId?")
    Call<GetStores> getProductListByBannerId(@Query("bannerID") int bannerID,
                                             @Query("userID") String userId,
                                             @Query("pageIndex") int pageIndex,
                                             @Query("pageSize") int pageSize);

    @GET("product/GetProductLisByBannesId?")
    Call<GetProducts> getBannerProductList(@Query("bannerID") int bannerID,
                                             @Query("userID") String userId,
                                             @Query("pageIndex") int pageIndex,
                                             @Query("pageSize") int pageSize);

    @GET("product/GetProductLisByBannesId?")
    Call<GetCategoriesList> getBannerProductCategory(@Query("bannerID") int bannerID,
                                                 @Query("userID") String userId,
                                                 @Query("pageIndex") int pageIndex,
                                                 @Query("pageSize") int pageSize);

    @POST("home/AppViewCount?")
    Call<ApiViewCount> getAppViewCountApi(@Query("DeviceType") int deviceType);
}
