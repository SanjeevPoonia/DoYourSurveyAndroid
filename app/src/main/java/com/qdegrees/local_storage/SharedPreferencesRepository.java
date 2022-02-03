package com.qdegrees.local_storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.qdegrees.doyoursurvey.DoYourSurvey;
import com.qdegrees.utils.Tags;


public class SharedPreferencesRepository implements Tags {

    private static SharedPreferences loginSharedPreferences;
    private static SharedPreferences.Editor loginPrefEditor;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPrefEditor;


    private static SharedPreferencesRepository dataManagerInstance;

    public static SharedPreferencesRepository getDataManagerInstance() {
        if (dataManagerInstance == null) {
            return dataManagerInstance = new SharedPreferencesRepository();
        } else {
            return dataManagerInstance;
        }
    }
    public void clear() {
        loginPrefEditor.clear().commit();
        loginPrefEditor.putBoolean(LOGIN_PREF_Profile, true).commit();
    }
    public SharedPreferencesRepository( ) {
    //static {
        loginSharedPreferences = DoYourSurvey.getApp().getSharedPreferences(LANG, Context.MODE_PRIVATE);
        loginPrefEditor = loginSharedPreferences.edit();
        sharedPreferences = DoYourSurvey.getApp().getSharedPreferences(DATA, Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }

    public void setLanguageSelected(boolean isSelected) {
        loginPrefEditor.putBoolean(IS_LANGUAGE_SELECTED, isSelected).commit();
    }
    public boolean isLanguageSelected() {
        return loginSharedPreferences.getBoolean(IS_LANGUAGE_SELECTED, false);
    }



    public void setIsFirstLaunch(boolean isFirstLaunch) {
        loginPrefEditor.putBoolean(IS_FIRST_LAUNCH, isFirstLaunch).commit();
    }
    public boolean isFirstLaunch() {
        return loginSharedPreferences.getBoolean(IS_FIRST_LAUNCH, false);
    }





    public static boolean isLoggedIn()
    {
        return loginSharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public  void setIsLoggedIn(boolean isFirstTime) {
        loginPrefEditor.putBoolean(IS_LOGGED_IN, isFirstTime).commit();
    }

    public static boolean isUSerName() {
        return loginSharedPreferences.getBoolean(LOGIN_PREF_Profile, false);
    }

    public static void setIsUserName(boolean isProfileComplete) {
        loginPrefEditor.putBoolean(LOGIN_PREF_Profile, isProfileComplete).commit();
    }

    public static void clearLoginPref(boolean isDashboardDemo,boolean isRewardDemo) {
        loginPrefEditor.clear();
        loginPrefEditor.putBoolean(IsDashboardDemoDone,isDashboardDemo);
        loginPrefEditor.putBoolean(IsRewardDemoDone,isRewardDemo);
        loginPrefEditor.commit();
    }


    public static void saveSessionToken(String sessionToken) {
        loginPrefEditor.putString(SESSION_TOKEN, sessionToken).commit();
    }
    public static void saveUserDetails(String userId, String emailStr, String mobileStr, String firstName,
                                       String lastName,String fullName,String gender,
                                       String dob,String city,String refferalBy,String referCode,
                                       String profileImage,String googleId,String facebookId,String date,String V,String rememberToken){
        loginPrefEditor.putString(UserId,userId);
        loginPrefEditor.putString(EmailStr,emailStr);
        loginPrefEditor.putString(MobileStr,mobileStr);
        loginPrefEditor.putString(FirstName,firstName);
        loginPrefEditor.putString(LastName,lastName);
        loginPrefEditor.putString(FullName,fullName);
        loginPrefEditor.putString(Gender,gender);
        loginPrefEditor.putString(Dob,dob);
        loginPrefEditor.putString(City,city);
        loginPrefEditor.putString(RefferalBy,refferalBy);
        loginPrefEditor.putString(ReferCode,referCode);
        loginPrefEditor.putString(ProfileImage,profileImage);
        loginPrefEditor.putString(GoogleId,googleId);
        loginPrefEditor.putString(FacebookId,facebookId);
        loginPrefEditor.putString(Date,date);
        loginPrefEditor.putString(v,V);
        loginPrefEditor.putString(RememberToken,rememberToken);
        loginPrefEditor.commit();


    }
    public static String getSessionUserId() {
        String value = loginSharedPreferences.getString(UserId, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    public static String getSessionname(){
        String value = loginSharedPreferences.getString(FirstName, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    public static String getSessionMobile(){
        String value = loginSharedPreferences.getString(MobileStr, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    public static String getSessionLastname(){
        String value = loginSharedPreferences.getString(LastName, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    public static String getSessionEmail(){
        String value = loginSharedPreferences.getString(EmailStr, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    public static String getSessionReferCode(){
        String value = loginSharedPreferences.getString(ReferCode, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    public static String getSessionRememberToken(){
        String value = loginSharedPreferences.getString(RememberToken, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static boolean IsDashboardDemoRequired(){
        boolean demo=loginSharedPreferences.getBoolean(IsDashboardDemoDone,true);
        return demo;
    }
    public void setDashboardDemoDone(){
        loginPrefEditor.putBoolean(IsDashboardDemoDone,false);
        loginPrefEditor.commit();
    }
    public static boolean IsRewardDemoRequired(){
        boolean demo=loginSharedPreferences.getBoolean(IsRewardDemoDone,true);
        return demo;
    }
    public void setRewardDemoDone(){
        loginPrefEditor.putBoolean(IsRewardDemoDone,false);
        loginPrefEditor.commit();
    }



    public static String getSessionToken() {
        String value = loginSharedPreferences.getString(SESSION_TOKEN, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static String getSelectedKNo() {
        String value = loginSharedPreferences.getString(SELECTED_KNO, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static void setSelectedKNo(String selectedKNo) {
        loginPrefEditor.putString(SELECTED_KNO, selectedKNo).commit();
    }


   /* public void saveUserData(UserDetails user) {
        String data = new Gson().toJson(user, UserDetails.class);
        loginPrefEditor.putString(USER_NAME, data).commit();
    }
    public UserDetails getUser() {
        String userData = loginSharedPreferences.getString(USER_NAME, null);
        return userData != null ? new Gson().fromJson(userData, UserDetails.class) : null;
    }*/


 /*   public  void setBankNameList(List<Bank> bankNameResponses) {
        String data = new Gson().toJson(bankNameResponses);
        sharedPrefEditor.putString(BANK_NAME, data).commit();
    }

    public List<Bank> getBankNameList() {
        String bankNameList = sharedPreferences.getString(BANK_NAME, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<Bank>>(){}.getType();
        ArrayList<Bank> userArray = gson.fromJson(bankNameList, userListType);
        return userArray;
    }*/


    public void savelat(String sessionToken) {
        loginPrefEditor.putString(lat, sessionToken).commit();
    }

    public String getlat() {
        String value = loginSharedPreferences.getString(lat, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    public  void savelong(String sessionToken) {
        loginPrefEditor.putString(Long, sessionToken).commit();
    }

    public String getlong() {
        String value = loginSharedPreferences.getString(Long, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

}
