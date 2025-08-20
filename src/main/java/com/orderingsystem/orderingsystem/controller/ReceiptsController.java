package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.ReceiptsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
@CrossOrigin
public class ReceiptsController {

    private final ReceiptsService receiptsService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createReceipt(@RequestBody @Valid ReceiptsRequest request) {
        ReceiptsResponse created = receiptsService.createReceipt(request);
        return ResponseHelper.created(created, "Receipt created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReceipt(@PathVariable Integer id, @RequestBody @Valid ReceiptsRequest request) {
        ReceiptsResponse updated = receiptsService.updateReceipt(id, request);
        return ResponseHelper.ok(updated, "Receipt updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteBill(@PathVariable Integer id) {
        ReceiptsResponse updated = receiptsService.softDeleteBill(id);
        return ResponseHelper.ok(updated, "Receipt soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReceipt(@PathVariable Integer id) {
        receiptsService.deleteReceipt(id);
        return ResponseHelper.deleted("Receipt deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getReceiptById(@PathVariable Integer id) {
        ReceiptsResponse receipt = receiptsService.getReceiptById(id);
        return ResponseHelper.ok(receipt, "Get receipt by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllReceipts() {
        List<ReceiptsResponse> receipts = receiptsService.getAllReceipts();
        return ResponseHelper.ok(receipts, "Get list of receipts successfully");
    }
}
