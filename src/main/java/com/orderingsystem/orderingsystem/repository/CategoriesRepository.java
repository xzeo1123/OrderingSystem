package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Categories;
import com.orderingsystem.orderingsystem.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
    boolean existsByName(String name);
    Optional<Categories> findByName(String name);
}
