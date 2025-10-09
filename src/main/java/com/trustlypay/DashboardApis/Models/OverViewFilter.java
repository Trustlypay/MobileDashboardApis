package com.trustlypay.DashboardApis.Models;

public class OverViewFilter {

    private String fromDate;
    private String toDate;
    private String merchantId; // optional

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public OverViewFilter(String fromDate, String toDate, String merchantId) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.merchantId = merchantId;
    }

    public OverViewFilter() {
    }
}
