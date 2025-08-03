package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.CategoriesRequest;
import com.orderingsystem.orderingsystem.dto.response.CategoriesResponse;
import com.orderingsystem.orderingsystem.dto.response.ResponseHelper;
import com.orderingsystem.orderingsystem.service.CategoriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@CrossOrigin
public class CategoriesController {

    private final CategoriesService categoriesService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoriesRequest request) {
        CategoriesResponse created = categoriesService.createCategory(request);
        return ResponseHelper.created(created, "Category created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody @Valid CategoriesRequest request) {
        CategoriesResponse updated = categoriesService.updateCategory(id, request);
        return ResponseHelper.ok(updated, "Category updated successfully");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteCategory(@PathVariable Integer id) {
        CategoriesResponse updated = categoriesService.softDeleteCategory(id);
        return ResponseHelper.ok(updated, "Category soft deleted successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        categoriesService.deleteCategory(id);
        return ResponseHelper.deleted("Category deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategorieById(@PathVariable Integer id) {
        CategoriesResponse category = categoriesService.getCategoryById(id);
        return ResponseHelper.ok(category, "Get category by ID successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoriesResponse> categories = categoriesService.getAllCategories();
        return ResponseHelper.ok(categories, "Get list of categories successfully");
    }
}
