package com.qtech.saman.network;


import android.util.Log;

import com.google.gson.Gson;
import com.qtech.saman.data.model.ApiViewCount;
import com.qtech.saman.data.model.ShippingAddress;
import com.qtech.saman.data.model.apis.AddAddressApi;
import com.qtech.saman.data.model.apis.ChangeLanguage;
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
import com.qtech.saman.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServicesHandler {

    public static WebServicesHandler instance = new WebServicesHandler();

    private WebServices webServices;

    private WebServicesHandler() {
        OkHttpClient.Builder httpClient ;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addNetworkInterceptor(logging);
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URLS.BaseURLApis)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        webServices = retrofit.create(WebServices.class);
    }

    public void login(String email, String password, String deviceToken, Callback<UserResponse> callback) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", email);
        parameters.put("password", password);
        parameters.put("deviceType", "2");
        if (deviceToken != null) {
            parameters.put("deviceToken", deviceToken);
        } else {
            parameters.put("deviceToken", "Emulator");
        }
        Log.e("LOGIN_URL", "----login---parameters---" + parameters);
        Call<UserResponse> call = webServices.login(parameters);
        call.enqueue(callback);
    }

    public void register(String fName, String lName, String email, String password, String deviceToken, String gender, String country,
                         String address,
                         String dob, String phone, String region, Callback<UserResponse> callback) {

        ShippingAddress obj = new Gson().fromJson(address, ShippingAddress.class);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Email", email);
        parameters.put("Password", password);
        parameters.put("deviceType", "2");
        if (deviceToken != null) {
            parameters.put("deviceToken", deviceToken);
        } else {
            parameters.put("deviceToken", "Emulator");
        }
        parameters.put("Gender", gender);
        parameters.put("Country", country);
        parameters.put("Address", address);
//        parameters.put("ShippingAddress", address);
        parameters.put("ShippingAddress[AddressLine1]", obj.getAddressLine1());
        parameters.put("ShippingAddress[AddressLine2]", obj.getAddressLine2());
        parameters.put("ShippingAddress[City]", obj.getCity());
        parameters.put("ShippingAddress[Country]", obj.getCountry());
        parameters.put("ShippingAddress[State]", obj.getState());
        parameters.put("DateOfBirth", dob);
        parameters.put("PhoneNumber", phone);
        parameters.put("Region", region);

        Log.e("SIGNUP_URL", "----sign---up---parameters---" + parameters);
        Call<UserResponse> call = webServices.register(parameters);
        call.enqueue(callback);
    }


    public void socialLogin(String fName, String lName, String email, String deviceToken, String socialImage, Callback<UserResponse> callback) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Email", email);
        parameters.put("deviceType", "2");
      /*parameters.put("Password", "");
        parameters.put("Gender", "");
        parameters.put("Country", "");
        parameters.put("ShippingAddress", "");
        parameters.put("DateOfBirth", "");
        parameters.put("MobileNumber", "");*/
        if (deviceToken != null) {
            parameters.put("deviceToken", deviceToken);
        } else {
            parameters.put("deviceToken", "Emulator");
        }
        parameters.put("ProfileImagePath", socialImage);

        Log.e("SOCIAL_LOGIN", "---SOCIAL_LOGIN---parameters--" + parameters);
        Call<UserResponse> call = webServices.socialLogin(parameters);
        call.enqueue(callback);
    }

    public void updateUser(int id, String fName, String lName, String gender, String country, JSONObject address, String dob, String phone, String region, Callback<UserResponse> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", id);
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Gender", gender);
        parameters.put("Country", country);
        parameters.put("ShippingAddress", address);
        parameters.put("DateOfBirth", dob);
        parameters.put("PhoneNumber", phone);
        parameters.put("Region", region);
      /*  try {
            parameters.put("ShippingAddress.ID", address.get("ID"));
            parameters.put("ShippingAddress.AddressLine1", address.get("AddressLine1"));
            parameters.put("ShippingAddress.Floor", address.get("Floor"));
            parameters.put("ShippingAddress.Apt",address.get("Apt"));
            parameters.put("ShippingAddress.City", address.get("City"));
            parameters.put("ShippingAddress.UserCountry",address.get("UserCountry"));
            parameters.put("ShippingAddress.UserRegion", address.get("UserRegion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        Log.e("UPDATE_PROFILEURL", "----profile---parameters---" + parameters);
        Call<UserResponse> call = webServices.updateProfile(parameters);
        call.enqueue(callback);
    }

    public void updateDeviceToken(int id, String token, Callback<UserResponse> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", id);
        parameters.put("deviceToken", token);
        Call<UserResponse> call = webServices.updateDeviceToken(parameters);
        call.enqueue(callback);
    }


    public void forgetPassword(String email, String phone, Callback<SimpleSuccess> callback) {

        Map<String, Object> parameters = new HashMap<>();
        if (email != null) {
            parameters.put("email", email);
            parameters.put("isEmail", true);
        } else if (phone != null) {
//          parameters.put("PhoneNumber", phone);
            parameters.put("email", phone);
            parameters.put("isEmail", false);
        }
        Log.e("FORGOT_URL", "----forget---parameters---" + parameters);
        Call<SimpleSuccess> call = webServices.forgetPassword(parameters);
        call.enqueue(callback);
    }

    public void resetPassword(String email, String password, Callback<SimpleSuccess> callback) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("token", email);
        parameters.put("password", password);

        Call<SimpleSuccess> call = webServices.resetPassword(parameters);
        call.enqueue(callback);
    }


    public void ChangePassword(int userID, String password, String oldPassword, Callback<UserResponse> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("password", password);
        parameters.put("oldPassword", oldPassword);

        Call<UserResponse> call = webServices.changePassword(parameters);
        call.enqueue(callback);
    }

    public void updatePaymentStatus(int orderID, int paymentStatus, Callback<SimpleSuccess> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderID", orderID);
        parameters.put("paymentStatus", paymentStatus);

        Call<SimpleSuccess> call = webServices.updatePaymentStatus(parameters);
        call.enqueue(callback);
    }

    public void placeOrder(int CustomerID,
                           int BillingAddressID,
                           int ShippingAddressID,
                           float ShippingTotal,
                           double TotalPrice,
                           String PaymentType,
                           String discount_couponId,
                           String discount_price,
                           String couponCode,
                           JSONArray array,
                           Callback<PlaceOrderResponse> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CustomerID", CustomerID);
        parameters.put("BillingAddressID", BillingAddressID);
        parameters.put("ShippingAddressID", ShippingAddressID);
        parameters.put("PaymentType", PaymentType);
        parameters.put("ShippingTotal", ShippingTotal);
        parameters.put("TotalPrice", TotalPrice);
        parameters.put("DiscountCoupanID", discount_couponId);
        parameters.put("Discount", discount_price);
        parameters.put("DiscountCouponCode", couponCode);
        parameters.put("OrderItems_", array);
      /*  for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                parameters.put("OrderItems[" + i + "].ProductID", jsonObject.getInt("ProductID"));
                parameters.put("OrderItems[" + i + "].ProductQuantity", jsonObject.getInt("ProductQuantity"));
                parameters.put("OrderItems[" + i + "].ProductPrice", jsonObject.getInt("ProductPrice"));
                JSONArray optionsArray = jsonObject.getJSONArray("OrderOptionValue");
                for (int j = 0; j < optionsArray.length(); j++) {
                    JSONObject jsonObj = optionsArray.getJSONObject(j);
                    parameters.put("OrderItems[" + i + "].OrderOptionValue[" + j + "].ID", jsonObj.getInt("ID"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        Log.e("PARAMETER", "---order--parameter---" + parameters.toString());
        Call<PlaceOrderResponse> call = webServices.placeOrder(parameters);
        call.enqueue(callback);
    }


    public void isPaymentSuccessful(int orderID, Callback<SimpleSuccess> callback) {
        Call<SimpleSuccess> call = webServices.isPaymentSuccessful(orderID);
        call.enqueue(callback);
    }

    public void getHomeScreenData(int userID, Callback<HomeScreenAPI> callback) {
        Call<HomeScreenAPI> call = webServices.getHomeScreenData(userID);
        call.enqueue(callback);
    }

    public void getOrderHistory(int userID, Callback<OrderHistoryAPI> callback) {
        Call<OrderHistoryAPI> call = webServices.getOrders(userID);
        call.enqueue(callback);
    }

    public void getFavoriteList(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getFavoriteList(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getStoreCategories(Callback<GetCategoriesList> callback) {
        Call<GetCategoriesList> call = webServices.getStoreCategories();
        call.enqueue(callback);
    }

    public void getStore(int storeId, Callback<GetStore> callback) {
        Call<GetStore> call = webServices.getStore(storeId);
        call.enqueue(callback);
    }

    public void getStoreByIdAndBanner(int storeId, int bannerId, Callback<GetStore> callback) {
        Call<GetStore> call = webServices.getStoreByIdAndBanner(storeId, bannerId);
        call.enqueue(callback);
    }

    public void getStoresByCategory(String categoryID, Callback<GetStores> callback) {
        Call<GetStores> call = webServices.getStoresByCategoryID(categoryID);
        call.enqueue(callback);
    }

    public void getAllStores(Callback<GetStores> callback) {
        Call<GetStores> call = webServices.getAllStores();
        call.enqueue(callback);
    }

    public void getProductDetail(String productId, String userID, Callback<GetProduct> callback) {
        Log.e("PRODUCT888", "--productId--" + productId + "--userID---" + userID);
        Call<GetProduct> call = webServices.getProductDetail(productId, userID);
        call.enqueue(callback);
    }

    public void getFavProductDetail(String productId, String userID, Callback<GetProduct> callback) {
        Call<GetProduct> call = webServices.getProductDetail(productId, userID);
        call.enqueue(callback);
    }

    public void getOrderStatus(int orderID, Callback<OrderTrackResponse> callback) {
        Call<OrderTrackResponse> call = webServices.getOrderStatus(orderID);
        call.enqueue(callback);
    }

    public void getOrderIdDetailes(int orderID, Callback<OrderHistoryAPI> callback) {
        Call<OrderHistoryAPI> call = webServices.getOrdersIdDetails(orderID);
        call.enqueue(callback);
    }


    public void getProductsByStore(int StoreId, int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByStore(StoreId, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getProductsByStoreAndCategory(int StoreId, int categoryId, int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByStoreAndCategory(StoreId, categoryId, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getProductsByStoreAndCategoryForBanner(int bannerID, int StoreId, int categoryId, int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByStoreAndCategoryForBanner(bannerID, StoreId, categoryId, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getLatestProducts(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getLatestProducts(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getNewInProductList(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getNewInProductList(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getAllProducts(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getAllProducts(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getProductsByCategory(int categoryId, int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByCategory(categoryId, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getProductsByCategoryForBanner(int bannerID, int categoryId, int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByCategoryForBanner(bannerID, categoryId, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getSaleProducts(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getSaleProducts(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getSaleListByCategory(int categoryId, int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getSaleListByCategory(categoryId, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getSearchProducts(int userID, String query, int sortType, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getSearchProducts(userID, query, sortType, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void applyPromo(String promo, Callback<PromoVerify> callback) {
        Call<PromoVerify> call = webServices.applyPromo(promo);
        call.enqueue(callback);
    }

    public void getCountries(Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServices.getCountries();
        call.enqueue(callback);
    }

    public void markFavourite(int userID, int productId, String[] optionIDs, int quantity, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("productID", productId);
        parameters.put("Quantity", quantity);

        if (optionIDs != null) {
            for (int i = 0; i < optionIDs.length; i++) {
                if (!optionIDs[i].equals("")) {
                    parameters.put("OrderOptionValue[" + i + "].ID", optionIDs[i]);
                }
            }
        }

        Call<SimpleSuccess> call = webServices.markFavorite(parameters);
        call.enqueue(callback);
    }

    public void markUnFavourite(int userID, int productId, String[] optionIDs, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("productID", productId);

        if (optionIDs != null) {
            for (int i = 0; i < optionIDs.length; i++) {
                if (!optionIDs[i].equals("")) {
                    parameters.put("OrderOptionValue[" + i + "].ID", optionIDs[i]);
                }
            }
        }

        Call<SimpleSuccess> call = webServices.markUnFavorite(parameters);
        call.enqueue(callback);
    }

    public void getAddressList(int userId, Callback<GetAddressApi> callback) {
        Call<GetAddressApi> call = webServices.getAddresses(userId);
        call.enqueue(callback);
    }

    public void addAddress(int userID, String addressLine, String floor, String apartment, String addressLine2, String city, String state, String country, String region, String latitude, String longitude, boolean isDefault, Callback<AddAddressApi> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("AddressLine1", addressLine);
        parameters.put("AddressLine2", addressLine2);
        parameters.put("City", city);
        parameters.put("State", state);
        parameters.put("UserCountry", country);
        parameters.put("UserRegion", region);
        parameters.put("isDefault", isDefault);
        parameters.put("Floor", floor);
        parameters.put("Apt", apartment);
        parameters.put("Latitude", latitude);
        parameters.put("Longitude", longitude);

        Call<AddAddressApi> call = webServices.insertAddress(parameters);
        call.enqueue(callback);
    }

    public void updateAddress(int user_id, int ID, String addressLine, String addressLine2, String floor, String apartment, String city, String state, String country, String region, String latitude, String longitude, boolean isDefault, Callback<SimpleSuccess> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", user_id);
        parameters.put("ID", ID);
        parameters.put("AddressLine1", addressLine);
        parameters.put("AddressLine2", addressLine2);
        parameters.put("City", city);
        parameters.put("State", state);
//      parameters.put("Country", country);
        parameters.put("UserCountry", country);
        parameters.put("UserRegion", region);
        parameters.put("isDefault", isDefault);
        parameters.put("Floor", floor);
        parameters.put("Apt", apartment);
        parameters.put("Latitude", latitude);
        parameters.put("Longitude", longitude);

        Log.e("ADDRE00", "--parameters----" + parameters);
        Call<SimpleSuccess> call = webServices.updateAddress(parameters);
        call.enqueue(callback);
    }

    public void deleteAddress(int ID, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("addressID", ID);
        Call<SimpleSuccess> call = webServices.deleteAddress(parameters);
        call.enqueue(callback);
    }

    public void uploadImage(int userId, File file, Callback<UserResponse> callback) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getPath(), reqFile);
        Call<UserResponse> call = webServices.postImage(userId, body);
        call.enqueue(callback);
    }

    public void uploadToSupport(int userID, String subject, String message, List<File> files, Callback<CustomerSupport> callback) {

        if (files.size() > 0) {
            MultipartBody.Part[] SupportImages = new MultipartBody.Part[files.size()];

            for (int index = 0; index < files.size(); index++) {
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), files.get(index));
                SupportImages[index] = MultipartBody.Part.createFormData("SupportImages[" + index + "]", files.get(index).getName(), surveyBody);
            }
            Call<CustomerSupport> call = webServices.uploadToSupport(userID, subject, message, SupportImages);
            call.enqueue(callback);
        } else {
            Call<CustomerSupport> call = webServices.uploadToSupport(userID, subject, message);
            call.enqueue(callback);
        }
    }

    public void getConversationList(int userId, Callback<GetConversationsApi> callback) {
        Call<GetConversationsApi> call = webServices.getConversationList(userId);
        call.enqueue(callback);
    }

    public void getConversation(int conversationID, Callback<GetConversationApi> callback) {
        Call<GetConversationApi> call = webServices.getConversation(conversationID);
        call.enqueue(callback);
    }

    public void deleteConversation(int conversationID, Callback<SimpleSuccess> callback) {
        Call<SimpleSuccess> call = webServices.deleteConversation(conversationID);
        call.enqueue(callback);
    }

    public void notifyAddProduct(int userId, int productId, Callback<SimpleSuccess> callback) {
        Call<SimpleSuccess> call = webServices.addNotifyProduct(userId, productId);
        call.enqueue(callback);
    }

    public void sendMessage(int userID, int recipientID, int conversationID, String title, String message, Callback<SendMessageApi> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("UserID", userID);
        parameters.put("ConversationID", conversationID);
        parameters.put("RecipentID", recipientID);
        parameters.put("Title", title);
        parameters.put("MessageBody", message);
        Call<SendMessageApi> call = webServices.sendMessage(parameters);
        call.enqueue(callback);
    }

    public void updateMessageStatus(int conversationID, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("conversationID", conversationID);
        Call<SimpleSuccess> call = webServices.updateMessageStatus(parameters);
        call.enqueue(callback);
    }

    public void sendVerificationCode(String number, Callback<PhoneCodeResponse> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Call<PhoneCodeResponse> call = webServices.sendVerificationCode(parameters);
        call.enqueue(callback);
    }

    public void updateOrderFeedback(int orderID, float rating, String feedback, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderID", orderID);
        parameters.put("rating", rating);
        parameters.put("feedback", feedback);
        Call<SimpleSuccess> call = webServices.updateOrderFeedback(parameters);
        call.enqueue(callback);
    }

    public void getSupportListByUser(int userId, Callback<CustomerSupportListApi> callback) {
        Call<CustomerSupportListApi> call = webServices.getSupportUserList(userId);
        call.enqueue(callback);
    }

    public void getTicketByID(int ticketId, Callback<CustomerSupportListApi> callback) {
        Call<CustomerSupportListApi> call = webServices.getTicketByID(ticketId);
        call.enqueue(callback);
    }

    public void getUserInfo(int userID, Callback<UserResponse> callback) {
        Call<UserResponse> call = webServices.getUserInfo(userID);
        call.enqueue(callback);
    }

    public void getCancelOrderApi(int orderID, String comment, int orderStatus, int userId, Callback<SimpleSuccess> callback) {
        Log.e("RES00000", "---orderID---" + orderID + "-orderStatus-" + orderStatus + "-userId--" + userId);
        Call<SimpleSuccess> call = webServices.cancelOrder(orderID, comment, orderStatus, userId);
        call.enqueue(callback);
    }

    public void getBannerProduct(int bannerID, String userId, int pageIndex, int pageSize, Callback<GetStores> callback) {
        Log.e("PRODUCT888", "--bannerID--" + bannerID + "--userID---" + userId);
        Call<GetStores> call = webServices.getProductListByBannerId(bannerID, userId, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getBannerProductList(int bannerID, String userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Log.e("PRODUCT888", "--getBannerProductList--" + bannerID + "--userID---" + userID);
        Call<GetProducts> call = webServices.getBannerProductList(bannerID, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getBannerProductCategory(int bannerID, String userID, int pageIndex, int pageSize, Callback<GetCategoriesList> callback) {
        Log.e("PRODUCT888", "--getBannerProductList--" + bannerID + "--userID---" + userID);
        Call<GetCategoriesList> call = webServices.getBannerProductCategory(bannerID, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getAppViewCount(Callback<ApiViewCount> callback) {

        int deviceType = 2;
        Call<ApiViewCount> call = webServices.getAppViewCountApi(deviceType);
        call.enqueue(callback);
    }

    public void getChangeLanguage(String userID, int lang, Callback<ChangeLanguage> callback) {
        int deviceType = 2;
        Call<ChangeLanguage> call = webServices.getChangeLanguage(userID, lang);
        call.enqueue(callback);
    }
}
