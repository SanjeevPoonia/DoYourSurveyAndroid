package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

public class ProfileUploadResponse {
    @SerializedName("code")
    public Integer status;
    @SerializedName("message")
    public  String message;
    @SerializedName("data")
    public String data;
}
