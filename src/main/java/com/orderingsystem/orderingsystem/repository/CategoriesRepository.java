package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
    boolean existsByName(String name);
}
