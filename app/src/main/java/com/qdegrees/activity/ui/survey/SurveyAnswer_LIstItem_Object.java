package com.qdegrees.activity.ui.survey;

import com.qdegrees.activity.ui.dashboard.SurveyOptions_ListItem;

import java.util.ArrayList;
import java.util.List;

public class SurveyAnswer_LIstItem_Object {
    String QuestionStr,TextValue;
    List<SurveyOptions_ListItem> OptionsList= new ArrayList<>();
   List<String> AnswerArray;
    String [] AnswerQuestionIdArray;

    public SurveyAnswer_LIstItem_Object(String questionStr, String textValue, List<SurveyOptions_ListItem> optionsList, List<String> answerArray, String[] answerQuestionIdArray) {
        QuestionStr = questionStr;
        TextValue = textValue;
        OptionsList = optionsList;
        AnswerArray = answerArray;
        AnswerQuestionIdArray = answerQuestionIdArray;
    }

    public String getQuestionStr() {
        return QuestionStr;
    }

    public String getTextValue() {
        return TextValue;
    }

    public List<SurveyOptions_ListItem> getOptionsList() {
        return OptionsList;
    }

    public List<String> getAnswerArray() {
        return AnswerArray;
    }

    public String[] getAnswerQuestionIdArray() {
        return AnswerQuestionIdArray;
    }
}
