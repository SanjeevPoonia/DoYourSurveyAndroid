package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    public Integer status;
    @SerializedName("message")
    public  String message;
    @SerializedName("token")
    public  String token;
    @SerializedName("data")
    public Data data;



    public class Data{
        @SerializedName("_id")
        public  String id;
        @SerializedName("email")
        public  String email;
        @SerializedName("mobile")
        public  String mobile;
        @SerializedName("firstName")
        public  String firstName;
        @SerializedName("lastName")
        public  String lastName;
        @SerializedName("fullName")
        public  String fullName;
        @SerializedName("gender")
        public  String gender;
        @SerializedName("dob")
        public  String dob;
        @SerializedName("city")
        public  String city;
        @SerializedName("refferalBy")
        public  String refferalBy;
        @SerializedName("referCode")
        public  String referCode;
        @SerializedName("profileImage")
        public  String profileImage;
        @SerializedName("googleId")
        public  String googleId;
        @SerializedName("facebookUserID")
        public  String facebookUserID;
        @SerializedName("password")
        public  String password;
        @SerializedName("date")
        public  String date;
        @SerializedName("__v")
        public  String v;
        @SerializedName("bio")
        public  String bio;
    }
}
