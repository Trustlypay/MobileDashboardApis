package com.trustlypay.DashboardApis.Service;


import com.trustlypay.DashboardApis.Models.MerchantData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MerchantDataService {

    private final JdbcTemplate jdbcTemplate;

    public MerchantDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MerchantData> getPayinMerchantsData() {

        String sql = """
             
                
                SELECT
                        m.name AS merchant_name,
                                m.merchant_gid,
                                (SELECT vb.bank_name
                        FROM vendor_bank vb
                        JOIN live_payment lp2 ON lp2.vendor_id = vb.id
                        WHERE lp2.created_merchant = m.id
                        LIMIT 1) AS vendor_bank,
                        SUM(CASE WHEN lp.transaction_status = 'success' THEN 1 ELSE 0 END) AS success_count,
                        SUM(CASE WHEN lp.transaction_status = 'failed' THEN 1 ELSE 0 END) AS failed_count,
                        SUM(CASE WHEN lp.transaction_status = 'pending' THEN 1 ELSE 0 END) AS pending_count,
                        COALESCE(SUM(CASE WHEN lp.transaction_status = 'success' THEN lp.transaction_amount ELSE 0 END), 0) AS success_amount,
                        COALESCE(SUM(CASE WHEN lp.transaction_status = 'failed' THEN lp.transaction_amount ELSE 0 END), 0) AS failed_amount,
                        COALESCE(SUM(CASE WHEN lp.transaction_status = 'pending' THEN lp.transaction_amount ELSE 0 END), 0) AS pending_amount
                        FROM merchant m
                        LEFT JOIN live_payment lp ON m.id = lp.created_merchant
                        GROUP BY m.id, m.name, m.merchant_gid;
                
                
                
                
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {
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

            MerchantData summary = new MerchantData();
            summary.setMerchantName(rs.getString("merchant_name"));
            summary.setMerchantGid(rs.getString("merchant_gid"));
            summary.setVendorBank(rs.getString("vendor_bank"));
            summary.setSuccessCount(successCount);
            summary.setFailedCount(failedCount);
            summary.setPendingCount(pendingCount);
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


    public List<MerchantData> getPayoutMerchantsData() {
        String sql = """
    SELECT
        m.name AS merchant_name,
        m.merchant_gid,
        SUM(CASE WHEN pt.status = 'success' THEN 1 ELSE 0 END) AS success_count,
        SUM(CASE WHEN pt.status = 'failed' THEN 1 ELSE 0 END) AS failed_count,
        SUM(CASE WHEN pt.status = 'pending' THEN 1 ELSE 0 END) AS pending_count,
        COALESCE(SUM(CASE WHEN pt.status = 'success' THEN pt.amount ELSE 0 END), 0) AS success_amount,
        COALESCE(SUM(CASE WHEN pt.status = 'failed' THEN pt.amount ELSE 0 END), 0) AS failed_amount,
        COALESCE(SUM(CASE WHEN pt.status = 'pending' THEN pt.amount ELSE 0 END), 0) AS pending_amount,
        COALESCE((
            SELECT GROUP_CONCAT(DISTINCT vb.bank_name)
            FROM payout_vendor_bank vb
            JOIN payout_transactions pt2 ON pt2.vendor = vb.id
            WHERE pt2.merchant_id = m.id
        ), '') AS vendor_bank
    FROM merchant m
    LEFT JOIN payout_transactions pt ON m.id = pt.merchant_id
    GROUP BY m.id, m.name, m.merchant_gid;
""";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
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

            MerchantData summary = new MerchantData();
            summary.setMerchantName(rs.getString("merchant_name"));
            summary.setMerchantGid(rs.getString("merchant_gid"));
            summary.setVendorBank(rs.getString("vendor_bank"));
            summary.setSuccessCount(successCount);
            summary.setFailedCount(failedCount);
            summary.setPendingCount(pendingCount);
            summary.setSuccessAmount(successAmount);
            summary.setFailedAmount(failedAmount);
            summary.setPendingAmount(pendingAmount);
            summary.setTotalAmount(totalAmount);
            summary.setSuccessPercentage(successPct);
            summary.setFailedPercentage(failedPct);
            summary.setPendingPercentage(pendingPct);

            return summary;
        });


    }}

