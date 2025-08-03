package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.config.CloudinaryProperties;
import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import com.orderingsystem.orderingsystem.entity.BillDetails;
import com.orderingsystem.orderingsystem.entity.Categories;
import com.orderingsystem.orderingsystem.entity.Products;
import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.BillDetailsRepository;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptDetailsRepository;
import com.orderingsystem.orderingsystem.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final BillDetailsRepository billDetailsRepository;
    private final ReceiptDetailsRepository receiptDetailsRepository;
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;

    /* ---------- CREATE ---------- */
    @Override
    public ProductsResponse createProduct(ProductsRequest request) {
        Categories category = categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + request.getCategoryId() + " not found"));

        request.setStatus((byte) 1);
        request.setQuantity(0);

        validate(request);

        if (request.getImageUrl() == null || request.getImageUrl().isBlank()) {
            request.setImageUrl(cloudinaryProperties.getUrl());
        }
        if (request.getImageId() == null || request.getImageId().isBlank()) {
            request.setImageId(cloudinaryProperties.getId());
        }

        Products product = new Products();
        product.setName(request.getName());
        product.setImageUrl(request.getImageUrl());
        product.setImageId(request.getImageId());
        product.setImportPrice(request.getImportPrice());
        product.setSalePrice(request.getSalePrice());
        product.setQuantity(request.getQuantity());
        product.setStatus(request.getStatus());
        product.setCategory(category);

        return toResponse(productsRepository.save(product));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public ProductsResponse updateProduct(Integer id, ProductsRequest request) {
        if (id == 1) {
            throw new RuntimeException("Cannot update this product (ID = 1)");
        }

        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));

        Categories category = categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + request.getCategoryId() + " not found"));

        validate(request);

        if (request.getImageId() == null || request.getImageId().isBlank()) {
            request.setImageId(product.getImageId() != null ? product.getImageId() : cloudinaryProperties.getId());
        }
        if (request.getImageUrl() == null || request.getImageUrl().isBlank()) {
            request.setImageUrl(product.getImageUrl() != null ? product.getImageUrl() : cloudinaryProperties.getUrl());
        }

        if (product.getImageId() != null &&
                request.getImageId() != null &&
                !request.getImageId().equals(product.getImageId()) &&
                !product.getImageId().equals(cloudinaryProperties.getId())) {
            try {
                cloudinaryService.deleteImage(product.getImageId());
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete old image from Cloudinary");
            }
        }

        product.setName(request.getName());
        product.setImageUrl(request.getImageUrl());
        product.setImageId(request.getImageId());
        product.setImportPrice(request.getImportPrice());
        product.setSalePrice(request.getSalePrice());
        product.setQuantity(request.getQuantity());
        product.setStatus(request.getStatus());
        product.setCategory(category);

        return toResponse(productsRepository.save(product));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public ProductsResponse softDeleteProduct(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot soft delete this product (ID = 1)");
        }

        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));

        product.setStatus((byte)0);

        return toResponse(productsRepository.save(product));
    }

    /* ---------- DELETE ---------- */
    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot delete this product (ID = 1)");
        }

        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));

        List<BillDetails> billDetailsList = billDetailsRepository.findByProduct_Id(id);
        for (BillDetails bd : billDetailsList) {
            bd.setProduct(productsRepository.getReferenceById(1));
        }
        billDetailsRepository.saveAll(billDetailsList);

        List<ReceiptDetails> receiptDetailsList = receiptDetailsRepository.findByProduct_Id(id);
        for (ReceiptDetails rd : receiptDetailsList) {
            rd.setProduct(productsRepository.getReferenceById(1));
        }
        receiptDetailsRepository.saveAll(receiptDetailsList);

        if (product.getImageId() != null && !product.getImageId().equals(cloudinaryProperties.getId())) { // ✅ KHÔNG XÓA ẢNH MẶC ĐỊNH
            try {
                cloudinaryService.deleteImage(product.getImageId());
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete image from Cloudinary");
            }
        }

        productsRepository.delete(product);
    }

    /* ---------- READ ---------- */
    @Override
    public ProductsResponse getProductById(Integer id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));
        return toResponse(product);
    }

    @Override
    public List<ProductsResponse> getAllProducts() {
        return productsRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(ProductsRequest request) {
        if (request.getSalePrice().compareTo(request.getImportPrice()) < 0) {
            throw new BusinessRuleException("Sale price must be greater than or equal to import price");
        }
        if (request.getQuantity() < 0) {
            throw new BusinessRuleException("Quantity cannot be negative");
        }
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessRuleException("Status must be 0 (inactive) or 1 (active)");
        }
    }

    private ProductsResponse toResponse(Products product) {
        return ProductsResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .importPrice(product.getImportPrice())
                .salePrice(product.getSalePrice())
                .quantity(product.getQuantity())
                .status(product.getStatus())
                .categoryId(product.getCategory().getId())
                .build();
    }
}
