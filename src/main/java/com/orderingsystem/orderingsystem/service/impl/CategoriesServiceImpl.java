package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.CategoriesRequest;
import com.orderingsystem.orderingsystem.dto.response.CategoriesResponse;
import com.orderingsystem.orderingsystem.entity.Categories;
import com.orderingsystem.orderingsystem.entity.Products;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final ProductsRepository productsRepository;

    /* ---------- CREATE ---------- */
    @Override
    public CategoriesResponse createCategory(CategoriesRequest request) {
        request.setStatus((byte)1);

        validate(request);

        Categories category = new Categories();
        category.setName(request.getName());
        category.setStatus(request.getStatus());
        category.setDescription(request.getDescription());

        return toResponse(categoriesRepository.save(category));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public CategoriesResponse updateCategory(Integer id, CategoriesRequest request) {
        if (id == 1) {
            throw new RuntimeException("Cannot update this category (ID = 1)");
        }

        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        validate(request);

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus());

        return toResponse(categoriesRepository.save(category));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public CategoriesResponse softDeleteCategory(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot soft delete this category (ID = 1)");
        }

        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category " + id + " not found"));

        category.setStatus((byte)0);

        return toResponse(categoriesRepository.save(category));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteCategory(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot delete this category (ID = 1)");
        }

        if (!categoriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }

        List<Products> productsList = productsRepository.findByCategory_Id(id);
        for (Products p : productsList) {
            p.setCategory(categoriesRepository.getReferenceById(1));
        }
        productsRepository.saveAll(productsList);

        categoriesRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public CategoriesResponse getCategoryById(Integer id) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        return toResponse(category);
    }

    @Override
    public List<CategoriesResponse> getAllCategories() {
        return categoriesRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(CategoriesRequest request) {
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessRuleException("Status must be 0 (inactive) or 1 (active)");
        }
        if (categoriesRepository.existsByName(request.getName().trim())) {
            throw new BusinessRuleException("Category name already exists");
        }
    }

    private CategoriesResponse toResponse(Categories category) {
        return CategoriesResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .build();
    }
}
