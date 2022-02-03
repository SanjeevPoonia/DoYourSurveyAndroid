package com.qdegrees.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReedemPointResponse {
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("code")
    @Expose
    public Integer status;
}
