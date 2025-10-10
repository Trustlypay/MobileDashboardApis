package com.trustlypay.DashboardApis.Controllers;

import com.trustlypay.DashboardApis.Models.OverViewFilter;
import com.trustlypay.DashboardApis.Service.DasboardSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard Summary API", description = "Provides transaction summaries")
@CrossOrigin(origins = "*")
public class DashboardSummaryController {

    private final DasboardSummaryService dasboardSummaryService;

    public DashboardSummaryController(DasboardSummaryService dasboardSummaryService) {
        this.dasboardSummaryService = dasboardSummaryService;
    }

    @GetMapping("/payin-summary")
    @Operation(summary = "Get Payin Transaction Summary",
            description = "Returns failed, success, pending transactions and total volume for given date range and optional merchantId")
    public ResponseEntity<List<Map<String, Object>>> getDashboardPayinTransactionSummary(
            @Parameter(description = "Start date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-01 00:00:00", required = true)
            @RequestParam String fromDate,

            @Parameter(description = "End date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-31 23:59:59", required = true)
            @RequestParam String toDate,

            @Parameter(description = "Merchant ID (optional)", example = "merchant_123")
            @RequestParam(required = false) String merchantId) {

        try {
            List<Map<String, Object>> summary = dasboardSummaryService.getPayinDashboardSummary(fromDate, toDate, merchantId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(
                            Map.of("error", "Unable to fetch transaction summary")
                    ));
        }
    }

    @GetMapping("/payout-summary")
    @Operation(summary = "Get Payouy Transaction Summary",
            description = "Returns failed, success, pending transactions and total volume for given date range and optional merchantId")
    public ResponseEntity<List<Map<String, Object>>> getDashboardPayoutTransactionSummary(
            @Parameter(description = "Start date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-01 00:00:00", required = true)
            @RequestParam String fromDate,

            @Parameter(description = "End date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-31 23:59:59", required = true)
            @RequestParam String toDate,

            @Parameter(description = "Merchant ID (optional)", example = "merchant_123")
            @RequestParam(required = false) Integer merchantId) {

        try {
            List<Map<String, Object>> summary = dasboardSummaryService.getDashboardPayoutTransactionSummary(fromDate, toDate, merchantId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(
                            Map.of("error", "Unable to fetch transaction summary")
                    ));
        }
    }

}