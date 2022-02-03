package com.qdegrees.activity.ui.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyQuestions_ListItem implements Serializable {
    String IdStr,QuestionStr,QuestionMessage,SelectionStr,TypeStr,SatusStr,ConditionStr,DynamicSelection,Profiling;
    List<SurveyOptions_ListItem> OptionsList= new ArrayList<>();
    String AnswerRadio="";
    List<String> CheckBoxAnswer=new ArrayList<>();
    String MinimumSelection="" ;
    String MaximumSelection="";
    List<String> RequiredOneOfThem= new ArrayList<>();
    boolean IsResponseCountApplicable;
    boolean ShowHideApplicable;
    String ShowHideAppliTargetId;


    public SurveyQuestions_ListItem(String idStr, String questionStr, String questionMessage, String selectionStr, String typeStr, String satusStr, String conditionStr, List<SurveyOptions_ListItem> optionsList,String dynamicSelection,String minimumSelection,String maximumSelection,List<String>requiredOneOfThem,String profiling,boolean isResponseCountApplicable,boolean showHideApplicable,String showHideAppliTargetId) {
        IdStr = idStr;
        QuestionStr = questionStr;
        QuestionMessage = questionMessage;
        SelectionStr = selectionStr;
        TypeStr = typeStr;
        SatusStr = satusStr;
        ConditionStr = conditionStr;
        OptionsList = optionsList;
        DynamicSelection=dynamicSelection;
        MinimumSelection=minimumSelection;
        MaximumSelection=maximumSelection;
        RequiredOneOfThem=requiredOneOfThem;
        Profiling=profiling;

        IsResponseCountApplicable=isResponseCountApplicable;
        ShowHideApplicable=showHideApplicable;
        ShowHideAppliTargetId=showHideAppliTargetId;
    }

    public String getIdStr() {
        return IdStr;
    }

    public String getQuestionStr() {
        return QuestionStr;
    }

    public String getQuestionMessage() {
        return QuestionMessage;
    }

    public String getSelectionStr() {
        return SelectionStr;
    }

    public String getTypeStr() {
        return TypeStr;
    }

    public String getSatusStr() {
        return SatusStr;
    }

    public String getConditionStr() {
        return ConditionStr;
    }

    public List<SurveyOptions_ListItem> getOptionsList() {
        return OptionsList;
    }

    public void setAnswerRadio(String answerRadio) {
        AnswerRadio = answerRadio;
    }

    public String getAnswerRadio() {
        return AnswerRadio;
    }
    public void setCheckBoxAnswer(List<String> CheckBoxAns){
        CheckBoxAnswer=CheckBoxAns;
    }
    public List<String> getCheckBoxAnswer(){return CheckBoxAnswer;}

    public String getDynamicSelection(){return DynamicSelection;}
    public String getMinimumSelection(){return MinimumSelection;}
    public String getMaximumSelection(){return MaximumSelection;}
    public List<String> getRequiredOneOfThem(){return RequiredOneOfThem;}
    public String getProfiling(){return Profiling;}

    public boolean isResponseCountApplicable() {
        return IsResponseCountApplicable;
    }

    public boolean isShowHideApplicable() {
        return ShowHideApplicable;
    }

    public String getShowHideAppliTargetId() {
        return ShowHideAppliTargetId;
    }
}
