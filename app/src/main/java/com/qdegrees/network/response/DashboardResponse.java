package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DashboardResponse {
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
        @SerializedName("date")
        public  String date;
        @SerializedName("questions")
        public List<Questions> Questions= new ArrayList<>();
    }
    public class Questions{
        @SerializedName("_id")
        public  String id;
        @SerializedName("question")
        public  String question;
        @SerializedName("que_message")
        public  String que_message;
        @SerializedName("selection")
        public  String selection;
        @SerializedName("type")
        public  String type;
        @SerializedName("status")
        public  String status;
        @SerializedName("condition")
        public  String condition;
        @SerializedName("profiling")
        public  String profiling;
        @SerializedName("dynamicSelection")
        public  String dynamicSelection;
        @SerializedName("min_selection")
        public String min_selection;
        @SerializedName("max_selection")
        public String max_selection;
        @SerializedName("is_response_count_applicable")
        public boolean is_response_count_applicable;
        @SerializedName("show_hide_applicable")
        public boolean show_hide_applicable;
        @SerializedName("show_hide_applicable_target_id")
        public String show_hide_applicable_target_id;



        @SerializedName("requiredAnyOneFrom")
        public List<String> requiredAnyOneFrom=new ArrayList<>();
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
        @SerializedName("response_limit")
        public  int response_limit;
        @SerializedName("show_hide_option_android")
        public List<ShowHideOption> showHideOptions= new ArrayList<>();
        @SerializedName("columns")
        public List<String>coloumns= new ArrayList<>();
        @SerializedName("sub_columns")
        public List<String>sub_columns= new ArrayList<>();
    }

    public class ShowHideOption{
        @SerializedName("product_name")
        public  String product_name;
        @SerializedName("product_value")
        public  boolean product_value;

    }
}
