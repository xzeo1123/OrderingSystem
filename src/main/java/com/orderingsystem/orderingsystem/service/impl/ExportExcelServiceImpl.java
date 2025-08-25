package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.repository.*;
import com.orderingsystem.orderingsystem.service.ExportExcelService;
import com.orderingsystem.orderingsystem.utils.ExcelHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportExcelServiceImpl implements ExportExcelService {

    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final TablesRepository tablesRepository;
    private final BillsRepository billsRepository;
    private final ReceiptsRepository receiptsRepository;
    private final ExcelHelper excelHelper;

    @Override
    public ResponseEntity<InputStreamResource> exportProducts() throws IOException {
        List<Products> products = productsRepository.findAll();
        ByteArrayInputStream in = excelHelper.productsToExcel(products);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @Override
    public ResponseEntity<InputStreamResource> exportCategories() throws IOException {
        List<Categories> categories = categoriesRepository.findAll();
        ByteArrayInputStream in = excelHelper.categoriesToExcel(categories);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @Override
    public ResponseEntity<InputStreamResource> exportUsers() throws IOException {
        List<Users> users = usersRepository.findAll();
        ByteArrayInputStream in = excelHelper.usersToExcel(users);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @Override
    public ResponseEntity<InputStreamResource> exportTables() throws IOException {
        List<Tables> tables = tablesRepository.findAll();
        ByteArrayInputStream in = excelHelper.tablesToExcel(tables);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @Override
    public ResponseEntity<InputStreamResource> exportBills() throws IOException {
        List<Bills> bills = billsRepository.findAll();
        ByteArrayInputStream in = excelHelper.billsToExcel(bills);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @Override
    public ResponseEntity<InputStreamResource> exportReceipts() throws IOException {
        List<Receipts> receipts = receiptsRepository.findAll();
        ByteArrayInputStream in = excelHelper.receiptsToExcel(receipts);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
