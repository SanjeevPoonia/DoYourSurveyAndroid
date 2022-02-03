package com.qdegrees.utils;

import android.Manifest;

public interface Tags {

    String SUCCESS = "success";
    String BUNDLE = "bundle";
    String DATA = "doYourSurvey";
    String LANG = "language_pref";
    String LOGIN_PREF_Profile = "login_pref_Profile";
    String PREF_FILE = "pref_file";
    String IS_LOGGED_IN = "is_logged_in";
    //key for first time launch
    String IS_FIRST_LAUNCH="IsFirstLaunch";
    String SELECTED_LANGUAGE = "selected_language";
    String[] PERMISSIONS = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String IS_LANGUAGE_SELECTED = "is_language_selected";
    String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    String SAMPLE_CROPPED_IMAGE_NAME = "doYourSurvey";
    String TITLE = "title";
    String URL = "tags";
    String BANK_NAME = "BankName";
    String SESSION_TOKEN = "session_token";
    String lat = "lat";
    String Long  = "long";


    String SELECTED_KNO = "selected_ko";


    String UserId="user_id";
    String EmailStr="email";
    String MobileStr="mobile";
    String FirstName="firstName";
    String LastName="lastName";
    String FullName="fullName";
    String Gender="gender";
    String Dob="dob";
    String City="city";
    String RefferalBy="refferalBy";
    String ReferCode="referCode";
    String ProfileImage="profileImage";
    String GoogleId="googleId";
    String FacebookId="facebookUserID";
    String Date="date";
    String v="v";
    String RememberToken="remember_token";
    String IsDashboardDemoDone="isdashboard_demo";
    String IsRewardDemoDone="isreward_demo";


}
