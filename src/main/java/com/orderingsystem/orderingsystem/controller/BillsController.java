package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.BillsService;
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
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
@CrossOrigin
public class BillsController {

    private final BillsService billsService;

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBill(@RequestBody @Valid BillsRequest billsRequest) {
        BillsResponse created = billsService.createBill(billsRequest);
        return ResponseHelper.created(created, "Bill created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{billId}")
    public ResponseEntity<?> updateBill(@PathVariable Integer billId, @RequestBody @Valid BillsRequest billsRequest) {
        BillsResponse updated = billsService.updateBill(billId, billsRequest);
        return ResponseHelper.ok(updated, "Bill updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{billId}")
    public ResponseEntity<?> softDeleteBill(@PathVariable Integer billId) {
        BillsResponse updated = billsService.softDeleteBill(billId);
        return ResponseHelper.ok(updated, "Bill soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{billId}")
    public ResponseEntity<?> deleteBill(@PathVariable Integer billId) {
        billsService.deleteBill(billId);
        return ResponseHelper.deleted("Bill deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{billId}")
    public ResponseEntity<?> getBillById(@PathVariable Integer billId) {
        BillsResponse bill = billsService.getBillById(billId);
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

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/user")
    public ResponseEntity<?> getBillsByUser(@RequestParam Integer userId) {
        List<BillsResponse> bills = billsService.getBillsByUser(userId);
        return ResponseHelper.ok(bills, "Get bills by user successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/table")
    public ResponseEntity<?> getBillsByTable(@RequestParam Integer tableId) {
        List<BillsResponse> bills = billsService.getBillsByTable(tableId);
        return ResponseHelper.ok(bills, "Get bills by table successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/date")
    public ResponseEntity<?> getBillsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<BillsResponse> bills = billsService.getBillsByDateRange(startDate, endDate);
        return ResponseHelper.ok(bills, "Get bills by date range successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/total")
    public ResponseEntity<?> getBillsByTotalRange(
            @RequestParam Double minTotal,
            @RequestParam Double maxTotal) {
        List<BillsResponse> bills = billsService.getBillsByTotalRange(minTotal, maxTotal);
        return ResponseHelper.ok(bills, "Get bills by total range successfully");
    }
}
