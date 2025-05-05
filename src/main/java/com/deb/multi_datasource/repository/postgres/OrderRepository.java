package com.deb.multi_datasource.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deb.multi_datasource.model.postgres.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomerId(Long customerId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT o FROM Order o WHERE o.totalAmount > ?1")
    List<Order> findHighValueOrders(double minAmount);
}