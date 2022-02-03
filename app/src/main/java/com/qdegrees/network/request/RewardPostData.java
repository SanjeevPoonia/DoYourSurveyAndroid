package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardPostData {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("page")
    @Expose
    private Integer page;
    public RewardPostData(String email,int page) {
        this.email = email;
        this.page = page;

    }
}
