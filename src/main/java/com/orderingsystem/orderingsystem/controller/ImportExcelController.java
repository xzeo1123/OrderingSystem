package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.response.ProductExcelImportResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.impl.ImportExcelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportExcelController {

    private final ImportExcelServiceImpl importService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<?> importProducts(@RequestParam("file") MultipartFile file) throws IOException, IOException {
        ProductExcelImportResponse summary = importService.importProductFromExcel(file);
        return ResponseHelper.ok(summary, "Excel imported successfully");
    }
}
