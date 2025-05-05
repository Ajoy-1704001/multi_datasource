package com.deb.multi_datasource.service;

import java.time.LocalDate;
import java.util.List;

import com.deb.multi_datasource.model.h2.SalesAnalytics;
import com.deb.multi_datasource.model.mongodb.Product;
import com.deb.multi_datasource.model.postgres.Order;

public interface AnalyticsService {
	public List<SalesAnalytics> generateDailySalesAnalytics(LocalDate date);
	public List<SalesAnalytics> getTopSellingProducts(LocalDate startDate, LocalDate endDate, int limit);
	public Product getProductDetails(String productId);
	public List<Order> getOrdersContainingProduct(String productId);
}
