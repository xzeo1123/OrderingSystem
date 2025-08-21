package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.ProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductsController {

    private final ProductsService productsService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductsRequest request) {
        ProductsResponse created = productsService.createProduct(request);
        return ResponseHelper.created(created, "Product created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductsRequest request) {
        ProductsResponse updated = productsService.updateProduct(id, request);
        return ResponseHelper.ok(updated, "Product updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteProduct(@PathVariable Integer id) {
        ProductsResponse updated = productsService.softDeleteProduct(id);
        return ResponseHelper.ok(updated, "Product soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productsService.deleteProduct(id);
        return ResponseHelper.deleted("Product deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        ProductsResponse product = productsService.getProductById(id);
        return ResponseHelper.ok(product, "Get product by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam Integer page,
            @RequestParam Integer size) {

        if (page < 0) page = 0;
        if (size > 20) size = 20;

        Page<ProductsResponse> products = productsService.getAllProducts(page, size);
        return ResponseHelper.ok(products, "Get list of products successfully");
    }
}
