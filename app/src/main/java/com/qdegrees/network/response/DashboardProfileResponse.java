package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DashboardProfileResponse {

    @SerializedName("code")
    public Integer status;
    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("_id")
        public  String id;
        @SerializedName("user")
        public  String user;
        @SerializedName("data")
        public List<dataUser> dataUsers= new ArrayList<>();

        @SerializedName("profileTaskStatus")
        public List<profileTaskStatus> ProfileTask= new ArrayList<>();

    }
    public class dataUser{
        @SerializedName("_id")
        public  String id;
        @SerializedName("email")
        public  String email;
        @SerializedName("pointEarned")
        public  String pointEarned;
        @SerializedName("pointRedeemed")
        public  String pointRedeemed;
        @SerializedName("pointAvailable")
        public  String pointAvailable;

        @SerializedName("completedSurvey")
        public  String completedSurvey;

        @SerializedName("profilePercent")
        public Integer profilePercent;



    }
    public class profileTaskStatus{
        @SerializedName("_id")
        public  String id;
        @SerializedName("taskName")
        public  String taskName;
        @SerializedName("status")
        public  boolean status;
    }
}
