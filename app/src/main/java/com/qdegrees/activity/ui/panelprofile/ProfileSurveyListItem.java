package com.qdegrees.activity.ui.panelprofile;

public class ProfileSurveyListItem {
    String SurveyId,TaskName;
    boolean status;

    public ProfileSurveyListItem(String surveyId, String taskName, boolean status) {
        SurveyId = surveyId;
        TaskName = taskName;
        this.status = status;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public String getTaskName() {
        return TaskName;
    }

    public boolean isStatus() {
        return status;
    }
}
