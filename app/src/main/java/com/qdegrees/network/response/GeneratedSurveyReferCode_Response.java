package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GeneratedSurveyReferCode_Response {
    @SerializedName("code")
    public Integer status;
    @SerializedName("message")
    public  String message;
    @SerializedName("data")
    public List<Data> DataList= new ArrayList<>();

    public class Data{
        @SerializedName("_id")
        public  String ID;
        @SerializedName("email")
        public  String email;
        @SerializedName("data")
        public List<ReferalData> referalData= new ArrayList<>();

    }

    public class ReferalData{
        @SerializedName("_id")
        public  String ID;
        @SerializedName("survey_id")
        public  String survey_id;
        @SerializedName("referedSurveyGeneratedCode")
        public  String referedSurveyGeneratedCode;
    }
}
