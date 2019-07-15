package com.algorepublic.saman.network;


import com.algorepublic.saman.data.model.ShippingAddress;
import com.algorepublic.saman.data.model.apis.AddAddressApi;
import com.algorepublic.saman.data.model.apis.CustomerSupport;
import com.algorepublic.saman.data.model.apis.GetAddressApi;
import com.algorepublic.saman.data.model.apis.GetConversationApi;
import com.algorepublic.saman.data.model.apis.GetConversationsApi;
import com.algorepublic.saman.data.model.apis.GetProduct;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.apis.GetStore;
import com.algorepublic.saman.data.model.apis.HomeScreenAPI;
import com.algorepublic.saman.data.model.apis.OrderHistoryAPI;
import com.algorepublic.saman.data.model.apis.OrderTrackResponse;
import com.algorepublic.saman.data.model.apis.PhoneCodeResponse;
import com.algorepublic.saman.data.model.apis.PlaceOrderResponse;
import com.algorepublic.saman.data.model.apis.PromoVerify;
import com.algorepublic.saman.data.model.apis.SendMessageApi;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.GetCategoriesList;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.utils.Constants;
import com.google.gson.Gson;
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
import okhttp3.ResponseBody;
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
        if (deviceToken != null) {
            parameters.put("deviceToken", deviceToken);
        }else {
            parameters.put("deviceToken", "Emulator");
        }

        Call<UserResponse> call = webServices.login(parameters);
        call.enqueue(callback);

    }

    public void register(String fName, String lName, String email, String password, String deviceToken, String gender, String country,
                         String address,
                         String dob, String phone,String region, Callback<UserResponse> callback) {

        ShippingAddress obj = new Gson().fromJson(address, ShippingAddress.class);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Email", email);
        parameters.put("Password", password);
        parameters.put("deviceType", "2");
        if (deviceToken != null) {
            parameters.put("deviceToken", deviceToken);
        }else {
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

        Call<UserResponse> call = webServices.register(parameters);
        call.enqueue(callback);
    }


    public void socialLogin(String fName, String lName, String email, String deviceToken, String socialImage, Callback<UserResponse> callback) {


        Map<String, String> parameters = new HashMap<>();
        parameters.put("FirstName", fName);
        parameters.put("LastName", lName);
        parameters.put("Email", email);
        parameters.put("deviceType", "2");
        if (deviceToken != null) {
            parameters.put("deviceToken", deviceToken);
        }else {
            parameters.put("deviceToken", "Emulator");
        }
        parameters.put("ProfileImagePath", socialImage);

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
//        parameters.put("ShippingAddress", address);
        parameters.put("DateOfBirth", dob);
        parameters.put("PhoneNumber", phone);
        parameters.put("Region", region);
        try {
            parameters.put("ShippingAddress.ID", address.get("ID"));
            parameters.put("ShippingAddress.AddressLine1", address.get("AddressLine1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
//            parameters.put("PhoneNumber", phone);
            parameters.put("email", phone);
            parameters.put("isEmail", false);
        }
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

    public void getFavProductDetail(String productId, String userID, Callback<GetProduct> callback) {
        Call<GetProduct> call = webServices.getProductDetail(productId, userID);
        call.enqueue(callback);
    }

    public void getOrderStatus(int orderID, Callback<OrderTrackResponse> callback) {
        Call<OrderTrackResponse> call = webServices.getOrderStatus(orderID);
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

    public void getLatestProducts(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getLatestProducts(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }
    public void getAllProducts(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getAllProducts(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getProductsByCategory(int categoryId,int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getProductsByCategory(categoryId,userID, pageIndex, pageSize);
        call.enqueue(callback);
    }


    public void getSaleProducts(int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getSaleProducts(userID, pageIndex, pageSize);
        call.enqueue(callback);
    }

    public void getSaleListByCategory(int categoryId,int userID, int pageIndex, int pageSize, Callback<GetProducts> callback) {
        Call<GetProducts> call = webServices.getSaleListByCategory(categoryId,userID, pageIndex, pageSize);
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

    public void markFavourite(int userID, int productId, String[] optionIDs,int quantity, Callback<SimpleSuccess> callback) {
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

    public void markUnFavourite(int userID, int productId,String[] optionIDs, Callback<SimpleSuccess> callback) {
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

    public void addAddress(int userID, String addressLine, String addressLine2, String city, String state, String country, boolean isDefault, Callback<AddAddressApi> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("AddressLine1", addressLine);
        parameters.put("AddressLine2", addressLine2);
        parameters.put("City", city);
        parameters.put("State", state);
        parameters.put("Country", country);
        parameters.put("isDefault", isDefault);

        Call<AddAddressApi> call = webServices.insertAddress(parameters);
        call.enqueue(callback);
    }

    public void updateAddress(int ID, String addressLine, String addressLine2, String city, String state, String country, boolean isDefault, Callback<SimpleSuccess> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", ID);
        parameters.put("AddressLine1", addressLine);
        parameters.put("AddressLine2", addressLine2);
        parameters.put("City", city);
        parameters.put("State", state);
        parameters.put("Country", country);
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

    public void updateMessageStatus(int conversationID,Callback<SimpleSuccess> callback) {
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

    public void updateOrderFeedback(int orderID, float rating,  String feedback, Callback<SimpleSuccess> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderID", orderID);
        parameters.put("rating", rating);
        parameters.put("feedback", feedback);
        Call<SimpleSuccess> call = webServices.updateOrderFeedback(parameters);
        call.enqueue(callback);
    }

}
