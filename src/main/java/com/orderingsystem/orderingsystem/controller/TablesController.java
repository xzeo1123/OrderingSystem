package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.TablesRequest;
import com.orderingsystem.orderingsystem.dto.response.TablesResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.TablesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
@CrossOrigin
public class TablesController {

    private final TablesService tablesService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTable(@RequestBody @Valid TablesRequest tablesRequest) {
        TablesResponse created = tablesService.createTable(tablesRequest);
        return ResponseHelper.created(created, "Table created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable Integer tableId, @RequestBody @Valid TablesRequest tablesRequest) {
        TablesResponse updated = tablesService.updateTable(tableId, tablesRequest);
        return ResponseHelper.ok(updated, "Table updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteTable(@PathVariable Integer tableId) {
        TablesResponse updated = tablesService.softDeleteTable(tableId);
        return ResponseHelper.ok(updated, "Table soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Integer tableId) {
        tablesService.deleteTable(tableId);
        return ResponseHelper.deleted("Table deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTableById(@PathVariable Integer tableId) {
        TablesResponse table = tablesService.getTableById(tableId);
        return ResponseHelper.ok(table, "Get table by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllTables() {
        List<TablesResponse> tables = tablesService.getAllTables();
        return ResponseHelper.ok(tables, "Get list of tables successfully");
    }
}
