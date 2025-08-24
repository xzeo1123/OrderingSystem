package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.config.CloudinaryProperties;
import com.orderingsystem.orderingsystem.dto.request.ProductExcelImportRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductExcelImportResponse;
import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptDetailsRepository;
import com.orderingsystem.orderingsystem.service.ImportExcelService;
import com.orderingsystem.orderingsystem.utils.ExcelHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ImportExcelServiceImpl implements ImportExcelService {

    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final ReceiptsRepository receiptRepository;
    private final ReceiptDetailsRepository receiptDetailsRepository;

    private final CloudinaryProperties cloudinaryProperties;

    @Autowired
    private final ExcelHelper excelHelper;

    public ProductExcelImportResponse importProductFromExcel(MultipartFile file) throws IOException {
        List<ProductExcelImportRequest> rows = excelHelper.readExcel(file);

        List<String> errors = validateRows(rows);
        if (!errors.isEmpty()) {
            throw new BusinessRuleException("Import data invalid: " + String.join(", ", errors));
        }

        int created = 0;
        int updated = 0;
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        Receipts receipt = new Receipts();
        receipt.setDateCreate(LocalDateTime.now());
        receipt.setNote("Import from Excel");
        receipt.setStatus(Status.ACTIVE);

        List<ReceiptDetails> receiptDetails = new ArrayList<>();

        for (ProductExcelImportRequest row : rows) {
            Products targetProduct;

            if (row.getId() == 0) {
                targetProduct = createNewProduct(row);
                productsRepository.save(targetProduct);
                created++;
            } else {
                Optional<Products> optProduct = productsRepository.findById(row.getId());

                if (optProduct.isPresent()) {
                    Products existing = optProduct.get();
                    boolean samePrice = row.getImportPrice() != null && row.getSalePrice() != null
                            && existing.getImportPrice().compareTo(row.getImportPrice()) == 0
                            && existing.getSalePrice().compareTo(row.getSalePrice()) == 0;

                    if (samePrice) {
                        updateExistingProduct(existing, row);
                        productsRepository.save(existing);
                        updated++;
                        targetProduct = existing;
                    } else {
                        targetProduct = createNewProduct(row);
                        productsRepository.save(targetProduct);
                        created++;
                    }
                } else {
                    targetProduct = createNewProduct(row);
                    productsRepository.save(targetProduct);
                    created++;
                }
            }

            totalQuantity += row.getQuantity();
            totalAmount = totalAmount.add(targetProduct.getImportPrice()
                    .multiply(BigDecimal.valueOf(row.getQuantity())));

            ReceiptDetails detail = new ReceiptDetails();
            detail.setProduct(targetProduct);
            detail.setQuantity(row.getQuantity());
            detail.setReceipt(receipt);
            receiptDetails.add(detail);
        }

        receipt.setTotal(totalAmount);
        receiptRepository.save(receipt);

        for (ReceiptDetails detail : receiptDetails) {
            receiptDetailsRepository.save(detail);
        }

        return new ProductExcelImportResponse(created, updated, totalQuantity, totalAmount);
    }

    private void updateExistingProduct(Products existing, ProductExcelImportRequest row) {
        if (row.getName() != null && !row.getName().isBlank()) {
            existing.setName(row.getName());
        }
        if (row.getCategoryId() != null) {
            Categories cat = categoriesRepository.findById(row.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category " + row.getCategoryId() + " not found"));
            existing.setCategory(cat);
        }
        if (row.getQuantity() != null && row.getQuantity() > 0) {
            existing.setQuantity(existing.getQuantity() + row.getQuantity());
        }
    }

    private Products createNewProduct(ProductExcelImportRequest row) {
        Categories category = categoriesRepository.findById(row.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category " + row.getCategoryId() + " not found"));

        Products product = new Products();
        product.setName(row.getName());
        product.setImportPrice(row.getImportPrice());
        product.setSalePrice(row.getSalePrice());
        product.setQuantity(row.getQuantity());
        product.setStatus(Status.ACTIVE);
        product.setCategory(category);

        product.setImageUrl(cloudinaryProperties.getUrl());
        product.setImageId(cloudinaryProperties.getId());

        return product;
    }

    private List<String> validateRows(List<ProductExcelImportRequest> rows) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            ProductExcelImportRequest row = rows.get(i);

            if (row.getId() == null || row.getId() == 0) {
                if (row.getName() == null || row.getName().isBlank())
                    errors.add("Row " + (i+1) + " missing name");
                if (row.getImportPrice() == null)
                    errors.add("Row " + (i+1) + " missing importPrice");
                if (row.getSalePrice() == null)
                    errors.add("Row " + (i+1) + " missing salePrice");
                if (row.getQuantity() == null || row.getQuantity() <= 0)
                    errors.add("Row " + (i+1) + " missing quantity");
                if (row.getCategoryId() == null)
                    errors.add("Row " + (i+1) + " missing categoryId");
            } else {
                errors.add("Row " + (i+1) + " has invalid id");
            }
        }

        return errors;
    }
}
