package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Float total;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime dateCreate;

    private String note;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Byte status;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReceiptDetails> details = new ArrayList<>();
}
