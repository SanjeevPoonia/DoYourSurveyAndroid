package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfilePostData {
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
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("referCode")
    @Expose
    private String referCode;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("facebookUserID")
    @Expose
    private String facebookUserID;
    @SerializedName("googleId")
    @Expose
    private String googleId;


    public UpdateProfilePostData(String em,String mNo,String fName , String lName ,String gen,String dateOB, String cty,String bo,String rfCode,String pImage , String faceId,String gId){
        this.email=em;
        this.mobile=mNo;
        this.firstName=fName;
        this.lastName = lName;
        this.fullName = fName +" "+lName;
        this.gender=gen;
        this.dob=dateOB;
        this.city = cty;
        this.bio = bo;
        this.referCode=rfCode;
        this.profileImage=pImage;
        this.facebookUserID=faceId;
        this.googleId=gId;
    }

}
