package com.orderingsystem.orderingsystem.utils;

import com.orderingsystem.orderingsystem.dto.request.ProductExcelImportRequest;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@AllArgsConstructor
public class ExcelHelper {
    public List<ProductExcelImportRequest> readExcel(MultipartFile file) throws IOException {
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
}
