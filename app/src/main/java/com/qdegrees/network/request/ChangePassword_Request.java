package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassword_Request {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    @SerializedName("confirmPassword")
    @Expose
    private String confirmPassword;

    public ChangePassword_Request(String email,String password, String newPassword, String confirmPassword) {
        this.password = password;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.email=email;
    }
}
