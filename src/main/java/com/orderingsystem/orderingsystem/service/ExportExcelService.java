package com.orderingsystem.orderingsystem.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ExportExcelService {
    ResponseEntity<InputStreamResource> exportProducts() throws IOException;
    ResponseEntity<InputStreamResource> exportCategories() throws IOException;
    ResponseEntity<InputStreamResource> exportUsers() throws IOException;
    ResponseEntity<InputStreamResource> exportTables() throws IOException;
    ResponseEntity<InputStreamResource> exportBills() throws IOException;
    ResponseEntity<InputStreamResource> exportReceipts() throws IOException;
}
