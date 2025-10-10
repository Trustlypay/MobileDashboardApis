package com.trustlypay.DashboardApis.Controllers;


import com.trustlypay.DashboardApis.Service.DasboardSummaryService;
import com.trustlypay.DashboardApis.Service.MerchantServicesServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "*")
public class MerchantServicesController {

    private final MerchantServicesServices merchantServicesServices;

    public MerchantServicesController(MerchantServicesServices merchantServicesServices) {
        this.merchantServicesServices = merchantServicesServices;
    }


    @GetMapping("/merchants/status")
    @Operation(summary = "Get Payin/Payout status for all merchants",
            description = "Returns merchantName, payinEnabled, and payoutEnabled flags for all merchants")
    public ResponseEntity<List<Map<String, Object>>> getAllMerchantsStatus() {
        List<Map<String, Object>> result = merchantServicesServices.getAllMerchantsPayinPayoutStatus();
        return ResponseEntity.ok(result);
    }
}



