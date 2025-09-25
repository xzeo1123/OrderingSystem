package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.ReceiptDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receiptdetails")
@RequiredArgsConstructor
@CrossOrigin
public class ReceiptDetailsController {

    private final ReceiptDetailsService receiptDetailsService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createReceiptDetail(@RequestBody @Valid ReceiptDetailsRequest receiptDetailsRequest) {
        ReceiptDetailsResponse created = receiptDetailsService.createReceiptDetail(receiptDetailsRequest);
        return ResponseHelper.created(created, "Receipt detail created successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{receiptDetailsRequest}")
    public ResponseEntity<?> deleteReceiptDetail(@PathVariable Integer receiptDetailId) {
        receiptDetailsService.deleteReceiptDetail(receiptDetailId);
        return ResponseHelper.deleted("Receipt detail deleted successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{receiptDetailsRequest}")
    public ResponseEntity<?> getReceiptDetailById(@PathVariable Integer receiptDetailId) {
        ReceiptDetailsResponse receiptDetails = receiptDetailsService.getReceiptDetailById(receiptDetailId);
        return ResponseHelper.ok(receiptDetails, "Get receipt details by ID successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllReceiptDetails() {
        List<ReceiptDetailsResponse> receiptDetails = receiptDetailsService.getAllReceiptDetails();
        return ResponseHelper.ok(receiptDetails, "Get list of receipt details successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/receipt")
    public ResponseEntity<?> getReceiptDetailsByReceipt(@RequestParam Integer billId) {
        List<ReceiptDetailsResponse> receiptDetails = receiptDetailsService.getReceiptDetailsByReceipt(billId);
        return ResponseHelper.ok(receiptDetails, "Get receipt details by bill successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/product")
    public ResponseEntity<?> getReceiptDetailsByProduct(@RequestParam Integer productId) {
        List<ReceiptDetailsResponse> receiptDetails = receiptDetailsService.getReceiptDetailsByProduct(productId);
        return ResponseHelper.ok(receiptDetails, "Get receipt details by product successfully");
    }
}
