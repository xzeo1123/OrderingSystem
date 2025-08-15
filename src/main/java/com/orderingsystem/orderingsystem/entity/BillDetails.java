package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
    name = "bill_details",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"bill_id", "product_id"})
    },
    indexes = {
        @Index(name = "idx_billdetail_bill", columnList = "bill_id"),
        @Index(name = "idx_billdetail_product", columnList = "product_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bills bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Quantity cannot be lower than 1")
    @Column(nullable = false)
    private Integer quantity;
}
