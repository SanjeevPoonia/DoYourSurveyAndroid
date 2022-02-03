package com.qdegrees.activity.ui.dashboard;

import java.util.ArrayList;
import java.util.List;

public class Survey_Listitem_Object {
    String Id,SurveyName,SurveyId,Points,Status,SurveyType,DateStr;
    List<SurveyQuestions_ListItem> QuestionsList= new ArrayList<>();


    public Survey_Listitem_Object(String id, String surveyName, String surveyId, String points, String status, String surveyType, String dateStr,List<SurveyQuestions_ListItem> questionsList) {
        Id = id;
        SurveyName = surveyName;
        SurveyId = surveyId;
        Points = points;
        Status = status;
        SurveyType = surveyType;
        DateStr = dateStr;
        QuestionsList = questionsList;
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

    public List<SurveyQuestions_ListItem> getQuestionsList() {
        return QuestionsList;
    }
}
