package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.CategoriesRequest;
import com.orderingsystem.orderingsystem.dto.response.CategoriesResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.CategoriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@CrossOrigin
public class CategoriesController {

    private final CategoriesService categoriesService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoriesRequest categoriesRequest) {
        CategoriesResponse created = categoriesService.createCategory(categoriesRequest);
        return ResponseHelper.created(created, "Category created successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId, @RequestBody @Valid CategoriesRequest categoriesRequest) {
        CategoriesResponse updated = categoriesService.updateCategory(categoryId, categoriesRequest);
        return ResponseHelper.ok(updated, "Category updated successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteCategory(@PathVariable Integer categoryId) {
        CategoriesResponse updated = categoriesService.softDeleteCategory(categoryId);
        return ResponseHelper.ok(updated, "Category soft deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId) {
        categoriesService.deleteCategory(categoryId);
        return ResponseHelper.deleted("Category deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategorieById(@PathVariable Integer categoryId) {
        CategoriesResponse category = categoriesService.getCategoryById(categoryId);
        return ResponseHelper.ok(category, "Get category by ID successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoriesResponse> categories = categoriesService.getAllCategories();
        return ResponseHelper.ok(categories, "Get list of categories successfully");
    }

    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<?> searchCategoriesByName(@RequestParam String categoryName) {
        List<CategoriesResponse> categories = categoriesService.searchCategoriesByName(categoryName);
        return ResponseHelper.ok(categories, "Search categories successfully");
    }
}
