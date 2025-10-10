package com.trustlypay.DashboardApis.Service;


import com.trustlypay.DashboardApis.Models.PayinDetailedTxnsFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;



import java.util.*;


@Service
public class PayinDetailedTxnSummaryService {

    private final JdbcTemplate jdbcTemplate;

    public PayinDetailedTxnSummaryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getPayinDetailedTransactionSummary(PayinDetailedTxnsFilter filterDto) {

        int pageNumber = filterDto.getPageNumber() != null ? filterDto.getPageNumber() : 1;
        int pageSize = filterDto.getPageSize() != null ? filterDto.getPageSize() : 10;
        int offset = (pageNumber - 1) * pageSize;

        // Allowed statuses
        List<String> allowedStatuses = Arrays.asList("success", "pending", "failed");

        // Use only allowed statuses if filter provided
        List<String> statusFilter = filterDto.getTransactionStatus() != null ?
                filterDto.getTransactionStatus().stream()
                        .filter(allowedStatuses::contains)
                        .collect(Collectors.toList())
                : allowedStatuses;

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        // Construct query for both tables using UNION ALL
        sql.append("(")
                .append("SELECT lp.transaction_date, lp.created_date, lp.transaction_username, ")
                .append("lp.transaction_amount, lp.transaction_gid, lp.transaction_status, ")
                .append("lp.bank_ref_no, lp.udf1, m.name AS merchant_name, vb.bank_name AS vendor_name ")
                .append("FROM live_payment lp ")
                .append("LEFT JOIN merchant m ON lp.created_merchant = m.id ")
                .append("LEFT JOIN vendor_bank vb ON lp.vendor_id = vb.id ")
                .append("WHERE lp.created_date BETWEEN ? AND ? ");

        params.add(filterDto.getFromDate());
        params.add(filterDto.getToDate());

        if (filterDto.getTransactionId() != null && !filterDto.getTransactionId().isEmpty()) {
            sql.append("AND lp.transaction_gid LIKE ? ");
            params.add("%" + filterDto.getTransactionId() + "%");
        }

        if (filterDto.getUtr() != null && !filterDto.getUtr().isEmpty()) {
            sql.append("AND lp.bank_ref_no LIKE ? ");
            params.add("%" + filterDto.getUtr() + "%");
        }

        if (filterDto.getUdf1() != null && !filterDto.getUdf1().isEmpty()) {
            sql.append("AND lp.udf1 LIKE ? ");
            params.add("%" + filterDto.getUdf1() + "%");
        }

        if (!statusFilter.isEmpty()) {
            sql.append("AND lp.transaction_status IN (")
                    .append(statusFilter.stream().map(s -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(statusFilter);
        }

        if (filterDto.getMerchantId() != null && !filterDto.getMerchantId().isEmpty()) {
            sql.append("AND lp.created_merchant IN (")
                    .append(filterDto.getMerchantId().stream().map(id -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(filterDto.getMerchantId());
        }

        sql.append(")")
                .append(" UNION ALL ")
                .append("(")
                .append("SELECT lp.transaction_date, lp.created_date, lp.transaction_username, ")
                .append("lp.transaction_amount, lp.transaction_gid, lp.transaction_status, ")
                .append("lp.bank_ref_no, lp.udf1, m.name AS merchant_name, vb.bank_name AS vendor_name ")
                .append("FROM live_payment_bkp lp ")
                .append("LEFT JOIN merchant m ON lp.created_merchant = m.id ")
                .append("LEFT JOIN vendor_bank vb ON lp.vendor_id = vb.id ")
                .append("WHERE lp.created_date BETWEEN ? AND ? ");

        params.add(filterDto.getFromDate());
        params.add(filterDto.getToDate());

        if (filterDto.getTransactionId() != null && !filterDto.getTransactionId().isEmpty()) {
            sql.append("AND lp.transaction_gid LIKE ? ");
            params.add("%" + filterDto.getTransactionId() + "%");
        }

        if (filterDto.getUtr() != null && !filterDto.getUtr().isEmpty()) {
            sql.append("AND lp.bank_ref_no LIKE ? ");
            params.add("%" + filterDto.getUtr() + "%");
        }

        if (filterDto.getUdf1() != null && !filterDto.getUdf1().isEmpty()) {
            sql.append("AND lp.udf1 LIKE ? ");
            params.add("%" + filterDto.getUdf1() + "%");
        }

        if (!statusFilter.isEmpty()) {
            sql.append("AND lp.transaction_status IN (")
                    .append(statusFilter.stream().map(s -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(statusFilter);
        }

        if (filterDto.getMerchantId() != null && !filterDto.getMerchantId().isEmpty()) {
            sql.append("AND lp.created_merchant IN (")
                    .append(filterDto.getMerchantId().stream().map(id -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(filterDto.getMerchantId());
        }

        sql.append(")");

        // Count total
        String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") AS count_table";
        int total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);

        // Add pagination
        sql.append(" LIMIT ? OFFSET ? ");
        params.add(pageSize);
        params.add(offset);

        List<Map<String, Object>> items = jdbcTemplate.queryForList(sql.toString(), params.toArray());

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("totalPages", (int) Math.ceil((double) total / pageSize));
        result.put("pageNumber", pageNumber);
        result.put("pageSize", pageSize);
        result.put("items", items);

        return result;
    }
}