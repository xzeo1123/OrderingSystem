package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.service.ExportExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/export")
@RequiredArgsConstructor
public class ExportExcelController {

    private final ExportExcelService exportExcelService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/products")
    public ResponseEntity<InputStreamResource> exportProducts() throws IOException {
        return exportExcelService.exportProducts();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/categories")
    public ResponseEntity<InputStreamResource> exportCategories() throws IOException {
        return exportExcelService.exportCategories();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<InputStreamResource> exportUsers() throws IOException {
        return exportExcelService.exportUsers();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/tables")
    public ResponseEntity<InputStreamResource> exportTables() throws IOException {
        return exportExcelService.exportTables();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/bills")
    public ResponseEntity<InputStreamResource> exportBills() throws IOException {
        return exportExcelService.exportBills();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/receipts")
    public ResponseEntity<InputStreamResource> exportReceipts() throws IOException {
        return exportExcelService.exportReceipts();
    }
}
