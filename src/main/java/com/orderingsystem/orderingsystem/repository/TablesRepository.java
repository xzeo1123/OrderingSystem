package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TablesRepository extends JpaRepository<Tables, Integer> {
    boolean existsByNumber(int number);
    boolean existsByNumberAndIdNot(Integer number, Integer id);
    Optional<Tables> findByNumber(int number);
}
