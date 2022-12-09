package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationRequest {
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("page")
    @Expose
    private Integer page;
    public NotificationRequest(String uId, int limt,int pg){
        this.user_id=uId;
        this.limit=limt;
        this.page=pg;
    }

}
