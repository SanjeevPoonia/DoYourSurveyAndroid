package com.qdegrees.activity.ui.dashboard;

import java.util.ArrayList;
import java.util.List;

class Dashboard_Survey_ListItem_Object {
    String Id,SurveyName,SurveyId,Points,Status,SurveyType,DateStr,ReferPointsStr,AccessSurveyCxmStr;
    boolean ReferApplicable;
    int IsShareable,IsAttemptable;

    List<SurveyQuestions_ListItem> QuestionsList= new ArrayList<>();
    public Dashboard_Survey_ListItem_Object(
            String id, String surveyName, String surveyId,
            String points, String status, String surveyType,
            String dateStr, String referPointsStr,String accessSurveyCxmStr,
            boolean referApplicable, int isShareable,int isAttemptable,
            List<SurveyQuestions_ListItem> questionsList) {
        Id = id;
        SurveyName = surveyName;
        SurveyId = surveyId;
        Points = points;
        Status = status;
        SurveyType = surveyType;
        DateStr = dateStr;
        QuestionsList = questionsList;
        ReferPointsStr=referPointsStr;
        AccessSurveyCxmStr=accessSurveyCxmStr;
        ReferApplicable=referApplicable;
        IsShareable=isShareable;
        IsAttemptable=isAttemptable;

    }



    public String getId() {
        return Id;
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

    public String getDateStr() {
        return DateStr;
    }
    public String getReferPointsStr(){return ReferPointsStr;}
    public String getAccessSurveyCxmStr(){return AccessSurveyCxmStr;}
    public Boolean getReferApplicable(){return ReferApplicable;}
    public int getIsShareable(){return IsShareable;}
    public int getIsAttemptable(){return IsAttemptable;}


    public List<SurveyQuestions_ListItem> getQuestionsList() {
        return QuestionsList;
    }



}
