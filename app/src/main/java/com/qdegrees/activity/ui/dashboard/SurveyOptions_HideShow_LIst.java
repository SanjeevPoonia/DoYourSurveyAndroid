package com.qdegrees.activity.ui.dashboard;

import java.io.Serializable;

public class SurveyOptions_HideShow_LIst implements Serializable {
    String ProductName;
    boolean ProductValue;

    public SurveyOptions_HideShow_LIst(String productName, boolean productValue) {
        ProductName = productName;
        ProductValue = productValue;
    }

    public String getProductName() {
        return ProductName;
    }

    public boolean isProductValue() {
        return ProductValue;
    }
}
