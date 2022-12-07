package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPostData {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("userType")
    @Expose
    private String userType;

    public LoginPostData(String email, String password,String userType) {
        this.email = email;
        this.password = password ;
        this.userType=userType;

    }
}


