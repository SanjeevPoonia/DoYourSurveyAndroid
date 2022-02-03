package com.qdegrees.activity.ui.dashboard;

import com.qdegrees.network.ResponseHeaderInterceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyOptions_ListItem implements Serializable {
    String OptionStr,ActionId,TextBox;
    int ResponseLimit;
    List<SurveyOptions_HideShow_LIst> ShowHideList= new ArrayList<>();
    boolean IsShowThisOption;

    List<String> Coloumns= new ArrayList<>();

    public SurveyOptions_ListItem(String optionStr, String actionId, String textBox,int responseLimit,List<SurveyOptions_HideShow_LIst>showHideList,boolean isShowThisOption,List<String> coloumns) {
        OptionStr = optionStr;
        ActionId = actionId;
        TextBox = textBox;
        ResponseLimit=responseLimit;
        ShowHideList=showHideList;
        IsShowThisOption=isShowThisOption;
        Coloumns=coloumns;
    }

    public String getOptionStr() {
        return OptionStr;
    }

    public String getActionId() {
        return ActionId;
    }

    public String getTextBox() {
        return TextBox;
    }

    public int getResponseLimit(){return ResponseLimit;}

    public List<SurveyOptions_HideShow_LIst> getShowHideList() {
        return ShowHideList;
    }

    public boolean isShowThisOption() {
        return IsShowThisOption;
    }

    public void setShowThisOption(boolean showThisOption) {
        IsShowThisOption = showThisOption;
    }

    public List<String> getColoumns(){return Coloumns;}

}
