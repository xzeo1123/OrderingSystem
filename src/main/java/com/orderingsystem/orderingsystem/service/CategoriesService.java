package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.CategoriesRequest;
import com.orderingsystem.orderingsystem.dto.response.CategoriesResponse;

import java.util.List;

public interface CategoriesService {
    CategoriesResponse createCategory(CategoriesRequest request);
    CategoriesResponse updateCategory(Integer id, CategoriesRequest request);
    CategoriesResponse softDeleteCategory(Integer id);
    void deleteCategory(Integer id);
    CategoriesResponse getCategoryById(Integer id);
    List<CategoriesResponse> getAllCategories();
}
