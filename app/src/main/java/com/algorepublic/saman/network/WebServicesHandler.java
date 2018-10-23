package com.algorepublic.saman.network;


import com.algorepublic.saman.data.model.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.GetCategoriesList;
import com.algorepublic.saman.data.model.UserResponse;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.utils.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServicesHandler {

    public static WebServicesHandler instance = new WebServicesHandler();

    private WebServices webServices;

    private WebServicesHandler() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URLS.BaseApis)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        webServices = retrofit.create(WebServices.class);
    }

    public void login(String email, String password,String deviceToken, Callback<UserResponse> callback) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", email);
        parameters.put("password", password);
        parameters.put("deviceType", "0");
        parameters.put("deviceToken", deviceToken);

        Call<UserResponse> call = webServices.login(parameters);
        call.enqueue(callback);

    }

    public void register(String fName,String lName,String email,String password,String deviceToken,String gender,String country,
                         String address,Callback<UserResponse> callback) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Email", email);
        parameters.put("Password", password);
        parameters.put("deviceType", "0");
        parameters.put("deviceToken", deviceToken);
        parameters.put("Gender", gender);
        parameters.put("Country", country);
        parameters.put("Address", address);

        Call<UserResponse> call = webServices.register(parameters);
        call.enqueue(callback);
    }

    public void forgetPassword(String email,Callback<SimpleSuccess> callback) {
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

    public void getStoreCategories(Callback<GetCategoriesList> callback) {
        Call<GetCategoriesList> call = webServices.getStoreCategories();
        call.enqueue(callback);
    }


    public void getStoresByCategory(String categoryID,Callback<GetStores> callback) {
        Call<GetStores> call = webServices.getStoresByCategoryID(categoryID);
        call.enqueue(callback);
    }
    public void getAllStores(Callback<GetStores> callback) {
        Call<GetStores> call = webServices.getAllStores();
        call.enqueue(callback);
    }


}
