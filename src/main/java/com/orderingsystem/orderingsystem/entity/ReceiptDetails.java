package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
    name = "receipt_details",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"receipt_id", "product_id"})
    },
    indexes = {
        @Index(name = "idx_receiptdetail_receipt", columnList = "receipt_id"),
        @Index(name = "idx_receiptdetail_product", columnList = "product_id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Sale Price cannot be lower than 1")
    @Column(nullable = false)
    private Integer quantity;
}
