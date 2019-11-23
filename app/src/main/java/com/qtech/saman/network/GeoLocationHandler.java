package com.qtech.saman.network;

import com.qtech.saman.utils.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoLocationHandler {

public static GeoLocationHandler instance = new GeoLocationHandler();

//    https://maps.googleapis.com/maps/api/geocode/json?latlng=23.5859,58.4059
    private GeoLocationService webServices;

    private GeoLocationHandler() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URLS.GeoCodeApis)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        webServices = retrofit.create(GeoLocationService.class);
    }

    public void getLocation(LatLng latLng,Callback<ResponseBody> callback) {
        String stringLatLng=latLng.latitude+","+latLng.longitude;
        String language="en";
        boolean sensor=false;
        String key="AIzaSyA9vi6vn2yc6Ki3OkRcVlBc74H_6NyN08w";

        Call<ResponseBody> call = webServices.getLocation(stringLatLng,sensor,language,key);
        call.enqueue(callback);
    }
}
