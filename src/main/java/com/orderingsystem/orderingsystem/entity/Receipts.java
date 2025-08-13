package com.orderingsystem.orderingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "receipts",
        indexes = {
                @Index(name = "idx_receipt_date", columnList = "dateCreate")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Float total;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreate;

    @NotBlank
    @Column(nullable = false)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReceiptDetails> details = new ArrayList<>();
}
