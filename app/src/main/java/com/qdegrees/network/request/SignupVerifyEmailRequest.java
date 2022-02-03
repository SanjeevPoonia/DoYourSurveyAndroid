package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupVerifyEmailRequest {
    @SerializedName("email")
    @Expose
    private String email;
    public SignupVerifyEmailRequest(String email) {
        this.email = email;
    }
}
