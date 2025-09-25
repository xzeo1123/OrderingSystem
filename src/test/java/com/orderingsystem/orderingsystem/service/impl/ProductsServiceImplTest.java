package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.config.CloudinaryProperties;
import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.ProductsMapper;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductsServiceImplTest {

    @Mock
    private ProductsRepository productsRepository;
    @Mock
    private CategoriesRepository categoriesRepository;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private CloudinaryProperties cloudinaryProperties;
    @Mock
    private ProductsMapper productsMapper;

    @InjectMocks
    private ProductsServiceImpl productsService;

    private Categories category;
    private ProductsRequest request;
    private Products product;
    private ProductsResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        int testCategoryId = 1;
        String testCategoryName = "test category name";
        String testCategoryDescription = "test_category_description";
        Status testCategoryStatus = Status.ACTIVE;

        category = new Categories();
        category.setId(testCategoryId);
        category.setName(testCategoryName);
        category.setDescription(testCategoryDescription);
        category.setStatus(testCategoryStatus);

        String testProductRequestName = "test product name";
        String testProductRequestImageUrl = "test product image url";
        String testProductRequestImageId = "test product image id";
        BigDecimal testProductRequestImportPrice = BigDecimal.valueOf(100);
        BigDecimal testProductRequestSalePrice = BigDecimal.valueOf(150);
        int testProductRequestQuantity = 1;
        Status testProductRequestStatus = Status.ACTIVE;

        request = new ProductsRequest();
        request.setName(testProductRequestName);
        request.setImageUrl(testProductRequestImageUrl);
        request.setImageId(testProductRequestImageId);
        request.setImportPrice(testProductRequestImportPrice);
        request.setSalePrice(testProductRequestSalePrice);
        request.setStatus(testProductRequestStatus);
        request.setQuantity(testProductRequestQuantity);
        request.setCategoryId(category.getId());

        int testProductId = 1;
        String testProductName = "test product name";
        String testProductImageUrl = "test product image url";
        String testProductImageId = "test product image id";
        BigDecimal testProductImportPrice = BigDecimal.valueOf(100);
        BigDecimal testProductSalePrice = BigDecimal.valueOf(150);
        int testProductQuantity = 1;
        Status testProductStatus = Status.ACTIVE;

        product = new Products();
        product.setId(testProductId);
        product.setName(testProductName);
        product.setImageUrl(testProductImageUrl);
        product.setImageId(testProductImageId);
        product.setImportPrice(testProductImportPrice);
        product.setSalePrice(testProductSalePrice);
        product.setQuantity(testProductQuantity);
        product.setStatus(testProductStatus);
        product.setCategory(category);

        response = new ProductsResponse();
        response.setId(1);

        when(productsMapper.toEntity(any())).thenReturn(product);
        when(productsMapper.toResponse(any())).thenReturn(response);

        when(cloudinaryProperties.getId()).thenReturn("default-id");
        when(cloudinaryProperties.getUrl()).thenReturn("default-url");
    }

    /* ---------- CREATE ---------- */
    @Test
    void createProduct_CategoryNotFound() {
        when(categoriesRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productsService.createProduct(request));
    }

    @Test
    void createProduct_ValidateFail() {
        request.setSalePrice(BigDecimal.valueOf(50));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        assertThrows(BusinessRuleException.class, () -> productsService.createProduct(request));
    }

    @Test
    void createProduct_DefaultBothImage() {
        request.setImageId(null);
        request.setImageUrl(null);
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        ProductsResponse res = productsService.createProduct(request);
        assertEquals(response.getId(), res.getId());
        assertEquals("default-id", request.getImageId());
        assertEquals("default-url", request.getImageUrl());
    }

    @Test
    void createProduct_DefaultUrlOnly() {
        request.setImageId("custom-id");
        request.setImageUrl(null);
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        productsService.createProduct(request);
        assertEquals("custom-id", request.getImageId());
        assertEquals("default-url", request.getImageUrl());
    }

    @Test
    void createProduct_DefaultIdOnly() {
        request.setImageId(null);
        request.setImageUrl("custom-url");
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        productsService.createProduct(request);
        assertEquals("default-id", request.getImageId());
        assertEquals("custom-url", request.getImageUrl());
    }

    @Test
    void createProduct_KeepProvidedImage() {
        request.setImageId("custom-id");
        request.setImageUrl("custom-url");
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        productsService.createProduct(request);
        assertEquals("custom-id", request.getImageId());
        assertEquals("custom-url", request.getImageUrl());
    }

    /* ---------- UPDATE ---------- */
    @Test
    void updateProduct_ProductNotFound() {
        when(productsRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productsService.updateProduct(1, request));
    }

    @Test
    void updateProduct_CategoryNotFound() {
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productsService.updateProduct(1, request));
    }

    @Test
    void updateProduct_ValidateFail() {
        request.setSalePrice(BigDecimal.valueOf(50));
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        assertThrows(BusinessRuleException.class, () -> productsService.updateProduct(1, request));
    }

    @Test
    void updateProduct_ImageIdNull_KeepExisting() {
        product.setImageId("img1");
        request.setImageId(null);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        ProductsResponse res = productsService.updateProduct(1, request);
        assertEquals(response.getId(), res.getId());
    }

    @Test
    void updateProduct_ImageUrlNull_KeepExisting() {
        product.setImageUrl("url1");
        request.setImageUrl(null);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        productsService.updateProduct(1, request);
        assertEquals("url1", request.getImageUrl());
    }

    @Test
    void updateProduct_ImageNull_SetDefault() {
        product.setImageId(null);
        product.setImageUrl(null);
        request.setImageId(null);
        request.setImageUrl(null);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        productsService.updateProduct(1, request);
        assertEquals("default-id", request.getImageId());
        assertEquals("default-url", request.getImageUrl());
    }

    @Test
    void updateProduct_ChangeImageId_DeleteOld() {
        product.setImageId("old-id");
        request.setImageId("new-id");
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        productsService.updateProduct(1, request);
        verify(cloudinaryService).deleteImage("old-id");
    }

    @Test
    void updateProduct_ChangeImageId_OldIsDefault_DoNotDelete() {
        product.setImageId("default-id");
        request.setImageId("new-id");
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        productsService.updateProduct(1, request);
        verify(cloudinaryService, never()).deleteImage(any());
    }

    @Test
    void updateProduct_DeleteImageFails_RuntimeException() {
        product.setImageId("old-id");
        request.setImageId("new-id");
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        doThrow(new RuntimeException("fail")).when(cloudinaryService).deleteImage("old-id");

        assertThrows(RuntimeException.class, () -> productsService.updateProduct(1, request));
    }

    @Test
    void updateProduct_HappyPath() {
        request.setImageId("same-id");
        product.setImageId("same-id");
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        ProductsResponse res = productsService.updateProduct(1, request);
        assertEquals(1, res.getId());
    }

    /* ---------- SOFT DELETE ---------- */
    @Test
    void softDeleteProduct_NotFound() {
        when(productsRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productsService.softDeleteProduct(1));
    }

    @Test
    void softDeleteProduct_Success() {
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(productsRepository.save(any())).thenReturn(product);

        ProductsResponse res = productsService.softDeleteProduct(1);
        assertEquals(1, res.getId());
        assertEquals(Status.INACTIVE, product.getStatus());
    }

    /* ---------- DELETE ---------- */
    @Test
    void deleteProduct_NotFound() {
        when(productsRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productsService.deleteProduct(1));
    }

    @Test
    void deleteProduct_ImageIsDefault() {
        product.setImageId("default-id");
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        productsService.deleteProduct(1);
        verify(cloudinaryService, never()).deleteImage(any());
    }

    @Test
    void deleteProduct_ImageNotDefault_DeleteCloudinary() {
        product.setImageId("custom-id");
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        productsService.deleteProduct(1);
        verify(cloudinaryService).deleteImage("custom-id");
        verify(productsRepository).delete(product);
    }

    @Test
    void deleteProduct_DeleteImageFails() {
        product.setImageId("custom-id");
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        doThrow(new RuntimeException("fail")).when(cloudinaryService).deleteImage("custom-id");
        assertThrows(RuntimeException.class, () -> productsService.deleteProduct(1));
    }

    /* ---------- GET BY ID ---------- */
    @Test
    void getProductById_NotFound() {
        when(productsRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productsService.getProductById(1));
    }

    @Test
    void getProductById_Success() {
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        ProductsResponse res = productsService.getProductById(1);
        assertEquals(1, res.getId());
    }

    /* ---------- GET ALL ---------- */
    @Test
    void getAllProducts_WithData() {
        Page<Products> page = new PageImpl<>(List.of(product));
        when(productsRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<ProductsResponse> res = productsService.getAllProducts(0, 10);
        assertFalse(res.isEmpty());
    }

    @Test
    void getAllProducts_Empty() {
        Page<Products> page = new PageImpl<>(Collections.emptyList());
        when(productsRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<ProductsResponse> res = productsService.getAllProducts(0, 10);
        assertTrue(res.isEmpty());
    }

    /* ---------- SEARCH BY NAME ---------- */
    @Test
    void searchProductsByName_Found() {
        when(productsRepository.findByNameContainingIgnoreCase("abc")).thenReturn(List.of(product));
        List<ProductsResponse> res = productsService.searchProductsByName("abc");
        assertEquals(1, res.size());
    }

    @Test
    void searchProductsByName_NotFound() {
        when(productsRepository.findByNameContainingIgnoreCase("abc")).thenReturn(Collections.emptyList());
        List<ProductsResponse> res = productsService.searchProductsByName("abc");
        assertTrue(res.isEmpty());
    }

    /* ---------- GET BY CATEGORY ---------- */
    @Test
    void getProductsByCategory_Found() {
        when(productsRepository.findByCategory_Id(1)).thenReturn(List.of(product));
        List<ProductsResponse> res = productsService.getProductsByCategory(1);
        assertEquals(1, res.size());
    }

    @Test
    void getProductsByCategory_NotFound() {
        when(productsRepository.findByCategory_Id(1)).thenReturn(Collections.emptyList());
        List<ProductsResponse> res = productsService.getProductsByCategory(1);
        assertTrue(res.isEmpty());
    }

    /* ---------- GET BY PRICE RANGE ---------- */
    @Test
    void getProductsByPriceRange_Found() {
        when(productsRepository.findBySalePriceBetween(10.0, 200.0)).thenReturn(List.of(product));
        List<ProductsResponse> res = productsService.getProductsByPriceRange(10.0, 200.0);
        assertEquals(1, res.size());
    }

    @Test
    void getProductsByPriceRange_NotFound() {
        when(productsRepository.findBySalePriceBetween(10.0, 200.0)).thenReturn(Collections.emptyList());
        List<ProductsResponse> res = productsService.getProductsByPriceRange(10.0, 200.0);
        assertTrue(res.isEmpty());
    }

    /* ---------- SEARCH PRODUCTS ---------- */
    @Test
    void searchProducts_Found() {
        when(productsRepository.searchProducts("abc", 1, 10.0, 200.0)).thenReturn(List.of(product));
        List<ProductsResponse> res = productsService.searchProducts("abc", 1, 10.0, 200.0);
        assertEquals(1, res.size());
    }

    @Test
    void searchProducts_NotFound() {
        when(productsRepository.searchProducts("abc", 1, 10.0, 200.0)).thenReturn(Collections.emptyList());
        List<ProductsResponse> res = productsService.searchProducts("abc", 1, 10.0, 200.0);
        assertTrue(res.isEmpty());
    }

    /* ---------- VALIDATE ---------- */
    @Test
    void validate_Fail() {
        request.setImportPrice(BigDecimal.valueOf(100));
        request.setSalePrice(BigDecimal.valueOf(50));

        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));

        assertThrows(BusinessRuleException.class, () -> productsService.createProduct(request));
    }

    @Test
    void validate_Pass() {
        request.setImportPrice(BigDecimal.valueOf(100));
        request.setSalePrice(BigDecimal.valueOf(150));

        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(product);

        ProductsResponse res = productsService.createProduct(request);
        assertEquals(1, res.getId());
    }
}
