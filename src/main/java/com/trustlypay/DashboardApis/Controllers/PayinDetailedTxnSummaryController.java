package com.trustlypay.DashboardApis.Controllers;


import com.trustlypay.DashboardApis.Models.PayinDetailedTxnsFilter;
import com.trustlypay.DashboardApis.Service.PayinDetailedTxnSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/merchant")
public class PayinDetailedTxnSummaryController {

    private final PayinDetailedTxnSummaryService payinDetailedTxnSummaryService;

    public PayinDetailedTxnSummaryController(PayinDetailedTxnSummaryService payinDetailedTxnSummaryService) {
        this.payinDetailedTxnSummaryService = payinDetailedTxnSummaryService;
    }

    @GetMapping("/detailed-summary")
    public ResponseEntity<Map<String, Object>> getPayinDetailedSummary(

            @Parameter(description = "Start date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-01 00:00:00", required = true)
            @RequestParam String fromDate,

            @Parameter(description = "End date (YYYY-MM-DD HH:MM:SS)", example = "2025-07-31 23:59:59", required = true)
            @RequestParam String toDate,

            @Parameter(description = "Page number", example = "1", required = true)
            @RequestParam Integer pageNumber,

            @Parameter(description = "Page size", example = "10", required = true)
            @RequestParam Integer pageSize,

            @Parameter(description = "Transaction ID (Alphanumeric)", example = "TXN12345")
            @RequestParam(required = false) String transactionId,

            @Parameter(description = "UTR number", example = "1234567890")
            @RequestParam(required = false) String utr,

            @Parameter(description = "UDF1 value (Alphanumeric)", example = "Order_001")
            @RequestParam(required = false) String udf1,

            @Parameter(
                    description = "Transaction Status (single or multiple: success, pending, failed)",
                    example = "success,pending",
                    schema = @Schema(type = "array", allowableValues = {"success","pending","failed"})
            )
            @RequestParam(required = false) List<String> transactionStatus,

            @Parameter(description = "Merchant IDs (single or multiple)", example = "1,2,12")
            @RequestParam(required = false) List<Long> merchantId
    ) {
        // Parse date strings to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PayinDetailedTxnsFilter dto = new PayinDetailedTxnsFilter();
        dto.setFromDate(fromDate);
        dto.setToDate(toDate);
        dto.setPageNumber(pageNumber);
        dto.setPageSize(pageSize);
        dto.setTransactionId(transactionId);
        dto.setUtr(utr);
        dto.setUdf1(udf1);
        dto.setTransactionStatus(transactionStatus);
        dto.setMerchantId(merchantId);

        return ResponseEntity.ok(payinDetailedTxnSummaryService.getPayinDetailedTransactionSummary(dto));
    }
}