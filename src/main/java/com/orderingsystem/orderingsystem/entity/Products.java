package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String imageId;

    @Column(nullable = false)
    private Float importPrice;

    @Column(nullable = false)
    private Float salePrice;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer quantity;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Byte status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
}
