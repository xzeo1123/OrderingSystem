package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductsService {
    ProductsResponse createProduct(ProductsRequest productsRequest);
    ProductsResponse updateProduct(Integer productId, ProductsRequest productsRequest);
    ProductsResponse softDeleteProduct(Integer productId);
    void deleteProduct(Integer productId);
    ProductsResponse getProductById(Integer productId);
    Page<ProductsResponse> getAllProducts(Integer page, Integer size);
    List<ProductsResponse> searchProductsByName(String name);
    List<ProductsResponse> getProductsByCategory(Integer categoryId);
    List<ProductsResponse> getProductsByPriceRange(Double minPrice, Double maxPrice);
    List<ProductsResponse> searchProducts(String name, Integer categoryId, Double minPrice, Double maxPrice);
}
