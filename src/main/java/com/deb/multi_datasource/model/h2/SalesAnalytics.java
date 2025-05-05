package com.deb.multi_datasource.model.h2;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales_analytics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "report_date")
    private LocalDate reportDate;
    
    @Column(name = "product_id")
    private String productId;
    
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "units_sold")
    private Integer unitsSold;
    
    @Column(name = "revenue")
    private BigDecimal revenue;
    
    @Column(name = "region")
    private String region;
}
