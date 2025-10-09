package com.trustlypay.DashboardApis.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class MerchantServicesServices {

    private final JdbcTemplate jdbcTemplate;

    public MerchantServicesServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Get payin/payout status for all merchants along with merchant name
     *
     * @return List of merchants with merchantName, payinEnabled, payoutEnabled
     */
    public List<Map<String, Object>> getAllMerchantsPayinPayoutStatus() {
        String sql = "SELECT ms.merchant_id, m.name AS merchantName, ms.payin, ms.payout " +
                "FROM trustlypay_db.merchant_services ms " +
                "JOIN trustlypay_db.merchant m ON ms.merchant_id = m.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> Map.of(
                "merchantId", rs.getInt("merchant_id"),
                "merchantName", rs.getString("merchantName"),
                "payinEnabled", rs.getInt("payin") == 1,
                "payoutEnabled", rs.getInt("payout") == 1
        ));
    }
}
