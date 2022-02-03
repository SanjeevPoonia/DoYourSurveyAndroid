package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileQuestion_Request {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("type")
    @Expose
    private String type;

    public ProfileQuestion_Request(String email, String type) {
        this.email = email;
        this.type = type;
    }
}
