package com.qtech.saman.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoLocationService {

//    latlng=23.5859,58.4059&sensor=false&language=fr&key=AIzaSyA9vi6vn2yc6Ki3OkRcVlBc74H_6NyN08w
    @GET("json?")
    Call<ResponseBody> getLocation(@Query("latlng") String latlng, @Query("sensor") boolean sensor, @Query("language") String language, @Query("key") String key);

}
