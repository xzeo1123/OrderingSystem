package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.BillDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillDetailsRepository extends JpaRepository<BillDetails, Integer> {
    List<BillDetails> findByProduct_Id(Integer productId);
    List<BillDetails> findByBill_Id(Integer billId);
}
