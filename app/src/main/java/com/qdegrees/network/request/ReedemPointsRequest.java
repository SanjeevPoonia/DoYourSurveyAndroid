package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReedemPointsRequest {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("RedeemPoint")
    @Expose
    private String RedeemPoint;
    @SerializedName("paytmMobile")
    @Expose
    private String paytmMobile;
    @SerializedName("voucherFor")
    @Expose
    private String voucherFor;
    @SerializedName("voucherCode")
    @Expose
    private String voucherCode;
    @SerializedName("status")
    @Expose
    private String status;

    public ReedemPointsRequest(String email, String redeemPoint,String paytmMobile, String voucherFor, String voucherCode, String status) {
        this.email = email;
        RedeemPoint = redeemPoint;
        this.voucherFor = voucherFor;
        this.voucherCode = voucherCode;
        this.status = status;
        this.paytmMobile=paytmMobile;
    }
}
