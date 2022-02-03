package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ShareableSurveyResponse {
    @SerializedName("code")
    public Integer status;
    @SerializedName("data")
    public List<Data> data= new ArrayList<>();
    public class Data{
        @SerializedName("_id")
        public  String id;
        @SerializedName("survey_name")
        public  String survey_name;
        @SerializedName("survey_id")
        public  String survey_id;
        @SerializedName("points")
        public  String points;
        @SerializedName("status")
        public  String status;
        @SerializedName("survey_type")
        public  String survey_type;
        @SerializedName("refer_applicable")
        public  boolean refer_applicable;
        @SerializedName("refer_point")
        public  String refer_point;
        @SerializedName("date")
        public  String date;
    }
}
