package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.ReceiptsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
@CrossOrigin
public class ReceiptsController {

    private final ReceiptsService receiptsService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createReceipt(@RequestBody @Valid ReceiptsRequest receiptsRequest) {
        ReceiptsResponse created = receiptsService.createReceipt(receiptsRequest);
        return ResponseHelper.created(created, "Receipt created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{receiptId}")
    public ResponseEntity<?> updateReceipt(@PathVariable Integer receiptId, @RequestBody @Valid ReceiptsRequest receiptsRequest) {
        ReceiptsResponse updated = receiptsService.updateReceipt(receiptId, receiptsRequest);
        return ResponseHelper.ok(updated, "Receipt updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{receiptId}")
    public ResponseEntity<?> softDeleteBill(@PathVariable Integer receiptId) {
        ReceiptsResponse updated = receiptsService.softDeleteBill(receiptId);
        return ResponseHelper.ok(updated, "Receipt soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{receiptId}")
    public ResponseEntity<?> deleteReceipt(@PathVariable Integer receiptId) {
        receiptsService.deleteReceipt(receiptId);
        return ResponseHelper.deleted("Receipt deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{receiptId}")
    public ResponseEntity<?> getReceiptById(@PathVariable Integer receiptId) {
        ReceiptsResponse receipt = receiptsService.getReceiptById(receiptId);
        return ResponseHelper.ok(receipt, "Get receipt by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllReceipts(
            @RequestParam Integer page,
            @RequestParam Integer size) {

        if (page < 0) page = 0;
        if (size > 20) size = 20;

        Page<ReceiptsResponse> receipts = receiptsService.getAllReceipts(page, size);
        return ResponseHelper.ok(receipts, "Get list of receipts successfully");
    }


    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/date")
    public ResponseEntity<?> getReceiptsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ReceiptsResponse> receipts = receiptsService.getReceiptsByDateRange(startDate, endDate);
        return ResponseHelper.ok(receipts, "Get receipts by date range successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/total")
    public ResponseEntity<?> getReceiptsByTotalRange(
            @RequestParam Double minTotal,
            @RequestParam Double maxTotal) {
        List<ReceiptsResponse> receipts = receiptsService.getReceiptsByTotalRange(minTotal, maxTotal);
        return ResponseHelper.ok(receipts, "Get receipts by total range successfully");
    }
}
