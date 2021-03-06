package com.qtech.saman.network;

import com.qtech.saman.data.model.paymentgateway.PaymentGateWay;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OmanNetServiceHandler {

    public static OmanNetServiceHandler instance = new OmanNetServiceHandler();

    private OmanNetService webServices;

    private OmanNetServiceHandler() {
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
                .baseUrl("https://staging.saman.om/api/Order/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        webServices = retrofit.create(OmanNetService.class);
    }

    public void invoice(Callback<PaymentGateWay> callback) {

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

        Call<PaymentGateWay> call = webServices.Invoice(parameters);
        call.enqueue(callback);

    }

    public void invoice(int orderID, String creditCardNumber,
                        String expiryMonth,
                        String expiryYear,
                        String cardHolderName,
                        String CVV,
                        Callback<PaymentGateWay> callback) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderID", orderID);
        parameters.put("CreditCardNumber", creditCardNumber); //4017518900000300349  //4017518900000300356
        parameters.put("ExpiryMonth", expiryMonth);
        parameters.put("ExpiryYear", expiryYear);
        parameters.put("CardHolderName", cardHolderName);
        parameters.put("CVV", CVV);

        Call<PaymentGateWay> call = webServices.Invoice(parameters);
        call.enqueue(callback);

    }
}
