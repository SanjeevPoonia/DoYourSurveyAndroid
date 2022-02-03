package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

public class SignupVerifyEmailReqResponse {
    @SerializedName("code")
    public Integer status;
    @SerializedName("message")
    public  String message;
    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("_id")
        public  String id;
        @SerializedName("email")
        public  String email;
        @SerializedName("__v")
        public  String __v;
        @SerializedName("expire_in")
        public  String expire_in;
        @SerializedName("otp")
        public  String otp;

    }
}
