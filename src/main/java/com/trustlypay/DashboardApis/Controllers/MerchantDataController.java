package com.trustlypay.DashboardApis.Controllers;


import com.trustlypay.DashboardApis.Models.MerchantData;
import com.trustlypay.DashboardApis.Service.MerchantDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Operation(summary = "Get Payin Merchant Data")

    public ResponseEntity<Map<String, Object>> getPayinMerchantsData(
            @Parameter(description = "Start date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-01 00:00:00", required = true)
            @RequestParam String fromDate,

            @Parameter(description = "End date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-31 23:59:59", required = true)
            @RequestParam String toDate) {

        List<MerchantData> summaries = merchantDataService.getPayinMerchantsData(fromDate, toDate);

        Map<String, Object> response = new HashMap<>();
        response.put("payin", summaries);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payout")
    @Operation(summary = "Get Payout Merchant Data")
    public ResponseEntity<Map<String, Object>> getPayoutMerchantsData(@Parameter(description = "Start date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-01 00:00:00", required = true)
                                                                      @RequestParam String fromDate,

                                                                      @Parameter(description = "End date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-31 23:59:59", required = true)
                                                                      @RequestParam String toDate) {
        List<MerchantData> summaries = merchantDataService.getPayoutMerchantsData(fromDate, toDate);
        Map<String, Object> response = new HashMap<>();
        response.put("payout", summaries);

        return ResponseEntity.ok(response);
    }


}

