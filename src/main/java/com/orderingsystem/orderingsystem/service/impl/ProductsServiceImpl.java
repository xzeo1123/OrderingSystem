package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.config.CloudinaryProperties;
import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.service.CloudinaryService;
import com.orderingsystem.orderingsystem.service.ProductsService;
import com.orderingsystem.orderingsystem.mapping.ProductsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public ProductsResponse createProduct(ProductsRequest productsRequest) {
        Categories category = categoriesRepository.findById(productsRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + productsRequest.getCategoryId() + " not found"));

        productsRequest.setStatus(Status.ACTIVE);
        productsRequest.setQuantity(0);

        validate(productsRequest);

        if (productsRequest.getImageUrl() == null || productsRequest.getImageUrl().isBlank()) {
            productsRequest.setImageUrl(cloudinaryProperties.getUrl());
        }
        if (productsRequest.getImageId() == null || productsRequest.getImageId().isBlank()) {
            productsRequest.setImageId(cloudinaryProperties.getId());
        }

        Products product = productsMapper.toEntity(productsRequest);
        product.setCategory(category);

        return productsMapper.toResponse(productsRepository.save(product));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public ProductsResponse updateProduct(Integer productId, ProductsRequest productsRequest) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + productId + " not found"));

        Categories category = categoriesRepository.findById(productsRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + productsRequest.getCategoryId() + " not found"));

        validate(productsRequest);

        if (productsRequest.getImageId() == null || productsRequest.getImageId().isBlank()) {
            productsRequest.setImageId(product.getImageId() != null ? product.getImageId() : cloudinaryProperties.getId());
        }
        if (productsRequest.getImageUrl() == null || productsRequest.getImageUrl().isBlank()) {
            productsRequest.setImageUrl(product.getImageUrl() != null ? product.getImageUrl() : cloudinaryProperties.getUrl());
        }

        if (product.getImageId() != null &&
                productsRequest.getImageId() != null &&
                !productsRequest.getImageId().equals(product.getImageId()) &&
                !product.getImageId().equals(cloudinaryProperties.getId())) {
            try {
                cloudinaryService.deleteImage(product.getImageId());
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete old image from Cloudinary");
            }
        }

        Products updatedProduct = productsMapper.toEntity(productsRequest);
        updatedProduct.setId(product.getId());
        updatedProduct.setBillDetails(product.getBillDetails());
        updatedProduct.setReceiptDetails(product.getReceiptDetails());
        updatedProduct.setCategory(category);

        return productsMapper.toResponse(productsRepository.save(updatedProduct));
    }

    /* ---------- DELETE ---------- */
    @Override
    public ProductsResponse softDeleteProduct(Integer productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + productId + " not found"));

        product.setStatus(Status.INACTIVE);

        return productsMapper.toResponse(productsRepository.save(product));
    }

    @Override
    public void deleteProduct(Integer productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + productId + " not found"));

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
    public ProductsResponse getProductById(Integer productId) {
        return productsMapper.toResponse(productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + productId + " not found")));
    }

    @Override
    public Page<ProductsResponse> getAllProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return productsRepository.findAll(pageable)
                .map(productsMapper::toResponse);
    }

    @Override
    public List<ProductsResponse> searchProductsByName(String productName) {
        return productsRepository.findByNameContainingIgnoreCase(productName)
                .stream()
                .map(productsMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductsResponse> getProductsByCategory(Integer categoryId) {
        return productsRepository.findByCategory_Id(categoryId)
                .stream()
                .map(productsMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductsResponse> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productsRepository.findBySalePriceBetween(minPrice, maxPrice)
                .stream()
                .map(productsMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductsResponse> searchProducts(String name, Integer categoryId, Double minPrice, Double maxPrice) {
        return productsRepository.searchProducts(name, categoryId, minPrice, maxPrice)
                .stream()
                .map(productsMapper::toResponse)
                .toList();
    }


    /* ---------- VALIDATE ---------- */
    private void validate(ProductsRequest productsRequest) {
        if (productsRequest.getSalePrice().compareTo(productsRequest.getImportPrice()) < 0) {
            throw new BusinessRuleException("Sale price must be greater than or equal to import price");
        }
    }
}
