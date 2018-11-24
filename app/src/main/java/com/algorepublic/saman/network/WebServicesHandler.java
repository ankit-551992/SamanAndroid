package com.algorepublic.saman.network;


import com.algorepublic.saman.data.model.apis.AddAddressApi;
import com.algorepublic.saman.data.model.apis.CustomerSupport;
import com.algorepublic.saman.data.model.apis.GetAddressApi;
import com.algorepublic.saman.data.model.apis.GetProduct;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.apis.GetStore;
import com.algorepublic.saman.data.model.apis.HomeScreenAPI;
import com.algorepublic.saman.data.model.apis.OrderHistoryAPI;
import com.algorepublic.saman.data.model.apis.PlaceOrderResponse;
import com.algorepublic.saman.data.model.apis.PromoVerify;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.GetCategoriesList;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;


public class WebServicesHandler {

    public static WebServicesHandler instance = new WebServicesHandler();

    private WebServices webServices;

    private WebServicesHandler() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URLS.BaseURLApis)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        webServices = retrofit.create(WebServices.class);
    }

    public void login(String email, String password, String deviceToken, Callback<UserResponse> callback) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", email);
        parameters.put("password", password);
        parameters.put("deviceType", "2");
        parameters.put("deviceToken", deviceToken);

        Call<UserResponse> call = webServices.login(parameters);
        call.enqueue(callback);

    }

    public void register(String fName, String lName, String email, String password, String deviceToken, String gender, String country,
                         String address, Callback<UserResponse> callback) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Email", email);
        parameters.put("Password", password);
        parameters.put("deviceType", "2");
        parameters.put("deviceToken", deviceToken);
        parameters.put("Gender", gender);
        parameters.put("Country", country);
        parameters.put("Address", address);

        Call<UserResponse> call = webServices.register(parameters);
        call.enqueue(callback);
    }


    public void updateUser(int id, String fName, String lName, String gender, String country,
                           JSONObject address, Callback<SimpleSuccess> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", id);
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Gender", gender);
        parameters.put("Country", country);
        parameters.put("ShippingAddress", address);

        Call<SimpleSuccess> call = webServices.updateProfile(parameters);
        call.enqueue(callback);
    }

    public void forgetPassword(String email, Callback<SimpleSuccess> callback) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
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


    public void placeOrder(int CustomerID,
                           int BillingAddressID,
                           int ShippingAddressID,
                           float ShippingTotal,
                           float TotalPrice,
                           String PaymentType,
                           JSONArray array,
                           Callback<PlaceOrderResponse> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CustomerID", CustomerID);
        parameters.put("BillingAddressID", BillingAddressID);
        parameters.put("ShippingAddressID", ShippingAddressID);
        parameters.put("PaymentType", PaymentType);
        parameters.put("ShippingTotal", ShippingTotal);
        parameters.put("TotalPrice", TotalPrice);

        for (int i = 0; i < array.length(); i++) {
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
        }
//        parameters.put("OrderItems", array);


        Call<PlaceOrderResponse> call = webServices.placeOrder(parameters);
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

    public void getStore(int storeId,Callback<GetStore> callback) {
        Call<GetStore> call = webServices.getStore(storeId);
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
        Call<GetProduct> call = webServices.getProductDetail(productId, userID);
        call.enqueue(callback);
    }


    public void getProductsByStore(int StoreId, int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByStore(StoreId, userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getProductsByStoreAndCategory(int StoreId,int categoryId,int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByStoreAndCategory(StoreId,categoryId,userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getLatestProducts(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getLatestProducts(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getSearchProducts(int userID,String query,int sortType, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getSearchProducts(userID,query,sortType,pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void applyPromo(String promo, Callback<PromoVerify> callback) {
        Call<PromoVerify> call = webServices.applyPromo(promo);
        call.enqueue(callback);
    }

    public void markFavourite(int userID, int productId, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("productID", productId);
        Call<SimpleSuccess> call = webServices.markFavorite(parameters);
        call.enqueue(callback);
    }

    public void markUnFavourite(int userID, int productId, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("productID", productId);
        Call<SimpleSuccess> call = webServices.markUnFavorite(parameters);
        call.enqueue(callback);
    }

    public void getAddressList(int userId, Callback<GetAddressApi> callback) {
        Call<GetAddressApi> call = webServices.getAddresses(userId);
        call.enqueue(callback);
    }

    public void addAddress(int userID,String addressLine,boolean isDefault, Callback<AddAddressApi> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("AddressLine1", addressLine);
        parameters.put("isDefault", isDefault);

        Call<AddAddressApi> call = webServices.insertAddress(parameters);
        call.enqueue(callback);
    }

    public void updateAddress(int ID,String addressLine,boolean isDefault, Callback<SimpleSuccess> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", ID);
        parameters.put("AddressLine1", addressLine);
        parameters.put("isDefault", isDefault);

        Call<SimpleSuccess> call = webServices.updateAddress(parameters);
        call.enqueue(callback);
    }

    public void deleteAddress(int ID, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("addressID", ID);
        Call<SimpleSuccess> call = webServices.deleteAddress(parameters);
        call.enqueue(callback);
    }

    public void uploadImage(int userId,File file, Callback<UserResponse> callback) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        Call<UserResponse> call = webServices.postImage(userId,body);
        call.enqueue(callback);
    }

    public void uploadToSupport(int userID,String subject,String message,List<File> files,Callback<CustomerSupport> callback) {

        MultipartBody.Part[] SupportImages = new MultipartBody.Part[files.size()];

        for (int index = 0; index < files.size(); index++) {
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), files.get(index));
            SupportImages[index] = MultipartBody.Part.createFormData("SupportImages["+index+"]", files.get(index).getName(), surveyBody);
        }
        Call<CustomerSupport> call = webServices.uploadToSupport(userID,subject,message,SupportImages);
        call.enqueue(callback);

    }
}
