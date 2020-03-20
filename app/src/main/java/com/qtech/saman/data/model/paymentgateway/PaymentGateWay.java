
package com.qtech.saman.data.model.paymentgateway;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class PaymentGateWay {

    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
