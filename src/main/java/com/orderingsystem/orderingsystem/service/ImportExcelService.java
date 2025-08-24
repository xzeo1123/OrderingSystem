package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.response.ProductExcelImportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportExcelService {
    ProductExcelImportResponse importProductFromExcel(MultipartFile file) throws IOException;
}
