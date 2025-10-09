package com.trustlypay.DashboardApis.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/merchant")
public class MerchantsVendorController {

    private final JdbcTemplate jdbcTemplate;

    public MerchantsVendorController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all-merchants-vendors")
    public ResponseEntity<Map<String, Object>> getAllMerchantsAndVendors() {


        String merchantSql = "SELECT DISTINCT name FROM merchant";
        List<String> merchants = jdbcTemplate.queryForList(merchantSql, String.class);


        String payinVendorSql = "SELECT DISTINCT bank_name FROM vendor_bank";
        List<String> payinVendors = jdbcTemplate.queryForList(payinVendorSql, String.class);

        String payoutVendorSql = "SELECT DISTINCT bank_name FROM payout_vendor_bank";
        List<String> payoutVendors = jdbcTemplate.queryForList(payoutVendorSql, String.class);


        Map<String, Object> response = new HashMap<>();
        response.put("merchants", merchants);
        response.put("payinVendors", payinVendors);
        response.put("payoutVendors", payoutVendors);



        return ResponseEntity.ok(response);
    }
}
