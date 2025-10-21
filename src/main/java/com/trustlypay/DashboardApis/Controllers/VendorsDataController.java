package com.trustlypay.DashboardApis.Controllers;



import com.trustlypay.DashboardApis.Models.VendorData;
import com.trustlypay.DashboardApis.Service.VendorDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/vendorsData")
@CrossOrigin(origins = "*")
public class VendorsDataController {

    private final VendorDataService vendorDataService;

    @Autowired
    public VendorsDataController(VendorDataService vendorDataService){
        this.vendorDataService = vendorDataService;
    }



    @GetMapping("/payin")
    @Operation(summary = "Get Payin Vendor Data")

    public ResponseEntity<Map<String, Object>> getPayinVendorsData(
            @Parameter(description = "Start date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-01 00:00:00", required = true)
            @RequestParam String fromDate,

            @Parameter(description = "End date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-31 23:59:59", required = true)
            @RequestParam String toDate) {

        List<VendorData> summaries = vendorDataService.getPayinVendorsData(fromDate, toDate);

        Map<String, Object> response = new HashMap<>();
        response.put("payin", summaries);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/payout")
    @Operation(summary = "Get Payout Vendor Data")

    public ResponseEntity<Map<String, Object>> getPayoutVendorsData(
            @Parameter(description = "Start date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-01 00:00:00", required = true)
            @RequestParam String fromDate,

            @Parameter(description = "End date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-31 23:59:59", required = true)
            @RequestParam String toDate) {

        List<VendorData> summaries = vendorDataService.getPayoutVendorsData(fromDate, toDate);

        Map<String, Object> response = new HashMap<>();
        response.put("payout", summaries);

        return ResponseEntity.ok(response);
    }

}
