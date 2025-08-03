package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
    List<Products> findByCategory_Id(Integer categoryId);
}
