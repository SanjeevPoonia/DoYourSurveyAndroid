package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpRequest {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("otp")
    @Expose
    private String otp;
    public VerifyOtpRequest(String email, String otp) {
        this.email = email;
        this.otp=otp;

    }
}
