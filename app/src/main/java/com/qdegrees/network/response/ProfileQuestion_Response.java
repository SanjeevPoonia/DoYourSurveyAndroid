package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProfileQuestion_Response {
    @SerializedName("code")
    public Integer status;
    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("_id")
        public  String id;
        @SerializedName("survey_name")
        public  String survey_name;
        @SerializedName("survey_id")
        public  String survey_id;
        @SerializedName("points")
        public  String points;
        @SerializedName("questions")
        public List<Questions> Questions= new ArrayList<>();
    }
    public class Questions{
        @SerializedName("_id")
        public  String id;
        @SerializedName("question")
        public  String question;

        @SerializedName("selection")
        public  String selection;
        @SerializedName("type")
        public  String type;
        @SerializedName("status")
        public  String status;
        @SerializedName("condition")
        public  String condition;
        @SerializedName("dynamicSelection")
        public  String dynamicSelection;

        @SerializedName("options")
        public List<Options> Options= new ArrayList<>();

    }
    public class Options{
        @SerializedName("option")
        public  String option;
        @SerializedName("action_id")
        public  String action_id;
        @SerializedName("text_box")
        public  String text_box;
    }
}

