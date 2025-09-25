package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Receipts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReceiptsRepository extends JpaRepository<Receipts, Integer> {
    List<Receipts> findByDateCreateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Receipts> findByTotalBetween(Double minTotal, Double maxTotal);
}
