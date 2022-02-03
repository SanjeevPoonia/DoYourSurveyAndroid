package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferAFriend_Request {
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("refer_name")
    @Expose
    private String refer_name;
    @SerializedName("refer_code")
    @Expose
    private String refer_code;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("refer_email")
    @Expose
    private String refer_email;

    public ReferAFriend_Request(String user_name, String refer_name, String refer_code, String email, String refer_email) {
        this.user_name = user_name;
        this.refer_name = refer_name;
        this.refer_code = refer_code;
        this.email = email;
        this.refer_email = refer_email;
    }
}
