package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationResponse {
    @SerializedName("code")
    public Integer status;
    @SerializedName("message")
    public  String message;
    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("notifications")
        public List<dataNotifications> dataUsers= new ArrayList<>();
    }
    public class dataNotifications{
        @SerializedName("user_id")
        public  String user_id;
        @SerializedName("read_status")
        public  Integer read_status;
        @SerializedName("status")
        public  Integer status;
        @SerializedName("_id")
        public  String _id;
        @SerializedName("title")
        public  String title;
        @SerializedName("message")
        public  String message;
        @SerializedName("type")
        public  String type;
        @SerializedName("created_at")
        public  String created_at;
        @SerializedName("updated_at")
        public  String updated_at;
    }
}
