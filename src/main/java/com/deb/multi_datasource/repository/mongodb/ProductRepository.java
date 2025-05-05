package com.deb.multi_datasource.repository.mongodb;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.deb.multi_datasource.model.mongodb.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
	List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByCategoriesIn(List<String> categories);
    
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    
    List<Product> findByStockQuantityLessThan(int threshold);
    
    @Query("{'attributes.?0': ?1}")
    List<Product> findByAttribute(String attributeName, String attributeValue);
}
