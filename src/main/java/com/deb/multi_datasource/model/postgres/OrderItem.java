package com.deb.multi_datasource.model.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_id")
    private String productId;
    
    private Integer quantity;
    
    private BigDecimal price;
    
    @Column(name = "subtotal")
    private BigDecimal subtotal;
}