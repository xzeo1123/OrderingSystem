package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    List<Users> findByUsernameContainingIgnoreCase(String username);
}
