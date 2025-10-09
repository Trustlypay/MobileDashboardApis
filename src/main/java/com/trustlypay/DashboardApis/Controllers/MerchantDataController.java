package com.trustlypay.DashboardApis.Controllers;


import com.trustlypay.DashboardApis.Models.MerchantData;
import com.trustlypay.DashboardApis.Service.MerchantDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/merchantsData")
public class MerchantDataController {


    private final MerchantDataService merchantDataService;

    @Autowired
    public MerchantDataController(MerchantDataService merchantDataService) {
        this.merchantDataService = merchantDataService;
    }

    @GetMapping("/payin")
    public ResponseEntity<Map<String, Object>> getPayinMerchantsData() {
        List<MerchantData> summaries = merchantDataService.getPayinMerchantsData();

        Map<String, Object> response = new HashMap<>();
        response.put("payin", summaries);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payout")

    public ResponseEntity<Map<String, Object>> getPayoutMerchantsData(){
        List<MerchantData> summaries = merchantDataService.getPayoutMerchantsData();
        Map<String, Object> response = new HashMap<>();
        response.put("payout", summaries);

        return ResponseEntity.ok(response);
    }


    }

