package com.algorepublic.saman.network;

import com.algorepublic.saman.data.model.apis.AddAddressApi;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OmanNetService {

    @FormUrlEncoded
    @POST("Invoice")
    Call<ResponseBody> Invoice(@FieldMap Map<String, Object> parameters);

}
