package com.deb.multi_datasource.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deb.multi_datasource.model.h2.ProductSalesData;
import com.deb.multi_datasource.model.h2.SalesAnalytics;
import com.deb.multi_datasource.model.mongodb.Product;
import com.deb.multi_datasource.model.postgres.Order;
import com.deb.multi_datasource.model.postgres.OrderItem;
import com.deb.multi_datasource.repository.h2.SalesAnalyticsRepository;
import com.deb.multi_datasource.repository.mongodb.ProductRepository;
import com.deb.multi_datasource.repository.postgres.OrderRepository;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
    private ProductRepository productRepository;
	@Autowired
    private SalesAnalyticsRepository salesAnalyticsRepository;

	@Override
	public List<SalesAnalytics> generateDailySalesAnalytics(LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusSeconds(1);
        
        List<Order> dailyOrders = orderRepository.findByOrderDateBetween(startOfDay, endOfDay);
        
        Map<String, ProductSalesData> productSalesMap = new HashMap<>();
        
        for (Order order : dailyOrders) {
            if (order.getStatus() == Order.OrderStatus.DELIVERED || 
                order.getStatus() == Order.OrderStatus.SHIPPED) {
                for (OrderItem item : order.getItems()) {
                    String productId = item.getProductId();
                    ProductSalesData data = productSalesMap.computeIfAbsent(
                        productId, id -> new ProductSalesData(productId)
                    );
                    
                    data.addUnits(item.getQuantity());
                    data.addRevenue(item.getSubtotal());
                }
            }
        }
        
        List<String> productIds = new ArrayList<>(productSalesMap.keySet());
        List<Product> products = productRepository.findAllById(productIds);
        
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), product);
        }
        
        List<SalesAnalytics> analyticsEntries = new ArrayList<>();
        for (Map.Entry<String, ProductSalesData> entry : productSalesMap.entrySet()) {
            String productId = entry.getKey();
            ProductSalesData salesData = entry.getValue();
            Product product = productMap.get(productId);
            
            if (product != null) {
                String category = !product.getCategories().isEmpty() ? 
                    product.getCategories().get(0) : "Uncategorized";
                
                SalesAnalytics analytics = SalesAnalytics.builder()
                    .reportDate(date)
                    .productId(productId)
                    .productName(product.getName())
                    .category(category)
                    .unitsSold(salesData.getUnitsSold())
                    .revenue(salesData.getRevenue())
                    .region("Global")
                    .build();
                
                analyticsEntries.add(analytics);
            }
        }
        
        return salesAnalyticsRepository.saveAll(analyticsEntries);
		
	}

	@Override
	public List<SalesAnalytics> getTopSellingProducts(LocalDate startDate, LocalDate endDate, int limit) {
		List<SalesAnalytics> salesData = salesAnalyticsRepository.findByReportDateBetween(startDate, endDate);
        salesData.sort((a, b) -> b.getRevenue().compareTo(a.getRevenue()));
        if (salesData.size() > limit) {
            return salesData.subList(0, limit);
        }
        return salesData;
	}

	@Override
	public Product getProductDetails(String productId) {
		return productRepository.findById(productId).orElse(null);
	}

	@Override
	public List<Order> getOrdersContainingProduct(String productId) {
		List<Order> allOrders = orderRepository.findAll();
        List<Order> filteredOrders = new ArrayList<>();
        
        for (Order order : allOrders) {
            for (OrderItem item : order.getItems()) {
                if (item.getProductId().equals(productId)) {
                    filteredOrders.add(order);
                    break;
                }
            }
        }
        
        return filteredOrders;
	}

}
