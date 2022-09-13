package com.tdd.TDD.repository;

import com.tdd.TDD.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
