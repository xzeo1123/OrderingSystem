package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.CategoriesRequest;
import com.orderingsystem.orderingsystem.dto.response.CategoriesResponse;
import com.orderingsystem.orderingsystem.entity.Categories;
import com.orderingsystem.orderingsystem.entity.Status;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.CategoriesMapper;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames = "categories")
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    /* ---------- CREATE ---------- */
    @Override
    @CacheEvict(allEntries = true)
    public CategoriesResponse createCategory(CategoriesRequest categoriesRequest) {
        categoriesRequest.setStatus(Status.ACTIVE);

        validate(categoriesRequest);

        Categories category = categoriesMapper.toEntity(categoriesRequest);
        return categoriesMapper.toResponse(categoriesRepository.save(category));
    }

    /* ---------- UPDATE ---------- */
    @Override
    @CachePut(key = "#categoryId")
    @CacheEvict(allEntries = true)
    public CategoriesResponse updateCategory(Integer categoryId, CategoriesRequest categoriesRequest) {
        Categories category = categoriesRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));

        validate(categoriesRequest);

        Categories updatedCategory = categoriesMapper.toEntity(categoriesRequest);
        updatedCategory.setId(category.getId());

        return categoriesMapper.toResponse(categoriesRepository.save(category));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    @CacheEvict(key = "#categoryId", allEntries = true)
    public CategoriesResponse softDeleteCategory(Integer categoryId) {
        Categories category = categoriesRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category " + categoryId + " not found"));

        category.setStatus(Status.INACTIVE);

        return categoriesMapper.toResponse(categoriesRepository.save(category));
    }

    /* ---------- DELETE ---------- */
    @Override
    @CacheEvict(key = "#categoryId", allEntries = true)
    public void deleteCategory(Integer categoryId) {
        if (!categoriesRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category with id " + categoryId + " not found");
        }

        categoriesRepository.deleteById(categoryId);
    }

    /* ---------- READ ---------- */
    @Override
    @Cacheable(key = "#categoryId")
    public CategoriesResponse getCategoryById(Integer categoryId) {
        Categories category = categoriesRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));
        return categoriesMapper.toResponse(category);
    }

    @Override
    @Cacheable(value = "categories_list")
    public List<CategoriesResponse> getAllCategories() {
        return categoriesRepository.findAll()
            .stream()
            .map(categoriesMapper::toResponse)
            .toList();
    }

    @Override
    public List<CategoriesResponse> searchCategoriesByName(String categoryName) {
        return categoriesRepository.findByNameContainingIgnoreCase(categoryName)
                .stream()
                .map(categoriesMapper::toResponse)
                .toList();
    }


    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(CategoriesRequest categoriesRequest) {
        if (categoriesRepository.existsByName(categoriesRequest.getName().trim())) {
            throw new BusinessRuleException("Category name already exists");
        }
    }
}
