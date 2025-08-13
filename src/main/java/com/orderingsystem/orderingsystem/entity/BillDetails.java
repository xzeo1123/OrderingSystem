package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "bill_details",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"bill_id", "product_id"})
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @Column(nullable = false)
    private Integer quantity;
}
