package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillsRepository extends JpaRepository<Bills, Integer> {
    List<Bills> findByTable_Id(Integer tableId);
    List<Bills> findByUser_id(Integer userId);
}
