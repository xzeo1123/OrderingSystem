package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BillsRepository extends JpaRepository<Bills, Integer> {
    List<Bills> findByTable_Id(Integer tableId);
    List<Bills> findByUser_Id(Integer userId);
    List<Bills> findByDateCreateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Bills> findByTotalBetween(Double minTotal, Double maxTotal);
}
