package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TablesRepository extends JpaRepository<Tables, Integer> {
    boolean existsByNumber(int tableNumber);
    boolean existsByNumberAndIdNot(Integer tableNumber, Integer tableId);
    Optional<Tables> findByNumber(int tableNumber);
}
