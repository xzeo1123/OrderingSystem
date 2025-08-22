package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.BillDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillDetailsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.BillDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billdetails")
@RequiredArgsConstructor
@CrossOrigin
public class BillDetailsController {

    private final BillDetailsService billDetailsService;

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBillDetail(@RequestBody @Valid BillDetailsRequest billDetailsRequest) {
        BillDetailsResponse created = billDetailsService.createBillDetail(billDetailsRequest);
        return ResponseHelper.created(created, "Bill details created successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBillDetail(@PathVariable Integer billDetailsId) {
        billDetailsService.deleteBillDetail(billDetailsId);
        return ResponseHelper.deleted("Bill details deleted successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBillDetailById(@PathVariable Integer billDetailsId) {
        BillDetailsResponse billDetail = billDetailsService.getBillDetailById(billDetailsId);
        return ResponseHelper.ok(billDetail, "Get bill details by ID successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllBillDetails() {
        List<BillDetailsResponse> billDetails = billDetailsService.getAllBillDetails();
        return ResponseHelper.ok(billDetails, "Get list of bill details successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/bill")
    public ResponseEntity<?> getBillDetailsByBill(@RequestParam Integer billId) {
        List<BillDetailsResponse> billDetails = billDetailsService.getBillDetailsByBill(billId);
        return ResponseHelper.ok(billDetails, "Get bill details by bill successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/product")
    public ResponseEntity<?> getBillDetailsByProduct(@RequestParam Integer productId) {
        List<BillDetailsResponse> billDetails = billDetailsService.getBillDetailsByProduct(productId);
        return ResponseHelper.ok(billDetails, "Get bill details by product successfully");
    }
}
