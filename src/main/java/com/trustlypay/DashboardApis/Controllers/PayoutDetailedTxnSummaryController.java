package com.trustlypay.DashboardApis.Controllers;

import com.trustlypay.DashboardApis.Models.PayinDetailedTxnsFilter;
import com.trustlypay.DashboardApis.Models.PayoutDetailedTxnsFilter;
import com.trustlypay.DashboardApis.Service.PayoutDetailedTxnSummaryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/merchant")
@CrossOrigin(origins = "*")
public class PayoutDetailedTxnSummaryController {


    private final PayoutDetailedTxnSummaryService payoutDetailedTxnSummaryService;

    public PayoutDetailedTxnSummaryController(PayoutDetailedTxnSummaryService payoutDetailedTxnSummaryService) {
        this.payoutDetailedTxnSummaryService = payoutDetailedTxnSummaryService;
    }

    @GetMapping("/payout-detailed-summary")
    public ResponseEntity<Map<String, Object>> getPayoutDetailedSummary(

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

                    schema = @Schema(type = "array", allowableValues = {"SUCCESS", "PENDING", "FAILED"})
            )
            @RequestParam(required = false) List<String> transactionStatus,

            @Parameter()
            @RequestParam(required = false) List<Long> merchantId
    ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PayoutDetailedTxnsFilter dto = new PayoutDetailedTxnsFilter();
        dto.setFromDate(fromDate);
        dto.setToDate(toDate);
        dto.setPageNumber(pageNumber);
        dto.setPageSize(pageSize);
        dto.setTransactionId(transactionId);
        dto.setUtr(utr);
        dto.setUdf1(udf1);
        dto.setTransactionStatus(transactionStatus);
        dto.setMerchantId(merchantId);

        return ResponseEntity.ok(payoutDetailedTxnSummaryService.getPayoutDetailedTransactionSummary(dto));
    }
}

