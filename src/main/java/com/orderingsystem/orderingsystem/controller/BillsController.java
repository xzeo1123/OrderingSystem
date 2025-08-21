package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.BillsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
@CrossOrigin
public class BillsController {

    private final BillsService billsService;

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBill(@RequestBody @Valid BillsRequest request) {
        BillsResponse created = billsService.createBill(request);
        return ResponseHelper.created(created, "Bill created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBill(@PathVariable Integer id, @RequestBody @Valid BillsRequest request) {
        BillsResponse updated = billsService.updateBill(id, request);
        return ResponseHelper.ok(updated, "Bill updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteBill(@PathVariable Integer id) {
        BillsResponse updated = billsService.softDeleteBill(id);
        return ResponseHelper.ok(updated, "Bill soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Integer id) {
        billsService.deleteBill(id);
        return ResponseHelper.deleted("Bill deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBillById(@PathVariable Integer id) {
        BillsResponse bill = billsService.getBillById(id);
        return ResponseHelper.ok(bill, "Get bill by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllBills(
            @RequestParam Integer page,
            @RequestParam Integer size) {

        if (page < 0) page = 0;
        if (size > 20) size = 20;

        Page<BillsResponse> bills = billsService.getAllBills(page, size);
        return ResponseHelper.ok(bills, "Get list of bills successfully");
    }
}
