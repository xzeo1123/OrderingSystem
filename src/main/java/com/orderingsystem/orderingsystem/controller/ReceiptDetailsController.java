package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.ReceiptDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receiptdetails")
@RequiredArgsConstructor
@CrossOrigin
public class ReceiptDetailsController {

    private final ReceiptDetailsService receiptDetailsService;

    @PostMapping
    public ResponseEntity<?> createReceiptDetail(@RequestBody @Valid ReceiptDetailsRequest request) {
        ReceiptDetailsResponse created = receiptDetailsService.createReceiptDetail(request);
        return ResponseHelper.created(created, "Receipt detail created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReceiptDetail(@PathVariable Integer id, @RequestBody @Valid ReceiptDetailsRequest request) {
        ReceiptDetailsResponse updated = receiptDetailsService.updateReceiptDetail(id, request);
        return ResponseHelper.ok(updated, "Receipt detail updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReceiptDetail(@PathVariable Integer id) {
        receiptDetailsService.deleteReceiptDetail(id);
        return ResponseHelper.deleted("Receipt detail deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReceiptDetailById(@PathVariable Integer id) {
        ReceiptDetailsResponse receiptdetail = receiptDetailsService.getReceiptDetailById(id);
        return ResponseHelper.ok(receiptdetail, "Get receipt detail by ID successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllReceiptDetails() {
        List<ReceiptDetailsResponse> receiptdetails = receiptDetailsService.getAllReceiptDetails();
        return ResponseHelper.ok(receiptdetails, "Get list of receipt details successfully");
    }
}
