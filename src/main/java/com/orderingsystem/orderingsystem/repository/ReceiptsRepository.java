package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Receipts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptsRepository extends JpaRepository<Receipts, Integer> {
}
