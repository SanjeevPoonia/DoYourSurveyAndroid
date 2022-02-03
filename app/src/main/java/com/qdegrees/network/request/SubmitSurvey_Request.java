package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitSurvey_Request {
    @SerializedName("compltedSurveyNumber")
    @Expose
    private String compltedSurveyNumber;
    @SerializedName("pointAdd")
    @Expose
    private String pointAdd;
    @SerializedName("surveyName")
    @Expose
    private String surveyName;
    @SerializedName("surveyId")
    @Expose
    private String surveyId;
    @SerializedName("surveyType")
    @Expose
    private String surveyType;
    @SerializedName("surveyStatus")
    @Expose
    private String surveyStatus;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("questions")
    @Expose
    private String questions;

    public SubmitSurvey_Request(String compltedSurveyNumber, String pointAdd, String surveyName, String surveyId, String surveyType, String surveyStatus, String email, String questions) {
        this.compltedSurveyNumber = compltedSurveyNumber;
        this.pointAdd = pointAdd;
        this.surveyName = surveyName;
        this.surveyId = surveyId;
        this.surveyType = surveyType;
        this.surveyStatus = surveyStatus;
        this.email = email;
        this.questions = questions;
    }
}
