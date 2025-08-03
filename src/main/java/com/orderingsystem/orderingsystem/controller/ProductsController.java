package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.ProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductsController {

    private final ProductsService productsService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductsRequest request) {
        ProductsResponse created = productsService.createProduct(request);
        return ResponseHelper.created(created, "Product created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductsRequest request) {
        ProductsResponse updated = productsService.updateProduct(id, request);
        return ResponseHelper.ok(updated, "Product updated successfully");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteProduct(@PathVariable Integer id) {
        ProductsResponse updated = productsService.softDeleteProduct(id);
        return ResponseHelper.ok(updated, "Product soft deleted successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productsService.deleteProduct(id);
        return ResponseHelper.deleted("Product deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        ProductsResponse product = productsService.getProductById(id);
        return ResponseHelper.ok(product, "Get product by ID successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductsResponse> products = productsService.getAllProducts();
        return ResponseHelper.ok(products, "Get list of products successfully");
    }
}
