package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
    List<Products> findByCategory_Id(Integer categoryId);
    List<Products> findByNameContainingIgnoreCase(String productName);
    List<Products> findBySalePriceBetween(Double minPrice, Double maxPrice);
    @Query("""
    SELECT p FROM Products p
    WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:minPrice IS NULL OR p.salePrice >= :minPrice)
      AND (:maxPrice IS NULL OR p.salePrice <= :maxPrice)
    """)
    List<Products> searchProducts(
            @Param("name") String productName,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

}
