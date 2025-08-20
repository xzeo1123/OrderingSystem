package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "products",
    indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_category", columnList = "category_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Image Url cannot be empty")
    @Column(nullable = false)
    private String imageUrl;

    @NotBlank(message = "Image Id cannot be empty")
    @Column(nullable = false)
    private String imageId;

    @NotNull(message = "Import Price cannot be empty")
    @Min(value = 0, message = "Import Price cannot be lower than 0")
    @Column(nullable = false)
    private BigDecimal importPrice;

    @NotNull(message = "Sale Price cannot be empty")
    @Min(value = 0, message = "Sale Price cannot be lower than 0")
    @Column(nullable = false)
    private BigDecimal salePrice;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 0, message = "Quantity cannot be lower than 0")
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillDetails> billDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReceiptDetails> receiptDetails = new ArrayList<>();
}

