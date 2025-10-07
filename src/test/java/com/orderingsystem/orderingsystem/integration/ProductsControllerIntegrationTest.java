package com.orderingsystem.orderingsystem.integration;

import com.orderingsystem.orderingsystem.OrderingsystemApplication;
import com.orderingsystem.orderingsystem.config.TestCacheConfig;
import com.orderingsystem.orderingsystem.config.TestSecurityConfig;
import com.orderingsystem.orderingsystem.entity.Products;
import com.orderingsystem.orderingsystem.entity.Categories;
import com.orderingsystem.orderingsystem.entity.Status;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.repository.CategoriesRepository;
import com.orderingsystem.orderingsystem.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        classes = {
                OrderingsystemApplication.class,
                TestCacheConfig.class,
                TestSecurityConfig.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@EntityScan(basePackages = "com.orderingsystem.orderingsystem.entity")
@ComponentScan(basePackages = "com.orderingsystem.orderingsystem")
@AutoConfigureTestDatabase
@Transactional
class ProductsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    private Categories savedCategory;

    @BeforeEach
    void setup() {
        Categories category = new Categories();
        category.setName("Test Category");
        category.setDescription("Test Category Description");
        category.setStatus(Status.ACTIVE);
        savedCategory = categoriesRepository.save(category);
    }

    @Test
    void testCreateProduct() throws Exception {
        String requestJson = """
        {
          "name": "Test Product",
          "imageUrl": "http://image.com/img.png",
          "imageId": "img123",
          "importPrice": 100,
          "salePrice": 150,
          "quantity": 10,
          "status": "ACTIVE",
          "categoryId": %d
        }
        """.formatted(savedCategory.getId());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.salePrice").value(150));
    }

    @Test
    void testGetProductById() throws Exception {
        Products product = new Products();
        product.setName("Existing Product");
        product.setImageUrl("http://image.com/img.png");
        product.setImageId("img123");
        product.setImportPrice(BigDecimal.valueOf(100));
        product.setSalePrice(BigDecimal.valueOf(200));
        product.setQuantity(5);
        product.setCategory(savedCategory);

        Products savedProduct = productsRepository.save(product);

        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Existing Product"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Products product = new Products();
        product.setName("Old Product");
        product.setImageUrl("http://image.com/img.png");
        product.setImageId("img123");
        product.setImportPrice(BigDecimal.valueOf(100));
        product.setSalePrice(BigDecimal.valueOf(200));
        product.setQuantity(5);
        product.setCategory(savedCategory);
        Products savedProduct = productsRepository.save(product);

        String updateJson = """
        {
          "name": "Updated Product",
          "imageUrl": "http://image.com/img2.png",
          "imageId": "img456",
          "importPrice": 120,
          "salePrice": 180,
          "quantity": 8,
          "status": "ACTIVE",
          "categoryId": %d
        }
        """.formatted(savedCategory.getId());

        doNothing().when(cloudinaryService).deleteImage(anyString());

        mockMvc.perform(put("/api/v1/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Product"))
                .andExpect(jsonPath("$.data.quantity").value(8));
    }

    @Test
    void testGetAllProducts() throws Exception {
        // thêm 2 product
        Products p1 = new Products();
        p1.setName("P1");
        p1.setImageUrl("http://image.com/p1.png");
        p1.setImageId("id1");
        p1.setImportPrice(BigDecimal.valueOf(100));
        p1.setSalePrice(BigDecimal.valueOf(150));
        p1.setQuantity(10);
        p1.setCategory(savedCategory);

        Products p2 = new Products();
        p2.setName("P2");
        p2.setImageUrl("http://image.com/p2.png");
        p2.setImageId("id2");
        p2.setImportPrice(BigDecimal.valueOf(200));
        p2.setSalePrice(BigDecimal.valueOf(300));
        p2.setQuantity(20);
        p2.setCategory(savedCategory);

        productsRepository.save(p1);
        productsRepository.save(p2);

        mockMvc.perform(get("/api/v1/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }

    @Test
    void testSoftDeleteProduct() throws Exception {
        Products product = new Products();
        product.setName("SoftDelete Product");
        product.setImageUrl("http://image.com/img.png");
        product.setImageId("img123");
        product.setImportPrice(BigDecimal.valueOf(100));
        product.setSalePrice(BigDecimal.valueOf(200));
        product.setQuantity(5);
        product.setCategory(savedCategory);
        Products savedProduct = productsRepository.save(product);

        mockMvc.perform(put("/api/v1/products/delete/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Products product = new Products();
        product.setName("Delete Product");
        product.setImageUrl("http://image.com/img.png");
        product.setImageId("img123");
        product.setImportPrice(BigDecimal.valueOf(100));
        product.setSalePrice(BigDecimal.valueOf(200));
        product.setQuantity(5);
        product.setCategory(savedCategory);
        Products savedProduct = productsRepository.save(product);

        doNothing().when(cloudinaryService).deleteImage(anyString());

        mockMvc.perform(delete("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    void testSearchByName() throws Exception {
        Products p1 = new Products();
        p1.setName("Apple iPhone");
        p1.setImageUrl("http://image.com/p1.png");
        p1.setImageId("id1");
        p1.setImportPrice(BigDecimal.valueOf(100));
        p1.setSalePrice(BigDecimal.valueOf(150));
        p1.setQuantity(10);
        p1.setCategory(savedCategory);
        productsRepository.save(p1);

        mockMvc.perform(get("/api/v1/products/searchbyname")
                        .param("productName", "iPhone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Apple iPhone"));
    }

    @Test
    void testFilterByCategory() throws Exception {
        Products p1 = new Products();
        p1.setName("Cat Product");
        p1.setImageUrl("http://image.com/cat.png");
        p1.setImageId("idCat");
        p1.setImportPrice(BigDecimal.valueOf(100));
        p1.setSalePrice(BigDecimal.valueOf(150));
        p1.setQuantity(10);
        p1.setCategory(savedCategory);
        productsRepository.save(p1);

        mockMvc.perform(get("/api/v1/products/filter/category")
                        .param("categoryId", savedCategory.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Cat Product"));
    }

    @Test
    void testFilterByPriceRange() throws Exception {
        Products p1 = new Products();
        p1.setName("Cheap Product");
        p1.setImageUrl("http://image.com/c1.png");
        p1.setImageId("idc1");
        p1.setImportPrice(BigDecimal.valueOf(50));
        p1.setSalePrice(BigDecimal.valueOf(100));
        p1.setQuantity(5);
        p1.setCategory(savedCategory);
        productsRepository.save(p1);

        Products p2 = new Products();
        p2.setName("Expensive Product");
        p2.setImageUrl("http://image.com/c2.png");
        p2.setImageId("idc2");
        p2.setImportPrice(BigDecimal.valueOf(500));
        p2.setSalePrice(BigDecimal.valueOf(1000));
        p2.setQuantity(2);
        p2.setCategory(savedCategory);
        productsRepository.save(p2);

        mockMvc.perform(get("/api/v1/products/filter/price")
                        .param("minPrice", "50")
                        .param("maxPrice", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Cheap Product"));
    }

    @Test
    void testSearchProductsWithMultipleFilters() throws Exception {
        Products p1 = new Products();
        p1.setName("Samsung Galaxy");
        p1.setImageUrl("http://image.com/sg.png");
        p1.setImageId("idsg");
        p1.setImportPrice(BigDecimal.valueOf(300));
        p1.setSalePrice(BigDecimal.valueOf(400));
        p1.setQuantity(15);
        p1.setCategory(savedCategory);
        productsRepository.save(p1);

        mockMvc.perform(get("/api/v1/products/filter")
                        .param("productName", "Samsung")
                        .param("categoryId", savedCategory.getId().toString())
                        .param("minPrice", "200")
                        .param("maxPrice", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Samsung Galaxy"));
    }
}
