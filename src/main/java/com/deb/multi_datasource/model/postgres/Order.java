package com.deb.multi_datasource.model.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNumber;
    
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private BigDecimal totalAmount;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;
    
    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELED
    }
}