package com.trustlypay.DashboardApis.Service;

import com.trustlypay.DashboardApis.Models.PayoutDetailedTxnsFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PayoutDetailedTxnSummaryService {

    private final JdbcTemplate jdbcTemplate;

    public PayoutDetailedTxnSummaryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getPayoutDetailedTransactionSummary(PayoutDetailedTxnsFilter filterDto) {

        int pageNumber = (filterDto.getPageNumber() != null) ? filterDto.getPageNumber() : 1;
        int pageSize = (filterDto.getPageSize() != null) ? filterDto.getPageSize() : 10;
        int offset = (pageNumber - 1) * pageSize;



        List<String> allowedStatuses = Arrays.asList("SUCCESS", "PENDING", "FAILED");
        List<String> statusFilter = (filterDto.getTransactionStatus() != null)
                ? filterDto.getTransactionStatus().stream()
                .filter(allowedStatuses::contains)
                .collect(Collectors.toList())
                : allowedStatuses;

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        // 🔹 Main payout table
        sql.append("(")
                .append("SELECT p.created_at, p.ben_name, p.amount, p.transfer_id, ")
                .append("p.status, p.udf1, m.name AS merchant_name ")
                .append("FROM payout_transactions p ")
                .append("LEFT JOIN merchant m ON p.merchant_id = m.id ")
                .append("WHERE p.created_at BETWEEN ? AND ? ");
        params.add(filterDto.getFromDate());
        params.add(filterDto.getToDate());

        if (filterDto.getTransactionId() != null && !filterDto.getTransactionId().isEmpty()) {
            sql.append("AND p.transfer_id LIKE ? ");
            params.add("%" + filterDto.getTransactionId() + "%");
        }

        if (filterDto.getUtr() != null && !filterDto.getUtr().isEmpty()) {
            sql.append("AND p.utr LIKE ? ");
            params.add("%" + filterDto.getUtr() + "%");
        }

        if (filterDto.getUdf1() != null && !filterDto.getUdf1().isEmpty()) {
            sql.append("AND p.udf1 LIKE ? ");
            params.add("%" + filterDto.getUdf1() + "%");
        }

        if (!statusFilter.isEmpty()) {
            sql.append("AND p.status IN (")
                    .append(statusFilter.stream().map(s -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(statusFilter);
        }

        if (filterDto.getMerchantId() != null && !filterDto.getMerchantId().isEmpty()) {
            sql.append("AND p.merchant_id IN (")
                    .append(filterDto.getMerchantId().stream().map(id -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(filterDto.getMerchantId());

        }

        sql.append(")");

        // 🔹 Union with backup table (if you have payout_transactions_bkp)
        sql.append(" UNION ALL (")
                .append("SELECT p.created_at, p.ben_name, p.amount, p.transfer_id, ")
                .append("p.status, p.udf1, m.name AS merchant_name ")
                .append("FROM payout_transactions_bkp p ")
                .append("LEFT JOIN merchant m ON p.merchant_id = m.id ")
                .append("WHERE p.created_at BETWEEN ? AND ? ");
        params.add(filterDto.getFromDate());
        params.add(filterDto.getToDate());

        if (filterDto.getTransactionId() != null && !filterDto.getTransactionId().isEmpty()) {
            sql.append("AND p.transfer_id LIKE ? ");
            params.add("%" + filterDto.getTransactionId() + "%");
        }

        if (filterDto.getUtr() != null && !filterDto.getUtr().isEmpty()) {
            sql.append("AND p.utr LIKE ? ");
            params.add("%" + filterDto.getUtr() + "%");
        }

        if (filterDto.getUdf1() != null && !filterDto.getUdf1().isEmpty()) {
            sql.append("AND p.udf1 LIKE ? ");
            params.add("%" + filterDto.getUdf1() + "%");
        }

        if (!statusFilter.isEmpty()) {
            sql.append("AND p.status IN (")
                    .append(statusFilter.stream().map(s -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(statusFilter);
        }

        if (filterDto.getMerchantId() != null && !filterDto.getMerchantId().isEmpty()) {
            sql.append("AND p.merchant_id IN (")
                    .append(filterDto.getMerchantId().stream().map(id -> "?").collect(Collectors.joining(",")))
                    .append(") ");
            params.addAll(filterDto.getMerchantId());
        }

        sql.append(")");

        // 🔹 Count total rows
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS count_table";
        int total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);

        // 🔹 Pagination
        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ? ");
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
