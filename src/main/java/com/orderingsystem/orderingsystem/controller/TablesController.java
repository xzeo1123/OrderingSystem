package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.TablesRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import com.orderingsystem.orderingsystem.dto.response.TablesResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.TablesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
@CrossOrigin
public class TablesController {

    private final TablesService tablesService;

    @PostMapping
    public ResponseEntity<?> createTable(@RequestBody @Valid TablesRequest request) {
        TablesResponse created = tablesService.createTable(request);
        return ResponseHelper.created(created, "Table created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable Integer id, @RequestBody @Valid TablesRequest request) {
        TablesResponse updated = tablesService.updateTable(id, request);
        return ResponseHelper.ok(updated, "Table updated successfully");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteTable(@PathVariable Integer id) {
        TablesResponse updated = tablesService.softDeleteTable(id);
        return ResponseHelper.ok(updated, "Table soft deleted successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Integer id) {
        tablesService.deleteTable(id);
        return ResponseHelper.deleted("Table deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTableById(@PathVariable Integer id) {
        TablesResponse table = tablesService.getTableById(id);
        return ResponseHelper.ok(table, "Get table by ID successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllTables() {
        List<TablesResponse> tables = tablesService.getAllTables();
        return ResponseHelper.ok(tables, "Get list of tables successfully");
    }
}
