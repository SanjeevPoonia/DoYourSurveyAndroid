package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateSurveyReferCodeRequest {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("survey_id")
    @Expose
    private String survey_id;

    public GenerateSurveyReferCodeRequest(String email, String survey_id) {
        this.email = email;
        this.survey_id = survey_id;
    }
}
