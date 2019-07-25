package com.algorepublic.saman.network;

import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OmanNetServiceHandler {

    public static OmanNetServiceHandler instance = new OmanNetServiceHandler();

    private OmanNetService webServices;

    private OmanNetServiceHandler() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl("https://ethaar.om/Endownment/")
                .baseUrl("https://www.saman.om/api/Order/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        webServices = retrofit.create(OmanNetService.class);
    }

    public void invoice(Callback<ResponseBody> callback) {

        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("fullname", "Fahad Khalid");
//        parameters.put("email", "fahad@algorepublic.com");
//        parameters.put("toemail", "fahad@algorepublic.com");
//        parameters.put("amount", "80");
        parameters.put("orderID", "298");
        parameters.put("CreditCardNumber", "4017518900000300356"); //4017518900000300349  //4017518900000300356
        parameters.put("ExpiryMonth", "03");
        parameters.put("ExpiryYear", "21");
        parameters.put("CardHolderName", "Ahmed");
        parameters.put("CVV", "200");

        Call<ResponseBody> call = webServices.Invoice(parameters);
        call.enqueue(callback);

    }

    public void invoice(int orderID,String creditCardNumber,
                        String expiryMonth,
                        String expiryYear,
                        String cardHolderName,
                        String CVV,
                        Callback<ResponseBody> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderID", orderID);
        parameters.put("CreditCardNumber", creditCardNumber); //4017518900000300349  //4017518900000300356
        parameters.put("ExpiryMonth", expiryMonth);
        parameters.put("ExpiryYear", expiryYear);
        parameters.put("CardHolderName", cardHolderName);
        parameters.put("CVV", CVV);

        Call<ResponseBody> call = webServices.Invoice(parameters);
        call.enqueue(callback);

    }
}
