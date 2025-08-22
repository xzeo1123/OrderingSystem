package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.CategoriesRequest;
import com.orderingsystem.orderingsystem.dto.response.CategoriesResponse;

import java.util.List;

public interface CategoriesService {
    CategoriesResponse createCategory(CategoriesRequest categoriesRequest);
    CategoriesResponse updateCategory(Integer categoryId, CategoriesRequest categoriesRequest);
    CategoriesResponse softDeleteCategory(Integer categoryId);
    void deleteCategory(Integer categoryId);
    CategoriesResponse getCategoryById(Integer categoryId);
    List<CategoriesResponse> getAllCategories();
    List<CategoriesResponse> searchCategoriesByName(String categoryName);
}
