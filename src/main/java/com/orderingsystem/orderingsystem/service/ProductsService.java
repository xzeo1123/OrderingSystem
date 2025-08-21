package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductsService {
    ProductsResponse createProduct(ProductsRequest request);
    ProductsResponse updateProduct(Integer id, ProductsRequest request);
    ProductsResponse softDeleteProduct(Integer id);
    void deleteProduct(Integer id);
    ProductsResponse getProductById(Integer id);
    Page<ProductsResponse> getAllProducts(Integer page, Integer size);
}
