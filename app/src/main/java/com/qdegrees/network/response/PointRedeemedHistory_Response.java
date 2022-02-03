package com.qdegrees.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PointRedeemedHistory_Response {
    @SerializedName("code")
    public Integer status;
    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("totalPage")
        public Integer totalPage;
        @SerializedName("doc")
        public Doc doc;
    }
    public class Doc{
        @SerializedName("user")
        public String totalPage;
        @SerializedName("redeemPoint")
        public List<RedeemPoints> redeemPointsData=new ArrayList<>();
    }
    public class RedeemPoints{
        @SerializedName("_id")
        public String _id;
        @SerializedName("RedeemPoint")
        public String RedeemPoint;
        @SerializedName("voucherFor")
        public String voucherFor;
        @SerializedName("voucherCode")
        public String voucherCode;
        @SerializedName("status")
        public String status;
        @SerializedName("date")
        public String date;
    }
}
