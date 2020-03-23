package com.qtech.saman.network;

import com.qtech.saman.data.model.paymentgateway.PaymentGateWay;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OmanNetService {

    @FormUrlEncoded
    @POST("Invoice")
    Call<PaymentGateWay> Invoice(@FieldMap Map<String, Object> parameters);

}
