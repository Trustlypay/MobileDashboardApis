package com.trustlypay.DashboardApis.Service;

import com.trustlypay.DashboardApis.Models.OverViewFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DasboardSummaryService {

    private final JdbcTemplate jdbcTemplate;

    public DasboardSummaryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getPayinDashboardSummary(String fromDate, String toDate, String merchantId) {


        // SQL: Combine live_payment and live_payment_bkp
        String sql = "SELECT transaction_status, SUM(transaction_amount) AS amount, COUNT(*) AS total " +
                "FROM ( " +
                "    SELECT transaction_status, transaction_amount, created_merchant, created_date FROM live_payment " +
                "    UNION ALL " +
                "    SELECT transaction_status, transaction_amount, created_merchant, created_date FROM live_payment_bkp " +
                ") AS combined " +
                "WHERE created_date BETWEEN ? AND ? ";

        List<Object> params = new ArrayList<>();
        params.add(fromDate);
        params.add(toDate);

        // Optional merchant filter
        if (merchantId != null && !merchantId.isEmpty()) {
            sql += " AND created_merchant = ? ";
            params.add(merchantId);
        }

        sql += " GROUP BY transaction_status";

        // Execute query
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params.toArray());



        // Initialize status maps
        Map<String, Object> success = new HashMap<>(Map.of(
                "status", "success",
                "totalAmount", 0,
                "totalCount", 0,
                "percentage", 0
        ));
        Map<String, Object> failed = new HashMap<>(Map.of(
                "status", "failed",
                "totalAmount", 0,
                "totalCount", 0,
                "percentage", 0
        ));
        Map<String, Object> pending = new HashMap<>(Map.of(
                "status", "pending",
                "totalAmount", 0,
                "totalCount", 0,
                "percentage", 0
        ));

        double totalAmount = 0;
        int totalCount = 0;

        // Populate data from query result
        for (Map<String, Object> row : result) {
            String status = row.get("transaction_status") != null
                    ? row.get("transaction_status").toString().toLowerCase()
                    : "";
            double amount = ((Number) row.get("amount")).doubleValue();
            int count = ((Number) row.get("total")).intValue();

            if (status.equals("success")) {
                success.put("totalAmount", amount);
                success.put("totalCount", count);
            } else if (status.equals("failed")) {
                failed.put("totalAmount", amount);
                failed.put("totalCount", count);
            } else if (status.equals("pending")) {
                pending.put("totalAmount", amount);
                pending.put("totalCount", count);
            }

            totalAmount += amount;
            totalCount += count;
        }


        if (totalCount > 0) {
            success.put("percentage", Math.round(((int) success.get("totalCount") / (double) totalCount) * 100) / 100.0);
            failed.put("percentage", Math.round(((int) failed.get("totalCount") / (double) totalCount) * 100) / 100.0);
            pending.put("percentage", Math.round(((int) pending.get("totalCount") / (double) totalCount) * 100) / 100.0);
        }

        Map<String, Object> all = new HashMap<>(Map.of(

                "totalAmount", totalAmount,
                "totalCount", totalCount
        ));

        return List.of(failed, success, pending, all);
    }

    public List<Map<String, Object>> getDashboardPayoutTransactionSummary(String fromDate, String toDate, Integer merchantId) {

        String sql = "SELECT status, SUM(CAST(amount AS DECIMAL(18,2))) AS amount, COUNT(*) AS total " +
                "FROM ( " +
                "    SELECT status, amount, merchant_id, created_at FROM payout_transactions " +
                "    UNION ALL " +
                "    SELECT status, amount, merchant_id, created_at FROM payout_transactions_bkp " +
                ") AS combined " +
                "WHERE created_at BETWEEN ? AND ? ";

        List<Object> params = new ArrayList<>();
        params.add(fromDate);
        params.add(toDate);

        // Optional merchant filter
        if (merchantId != null) {
            sql += " AND merchant_id = ? ";
            params.add(merchantId);
        }

        sql += " GROUP BY status";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params.toArray());

        Map<String, Object> success = new HashMap<>();
        success.put("status", "success");
        success.put("totalAmount", 0);
        success.put("totalCount", 0);
        success.put("percentage", 0);

        Map<String, Object> failed = new HashMap<>();
        failed.put("status", "failed");
        failed.put("totalAmount", 0);
        failed.put("totalCount", 0);
        failed.put("percentage", 0);

        Map<String, Object> pending = new HashMap<>();
        pending.put("status", "pending");
        pending.put("totalAmount", 0);
        pending.put("totalCount", 0);
        pending.put("percentage", 0);

        double totalAmount = 0;
        int totalCount = 0;

        for (Map<String, Object> row : result) {
            String status = row.get("status") != null ? row.get("status").toString().toLowerCase() : "";
            double amount = row.get("amount") != null ? ((Number) row.get("amount")).doubleValue() : 0;
            int count = row.get("total") != null ? ((Number) row.get("total")).intValue() : 0;

            switch (status) {
                case "success":
                    success.put("totalAmount", amount);
                    success.put("totalCount", count);
                    break;
                case "failed":
                    failed.put("totalAmount", amount);
                    failed.put("totalCount", count);
                    break;
                case "pending":
                    pending.put("totalAmount", amount);
                    pending.put("totalCount", count);
                    break;
            }

            totalAmount += amount;
            totalCount += count;
        }

        if (totalCount > 0) {
            success.put("percentage", Math.round(((int) success.get("totalCount") / (double) totalCount) * 100) / 100.0);
            failed.put("percentage", Math.round(((int) failed.get("totalCount") / (double) totalCount) * 100) / 100.0);
            pending.put("percentage", Math.round(((int) pending.get("totalCount") / (double) totalCount) * 100) / 100.0);
        }

        Map<String, Object> all = new HashMap<>();
        all.put("status", "total volume");
        all.put("totalAmount", totalAmount);
        all.put("totalCount", totalCount);

        return List.of(failed, success, pending, all);
    }

}
