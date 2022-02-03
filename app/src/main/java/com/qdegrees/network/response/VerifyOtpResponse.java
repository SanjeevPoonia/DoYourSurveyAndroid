package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

public class VerifyOtpResponse {
    @SerializedName("code")
    public Integer status;
    @SerializedName("message")
    public  String message;
}
