package com.qdegrees.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileSubmit_Request {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attempQuestionTask")
    @Expose
    private String attempQuestionTask;
    @SerializedName("newPercentage")
    @Expose
    private String newPercentage;
    @SerializedName("profileCompletionPoint")
    @Expose
    private String profileCompletionPoint;

    public ProfileSubmit_Request(String email, String type, String attempQuestionTask, String newPercentage, String profileCompletionPoint) {
        this.email = email;
        this.type = type;
        this.attempQuestionTask = attempQuestionTask;
        this.newPercentage = newPercentage;
        this.profileCompletionPoint = profileCompletionPoint;
    }
}
