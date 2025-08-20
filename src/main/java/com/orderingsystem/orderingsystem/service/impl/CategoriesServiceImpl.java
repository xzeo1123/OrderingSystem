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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    /* ---------- CREATE ---------- */
    @Override
    public CategoriesResponse createCategory(CategoriesRequest request) {
        request.setStatus(Status.ACTIVE);

        validate(request);

        Categories category = categoriesMapper.toEntity(request);
        return categoriesMapper.toResponse(categoriesRepository.save(category));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public CategoriesResponse updateCategory(Integer id, CategoriesRequest request) {
        Categories category = categoriesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        validate(request);

        Categories updatedCategory = categoriesMapper.toEntity(request);
        updatedCategory.setId(category.getId());

        return categoriesMapper.toResponse(categoriesRepository.save(category));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public CategoriesResponse softDeleteCategory(Integer id) {
        Categories category = categoriesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category " + id + " not found"));

        category.setStatus(Status.INACTIVE);

        return categoriesMapper.toResponse(categoriesRepository.save(category));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteCategory(Integer id) {
        if (!categoriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }

        categoriesRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public CategoriesResponse getCategoryById(Integer id) {
        Categories category = categoriesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        return categoriesMapper.toResponse(category);
    }

    @Override
    public List<CategoriesResponse> getAllCategories() {
        return categoriesRepository.findAll()
            .stream()
            .map(categoriesMapper::toResponse)
            .toList();
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(CategoriesRequest request) {
        if (categoriesRepository.existsByName(request.getName().trim())) {
            throw new BusinessRuleException("Category name already exists");
        }
    }
}
