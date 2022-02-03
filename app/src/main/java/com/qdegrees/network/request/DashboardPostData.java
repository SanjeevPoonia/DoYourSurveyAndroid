package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardPostData {
    @SerializedName("email")
    @Expose
    private String email;
    public DashboardPostData(String email) {
        this.email = email;
    }
}
