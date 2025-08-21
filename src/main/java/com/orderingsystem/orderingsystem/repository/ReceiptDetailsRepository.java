package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptDetailsRepository extends JpaRepository<ReceiptDetails, Integer> {
    List<ReceiptDetails> findByProduct_Id(Integer productId);
    List<ReceiptDetails> findByReceipt_Id(Integer receiptId);
}
