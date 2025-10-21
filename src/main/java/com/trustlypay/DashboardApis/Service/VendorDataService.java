package com.trustlypay.DashboardApis.Service;

import com.trustlypay.DashboardApis.Models.MerchantData;
import com.trustlypay.DashboardApis.Models.VendorData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class VendorDataService {
    private final JdbcTemplate jdbcTemplate;

    public VendorDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VendorData> getPayinVendorsData(String fromDate, String toDate) {
        String sql = """
        SELECT
            vb.id AS vendor_id,
            vb.bank_name AS vendor_name,
            SUM(CASE WHEN t.transaction_status = 'success' THEN 1 ELSE 0 END) AS success_count,
            SUM(CASE WHEN t.transaction_status = 'failed' THEN 1 ELSE 0 END) AS failed_count,
            SUM(CASE WHEN t.transaction_status = 'pending' THEN 1 ELSE 0 END) AS pending_count,
            COALESCE(SUM(CASE WHEN t.transaction_status = 'success' THEN t.transaction_amount ELSE 0 END), 0) AS success_amount,
            COALESCE(SUM(CASE WHEN t.transaction_status = 'failed' THEN t.transaction_amount ELSE 0 END), 0) AS failed_amount,
            COALESCE(SUM(CASE WHEN t.transaction_status = 'pending' THEN t.transaction_amount ELSE 0 END), 0) AS pending_amount
        FROM vendor_bank vb
        LEFT JOIN (
            SELECT lp.vendor_id, lp.transaction_status, lp.transaction_amount
            FROM live_payment lp
            WHERE lp.created_date BETWEEN ? AND ?
              AND lp.vendor_id IS NOT NULL AND lp.vendor_id != 0
            UNION ALL
            SELECT lpb.vendor_id, lpb.transaction_status, lpb.transaction_amount
            FROM live_payment_bkp lpb
            WHERE lpb.created_date BETWEEN ? AND ?
              AND lpb.vendor_id IS NOT NULL AND lpb.vendor_id != 0
        ) t ON vb.id = t.vendor_id
        GROUP BY vb.id, vb.bank_name
        ORDER BY vb.bank_name ASC;
    """;

        return jdbcTemplate.query(sql, new Object[]{fromDate, toDate, fromDate, toDate}, (rs, rowNum) -> {
            long successCount = rs.getLong("success_count");
            long failedCount = rs.getLong("failed_count");
            long pendingCount = rs.getLong("pending_count");

            BigDecimal successAmount = rs.getBigDecimal("success_amount");
            BigDecimal failedAmount = rs.getBigDecimal("failed_amount");
            BigDecimal pendingAmount = rs.getBigDecimal("pending_amount");

            long totalCount = successCount + failedCount + pendingCount;
            BigDecimal totalAmount = successAmount.add(failedAmount).add(pendingAmount);

//            double successPct = totalCount > 0 ? (double) successCount / totalCount * 100 : 0;
//            double failedPct = totalCount > 0 ? (double) failedCount / totalCount * 100 : 0;
//            double pendingPct = totalCount > 0 ? (double) pendingCount / totalCount * 100 : 0;

            double successPct = totalCount > 0 ? (double) successCount / totalCount : 0;
            double failedPct = totalCount > 0 ? (double) failedCount / totalCount : 0;
            double pendingPct = totalCount > 0 ? (double) pendingCount / totalCount : 0;

            VendorData vendorSummary = new VendorData();
            vendorSummary.setVendorId(rs.getInt("vendor_id"));
            vendorSummary.setVendorName(rs.getString("vendor_name"));
            vendorSummary.setSuccessCount(successCount);
            vendorSummary.setFailedCount(failedCount);
            vendorSummary.setPendingCount(pendingCount);
            vendorSummary.setTotalCount(totalCount);
            vendorSummary.setSuccessAmount(successAmount);
            vendorSummary.setFailedAmount(failedAmount);
            vendorSummary.setPendingAmount(pendingAmount);
            vendorSummary.setTotalAmount(totalAmount);
            vendorSummary.setSuccessPercentage(successPct);
            vendorSummary.setFailedPercentage(failedPct);
            vendorSummary.setPendingPercentage(pendingPct);

            return vendorSummary;
        });
    }


    public List<VendorData> getPayoutVendorsData(String fromDate, String toDate) {
        String sql = """
        SELECT
            vb.id AS vendor_id,
            vb.bank_name AS vendor_name,
            SUM(CASE WHEN pt.status = 'success' THEN 1 ELSE 0 END) AS success_count,
            SUM(CASE WHEN pt.status = 'failed' THEN 1 ELSE 0 END) AS failed_count,
            SUM(CASE WHEN pt.status = 'pending' THEN 1 ELSE 0 END) AS pending_count,
            COALESCE(SUM(CASE WHEN pt.status = 'success' THEN pt.amount ELSE 0 END), 0) AS success_amount,
            COALESCE(SUM(CASE WHEN pt.status = 'failed' THEN pt.amount ELSE 0 END), 0) AS failed_amount,
            COALESCE(SUM(CASE WHEN pt.status = 'pending' THEN pt.amount ELSE 0 END), 0) AS pending_amount
        FROM payout_vendor_bank vb
        LEFT JOIN (
            SELECT vendor, status, amount
            FROM payout_transactions
            WHERE created_at BETWEEN ? AND ?
              AND vendor IS NOT NULL AND vendor != 0
            UNION ALL
            SELECT vendor, status, amount
            FROM payout_transactions_bkp
            WHERE created_at BETWEEN ? AND ?
              AND vendor IS NOT NULL AND vendor != 0
        ) pt ON vb.id = pt.vendor
        GROUP BY vb.id, vb.bank_name
        ORDER BY vb.bank_name ASC;
    """;

        return jdbcTemplate.query(sql, new Object[]{fromDate, toDate, fromDate, toDate}, (rs, rowNum) -> {
            long successCount = rs.getLong("success_count");
            long failedCount = rs.getLong("failed_count");
            long pendingCount = rs.getLong("pending_count");

            BigDecimal successAmount = rs.getBigDecimal("success_amount");
            BigDecimal failedAmount = rs.getBigDecimal("failed_amount");
            BigDecimal pendingAmount = rs.getBigDecimal("pending_amount");

            long totalCount = successCount + failedCount + pendingCount;
            BigDecimal totalAmount = successAmount.add(failedAmount).add(pendingAmount);

            double successPct = totalCount > 0 ? (double) successCount / totalCount : 0;
            double failedPct = totalCount > 0 ? (double) failedCount / totalCount : 0;
            double pendingPct = totalCount > 0 ? (double) pendingCount / totalCount : 0;

            VendorData summary = new VendorData();
            summary.setVendorId(rs.getInt("vendor_id"));
            summary.setVendorName(rs.getString("vendor_name"));
            summary.setSuccessCount(successCount);
            summary.setFailedCount(failedCount);
            summary.setPendingCount(pendingCount);
            summary.setTotalCount(totalCount);
            summary.setSuccessAmount(successAmount);
            summary.setFailedAmount(failedAmount);
            summary.setPendingAmount(pendingAmount);
            summary.setTotalAmount(totalAmount);
            summary.setSuccessPercentage(successPct);
            summary.setFailedPercentage(failedPct);
            summary.setPendingPercentage(pendingPct);

            return summary;
        });
    }


}
