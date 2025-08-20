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
    public ResponseEntity<?> createBillDetail(@RequestBody @Valid BillDetailsRequest request) {
        BillDetailsResponse created = billDetailsService.createBillDetail(request);
        return ResponseHelper.created(created, "Bill details created successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBillDetail(@PathVariable Integer id) {
        billDetailsService.deleteBillDetail(id);
        return ResponseHelper.deleted("Bill details deleted successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBillDetailById(@PathVariable Integer id) {
        BillDetailsResponse billDetail = billDetailsService.getBillDetailById(id);
        return ResponseHelper.ok(billDetail, "Get bill details by ID successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllBillDetails() {
        List<BillDetailsResponse> billDetails = billDetailsService.getAllBillDetails();
        return ResponseHelper.ok(billDetails, "Get list of bill details successfully");
    }
}
