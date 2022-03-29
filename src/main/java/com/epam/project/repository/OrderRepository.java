package com.epam.project.repository;

import com.epam.project.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<OrderInfo, Integer> {
    Collection<OrderInfo> findByEmail(String email);
}