package com.qdegrees.activity.ui.rewards;

public class RewardHistoryListItem {
    String ReedemId,ReedemPoint,VoucherFor,VoucharCode,VoucherStatus,DateStr;

    public RewardHistoryListItem(String reedemId, String reedemPoint, String voucherFor, String voucharCode, String voucherStatus, String dateStr) {
        ReedemId = reedemId;
        ReedemPoint = reedemPoint;
        VoucherFor = voucherFor;
        VoucharCode = voucharCode;
        VoucherStatus = voucherStatus;
        DateStr = dateStr;
    }

    public String getReedemId() {
        return ReedemId;
    }

    public String getReedemPoint() {
        return ReedemPoint;
    }

    public String getVoucherFor() {
        return VoucherFor;
    }

    public String getVoucharCode() {
        return VoucharCode;
    }

    public String getVoucherStatus() {
        return VoucherStatus;
    }

    public String getDateStr() {
        return DateStr;
    }
}
