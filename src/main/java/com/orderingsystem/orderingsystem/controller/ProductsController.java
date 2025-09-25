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
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductsRequest productsRequest) {
        ProductsResponse created = productsService.createProduct(productsRequest);
        return ResponseHelper.created(created, "Product created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductsRequest productsRequest) {
        ProductsResponse updated = productsService.updateProduct(productId, productsRequest);
        return ResponseHelper.ok(updated, "Product updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{productId}")
    public ResponseEntity<?> softDeleteProduct(@PathVariable Integer productId) {
        ProductsResponse updated = productsService.softDeleteProduct(productId);
        return ResponseHelper.ok(updated, "Product soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productsService.deleteProduct(productId);
        return ResponseHelper.deleted("Product deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Integer productId) {
        ProductsResponse product = productsService.getProductById(productId);
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

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/searchbyname")
    public ResponseEntity<?> searchProductsByName(@RequestParam String productName) {
        List<ProductsResponse> products = productsService.searchProductsByName(productName);
        return ResponseHelper.ok(products, "Search products successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/category")
    public ResponseEntity<?> getProductsByCategory(@RequestParam Integer categoryId) {
        List<ProductsResponse> products = productsService.getProductsByCategory(categoryId);
        return ResponseHelper.ok(products, "Get products by category successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/filter/price")
    public ResponseEntity<?> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductsResponse> products = productsService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseHelper.ok(products, "Get products by price range successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<ProductsResponse> products = productsService.searchProducts(productName, categoryId, minPrice, maxPrice);
        return ResponseHelper.ok(products, "Search products successfully");
    }

}
