package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "receipt_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"receipt_id", "product_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipts receipt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @Column(nullable = false)
    private Integer quantity;
}
