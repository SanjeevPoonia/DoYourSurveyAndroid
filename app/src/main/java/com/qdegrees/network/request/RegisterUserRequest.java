package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUserRequest {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("remember")
    @Expose
    private String remember;
    @SerializedName("refferal")
    @Expose
    private String refferal;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("googleId")
    @Expose
    private String googleId;
    @SerializedName("facebookUserID")
    @Expose
    private String facebookUserID;
    @SerializedName("password")
    @Expose
    private String password;

    public RegisterUserRequest(String email, String mobile, String firstName, String lastName, String fullName, String gender, String dob, String city, String remember, String refferal, String profileImage, String googleId, String facebookUserID, String password) {
        this.email = email;
        this.mobile = mobile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.city = city;
        this.remember = remember;
        this.refferal = refferal;
        this.profileImage = profileImage;
        this.googleId = googleId;
        this.facebookUserID = facebookUserID;
        this.password = password;
    }
}
