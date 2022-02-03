package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLogin_Request {
    @SerializedName("registredId")
    @Expose
    private String registredId;

    public SocialLogin_Request(String registredId) {
        this.registredId = registredId;
    }
}
