package com.trustlypay.DashboardApis.Models;

import java.math.BigDecimal;

public class VendorData {
    private int vendorId;
    private String vendorName;

    private long successCount;
    private long failedCount;
    private long pendingCount;
    private long totalCount;

    private BigDecimal successAmount;
    private BigDecimal failedAmount;
    private BigDecimal pendingAmount;
    private BigDecimal totalAmount;

    private double successPercentage;
    private double failedPercentage;
    private double pendingPercentage;

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(long failedCount) {
        this.failedCount = failedCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(BigDecimal successAmount) {
        this.successAmount = successAmount;
    }

    public BigDecimal getFailedAmount() {
        return failedAmount;
    }

    public void setFailedAmount(BigDecimal failedAmount) {
        this.failedAmount = failedAmount;
    }

    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getSuccessPercentage() {
        return successPercentage;
    }

    public void setSuccessPercentage(double successPercentage) {
        this.successPercentage = successPercentage;
    }

    public double getFailedPercentage() {
        return failedPercentage;
    }

    public void setFailedPercentage(double failedPercentage) {
        this.failedPercentage = failedPercentage;
    }

    public double getPendingPercentage() {
        return pendingPercentage;
    }

    public void setPendingPercentage(double pendingPercentage) {
        this.pendingPercentage = pendingPercentage;
    }

    public VendorData() {
    }

    public VendorData(int vendorId, String vendorName, long successCount, long failedCount, long pendingCount, long totalCount, BigDecimal successAmount, BigDecimal failedAmount, BigDecimal pendingAmount, BigDecimal totalAmount, double successPercentage, double failedPercentage, double pendingPercentage) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
        this.totalCount = totalCount;
        this.successAmount = successAmount;
        this.failedAmount = failedAmount;
        this.pendingAmount = pendingAmount;
        this.totalAmount = totalAmount;
        this.successPercentage = successPercentage;
        this.failedPercentage = failedPercentage;
        this.pendingPercentage = pendingPercentage;
    }
}
