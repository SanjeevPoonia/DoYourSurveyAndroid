package com.qdegrees.activity.ui.dashboard;

public class Shareable_Survey_ListItem_Object {
    String _ID,SurveyName,SurveyId,Points,Status,SurveyType,ReferPoint,DateStr;
    boolean ReferApplicable;

    public Shareable_Survey_ListItem_Object(String _ID, String surveyName, String surveyId, String points, String status, String surveyType, boolean referApplicable, String referPoint, String dateStr) {
        this._ID = _ID;
        SurveyName = surveyName;
        SurveyId = surveyId;
        Points = points;
        Status = status;
        SurveyType = surveyType;
        ReferApplicable = referApplicable;
        ReferPoint = referPoint;
        DateStr = dateStr;
    }

    public String get_ID() {
        return _ID;
    }

    public String getSurveyName() {
        return SurveyName;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public String getPoints() {
        return Points;
    }

    public String getStatus() {
        return Status;
    }

    public String getSurveyType() {
        return SurveyType;
    }

    public String getReferPoint() {
        return ReferPoint;
    }

    public String getDateStr() {
        return DateStr;
    }

    public boolean isReferApplicable() {
        return ReferApplicable;
    }
}
