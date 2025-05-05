package com.deb.multi_datasource.model.h2;

import java.math.BigDecimal;

public class ProductSalesData {
	private final String productId;
    private int unitsSold = 0;
    private BigDecimal revenue = BigDecimal.ZERO;
    
    public ProductSalesData(String productId) {
        this.productId = productId;
    }
    
    public void addUnits(int units) {
        this.unitsSold += units;
    }
    
    public void addRevenue(BigDecimal amount) {
        this.revenue = this.revenue.add(amount);
    }
    
    public String getProductId() {
        return productId;
    }
    
    public int getUnitsSold() {
        return unitsSold;
    }
    
    public BigDecimal getRevenue() {
        return revenue;
    }
}
