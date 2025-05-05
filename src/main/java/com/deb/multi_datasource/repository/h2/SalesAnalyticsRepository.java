package com.deb.multi_datasource.repository.h2;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deb.multi_datasource.model.h2.SalesAnalytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesAnalyticsRepository extends JpaRepository<SalesAnalytics, Long> {

    List<SalesAnalytics> findByReportDateBetween(LocalDate start, LocalDate end);
    
    List<SalesAnalytics> findByCategory(String category);
    
    List<SalesAnalytics> findByRegion(String region);
    
    @Query("SELECT sa FROM SalesAnalytics sa WHERE sa.revenue > ?1")
    List<SalesAnalytics> findHighPerformingProducts(BigDecimal minRevenue);
    
    @Query("SELECT SUM(sa.revenue) FROM SalesAnalytics sa WHERE sa.reportDate BETWEEN ?1 AND ?2")
    BigDecimal calculateTotalRevenue(LocalDate start, LocalDate end);
    
    @Query("SELECT sa.category, SUM(sa.unitsSold) as totalSold FROM SalesAnalytics sa " +
           "GROUP BY sa.category ORDER BY totalSold DESC")
    List<Object[]> findTopCategories();
}
