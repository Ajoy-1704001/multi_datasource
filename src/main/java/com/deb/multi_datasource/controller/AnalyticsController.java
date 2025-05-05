package com.deb.multi_datasource.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deb.multi_datasource.model.mongodb.Product;
import com.deb.multi_datasource.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
	@Autowired
    private AnalyticsService analyticsService;
    
    @PostMapping("/generate")
    public ResponseEntity<?> generateAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(analyticsService.generateDailySalesAnalytics(date));
    }
    
    @GetMapping("/top-products")
    public ResponseEntity<?> getTopProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getTopSellingProducts(startDate, endDate, limit));
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable String productId) {
        Product product = analyticsService.getProductDetails(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/product/{productId}/orders")
    public ResponseEntity<?> getProductOrders(@PathVariable String productId) {
        return ResponseEntity.ok(analyticsService.getOrdersContainingProduct(productId));
    }
}
