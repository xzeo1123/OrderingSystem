package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.config.CloudinaryProperties;
import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.BillDetailsRepository;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptDetailsRepository;
import com.orderingsystem.orderingsystem.service.ProductsService;
import com.orderingsystem.orderingsystem.mapping.ProductsMapper;
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
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;
    private final ProductsMapper productsMapper;

    /* ---------- CREATE ---------- */
    @Override
    public ProductsResponse createProduct(ProductsRequest request) {
        Categories category = categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + request.getCategoryId() + " not found"));

        request.setStatus(Status.ACTIVE);
        request.setQuantity(0);

        validate(request);

        if (request.getImageUrl() == null || request.getImageUrl().isBlank()) {
            request.setImageUrl(cloudinaryProperties.getUrl());
        }
        if (request.getImageId() == null || request.getImageId().isBlank()) {
            request.setImageId(cloudinaryProperties.getId());
        }

        Products product = productsMapper.toEntity(request);
        product.setCategory(category);

        return productsMapper.toResponse(productsRepository.save(product));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public ProductsResponse updateProduct(Integer id, ProductsRequest request) {
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

        Products updatedProduct = productsMapper.toEntity(request);
        updatedProduct.setId(product.getId());
        updatedProduct.setBillDetails(product.getBillDetails());
        updatedProduct.setReceiptDetails(product.getReceiptDetails());
        updatedProduct.setCategory(category);

        return productsMapper.toResponse(productsRepository.save(updatedProduct));
    }

    /* ---------- DELETE ---------- */
    @Override
    public ProductsResponse softDeleteProduct(Integer id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));

        product.setStatus(Status.INACTIVE);

        return productsMapper.toResponse(productsRepository.save(product));
    }

    @Override
    public void deleteProduct(Integer id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));

        if (product.getImageId() != null && !product.getImageId().equals(cloudinaryProperties.getId())) {
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
        return productsMapper.toResponse(productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found")));
    }

    @Override
    public List<ProductsResponse> getAllProducts() {
        return productsRepository.findAll()
                .stream()
                .map(productsMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- VALIDATE ---------- */
    private void validate(ProductsRequest request) {
        if (request.getSalePrice().compareTo(request.getImportPrice()) < 0) {
            throw new BusinessRuleException("Sale price must be greater than or equal to import price");
        }
    }
}
