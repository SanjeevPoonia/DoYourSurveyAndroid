package com.qdegrees.activity.ui.helpcenter;

public class HelpCenterListItem {
    String MainItem,SubItem;

    public HelpCenterListItem(String mainItem, String subItem) {
        MainItem = mainItem;
        SubItem = subItem;
    }

    public String getMainItem() {
        return MainItem;
    }

    public String getSubItem() {
        return SubItem;
    }
}
