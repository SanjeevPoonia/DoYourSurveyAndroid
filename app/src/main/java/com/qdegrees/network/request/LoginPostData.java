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

    public LoginPostData(String email, String password) {
        this.email = email;
        this.password = password ;
    }
}


