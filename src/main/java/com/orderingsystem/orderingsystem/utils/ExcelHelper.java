package com.orderingsystem.orderingsystem.utils;

import com.orderingsystem.orderingsystem.dto.request.ProductExcelImportRequest;
import com.orderingsystem.orderingsystem.entity.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@AllArgsConstructor
public class ExcelHelper {
    public List<ProductExcelImportRequest> productsFromExcel(MultipartFile file) throws IOException {
        List<ProductExcelImportRequest> rows = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            if (iterator.hasNext()) iterator.next();

            while (iterator.hasNext()) {
                Row row = iterator.next();
                ProductExcelImportRequest dto = new ProductExcelImportRequest();

                dto.setId((int) row.getCell(0).getNumericCellValue());
                dto.setName(row.getCell(1).getStringCellValue());
                dto.setImportPrice(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));
                dto.setSalePrice(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
                dto.setQuantity((int) row.getCell(4).getNumericCellValue());
                dto.setCategoryId((int) row.getCell(5).getNumericCellValue());

                rows.add(dto);
            }
        }
        return rows;
    }

    public ByteArrayInputStream productsToExcel(List<Products> products) throws IOException {
        String[] HEADERS = {
                "ID", "Name", "Import Price", "Sale Price",
                "Quantity", "Category ID", "Category Name",
                "Image URL", "Status"
        };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Products");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;
            for (Products p : products) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getImportPrice().doubleValue());
                row.createCell(3).setCellValue(p.getSalePrice().doubleValue());
                row.createCell(4).setCellValue(p.getQuantity());

                if (p.getCategory() != null) {
                    row.createCell(5).setCellValue(p.getCategory().getId());
                    row.createCell(6).setCellValue(p.getCategory().getName());
                } else {
                    row.createCell(5).setCellValue("");
                    row.createCell(6).setCellValue("");
                }

                row.createCell(7).setCellValue(p.getImageUrl());
                row.createCell(8).setCellValue(p.getStatus().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream categoriesToExcel(List<Categories> categories) throws IOException {
        String[] HEADERS = {
                "ID", "Name", "Description", "Status"
        };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Categories");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;
            for (Categories c : categories) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(c.getId());
                row.createCell(1).setCellValue(c.getName());
                row.createCell(2).setCellValue(c.getDescription());
                row.createCell(3).setCellValue(c.getStatus().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream usersToExcel(List<Users> users) throws IOException {
        String[] HEADERS = {
                "ID", "Username", "Point", "Role", "Status"
        };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;
            for (Users u : users) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(u.getId());
                row.createCell(1).setCellValue(u.getUsername());
                row.createCell(2).setCellValue(u.getPoint());
                row.createCell(3).setCellValue(u.getRole().name());
                row.createCell(4).setCellValue(u.getStatus().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream tablesToExcel(List<Tables> tables) throws IOException {
        String[] HEADERS = {
                "ID", "Number", "Status"
        };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Tables");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;
            for (Tables t : tables) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(t.getId());
                row.createCell(1).setCellValue(t.getNumber());
                row.createCell(2).setCellValue(t.getStatus().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream billsToExcel(List<Bills> bills) throws IOException {
        String[] HEADERS = {
                "Bill id", "total", "date create", "note", "status",
                "user id", "username",
                "table id", "table number",
                "detail id", "product id", "product name", "quantity"
        };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bills");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                headerRow.createCell(col).setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;

            for (Bills b : bills) {
                boolean firstDetail = true;

                for (BillDetails d : b.getDetails()) {
                    Row row = sheet.createRow(rowIdx++);
                    int c = 0;

                    if (firstDetail) {
                        row.createCell(c++).setCellValue(b.getId());
                        row.createCell(c++).setCellValue(b.getTotal().doubleValue());
                        row.createCell(c++).setCellValue(b.getDateCreate().toString());
                        row.createCell(c++).setCellValue(b.getNote());
                        row.createCell(c++).setCellValue(b.getStatus().name());

                        row.createCell(c++).setCellValue(b.getUser().getId());
                        row.createCell(c++).setCellValue(b.getUser().getUsername());

                        row.createCell(c++).setCellValue(b.getTable().getId());
                        row.createCell(c++).setCellValue(b.getTable().getNumber());

                        firstDetail = false;
                    } else {
                        c += 8;
                    }

                    row.createCell(c++).setCellValue(d.getId());
                    row.createCell(c++).setCellValue(d.getProduct() != null ? d.getProduct().getId() : null);
                    row.createCell(c++).setCellValue(d.getProduct() != null ? d.getProduct().getName() : "");
                    row.createCell(c++).setCellValue(d.getQuantity());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream receiptsToExcel(List<Receipts> receipts) throws IOException {
        String[] HEADERS = {
                "Receipt id", "total", "date create", "note", "status",
                "detail id", "product id", "product name", "quantity"
        };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Receipts");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                headerRow.createCell(col).setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;

            for (Receipts r : receipts) {
                boolean firstDetail = true;

                for (ReceiptDetails d : r.getDetails()) {
                    Row row = sheet.createRow(rowIdx++);
                    int c = 0;

                    if (firstDetail) {
                        row.createCell(c++).setCellValue(r.getId());
                        row.createCell(c++).setCellValue(r.getTotal().doubleValue());
                        row.createCell(c++).setCellValue(r.getDateCreate().toString());
                        row.createCell(c++).setCellValue(r.getNote());
                        row.createCell(c++).setCellValue(r.getStatus().name());

                        firstDetail = false;
                    } else {
                        c += 8;
                    }

                    row.createCell(c++).setCellValue(d.getId());
                    row.createCell(c++).setCellValue(d.getProduct() != null ? d.getProduct().getId() : null);
                    row.createCell(c++).setCellValue(d.getProduct() != null ? d.getProduct().getName() : "");
                    row.createCell(c++).setCellValue(d.getQuantity());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
