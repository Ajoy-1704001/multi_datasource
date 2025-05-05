package com.deb.multi_datasource.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deb.multi_datasource.model.mongodb.Product;
import com.deb.multi_datasource.model.mongodb.ProductDetails;
import com.deb.multi_datasource.model.postgres.Order;
import com.deb.multi_datasource.model.postgres.OrderItem;
import com.deb.multi_datasource.repository.mongodb.ProductRepository;
import com.deb.multi_datasource.repository.postgres.OrderRepository;
import com.deb.multi_datasource.service.AnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class loads sample data into databases for demonstration purposes
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
    private AnalyticsService analyticsService;

    @Override
    public void run(String... args) {
        // Clear existing data
        productRepository.deleteAll();
        orderRepository.deleteAll();

        // Load sample products to MongoDB
        List<Product> products = createSampleProducts();
        productRepository.saveAll(products);

        // Load sample orders to PostgreSQL
        List<Order> orders = createSampleOrders(products);
        orderRepository.saveAll(orders);

        // Generate analytics data to H2
        // For demo purposes, generate analytics for the last 7 days
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            analyticsService.generateDailySalesAnalytics(date);
        }

        System.out.println("Sample data loaded to all data sources!");
    }

    private List<Product> createSampleProducts() {
        List<Product> products = new ArrayList<>();

        // Create Smartphone
        Product smartphone = Product.builder()
                .name("Premium Smartphone")
                .description("Latest model with high-end specs")
                .sku("TECH-1001")
                .price(new BigDecimal("999.99"))
                .stockQuantity(120)
                .categories(Arrays.asList("Electronics", "Smartphones"))
                .attributes(Map.of(
                        "color", "Black",
                        "storage", "256GB",
                        "screen", "6.5inch"
                ))
                .imageUrls(Arrays.asList("/images/smartphone1.jpg", "/images/smartphone2.jpg"))
                .details(ProductDetails.builder()
                        .manufacturer("Techno")
                        .origin("China")
                        .warranty("1 Year")
                        .material("Aluminum, Glass")
                        .specifications(Map.of(
                                "processor", "Octa-core",
                                "ram", "8GB",
                                "camera", "48MP"
                        ))
                        .build())
                .build();
        products.add(smartphone);

        // Create Laptop
        Product laptop = Product.builder()
                .name("Macbook Pro Intel 2018")
                .description("Lightweight professional laptop")
                .sku("TECH-2002")
                .price(new BigDecimal("1499"))
                .stockQuantity(75)
                .categories(Arrays.asList("Electronics", "Computers", "Laptops"))
                .attributes(Map.of(
                        "color", "Silver",
                        "storage", "512GB SSD",
                        "screen", "14inch"
                ))
                .imageUrls(Arrays.asList("/images/laptop1.jpg", "/images/laptop2.jpg"))
                .details(ProductDetails.builder()
                        .manufacturer("Apple")
                        .origin("Taiwan")
                        .warranty("2 Years")
                        .material("Aluminum")
                        .specifications(Map.of(
                                "processor", "Intel i7",
                                "ram", "16GB",
                                "gpu", "Dedicated 4GB"
                        ))
                        .build())
                .build();
        products.add(laptop);

        // Create Headphones
        Product headphones = Product.builder()
                .name("Wireless Noise-Cancelling Headphones")
                .description("Premium audio experience with active noise cancellation")
                .sku("AUDIO-3003")
                .price(new BigDecimal("299.99"))
                .stockQuantity(200)
                .categories(Arrays.asList("Electronics", "Audio", "Headphones"))
                .attributes(Map.of(
                        "color", "Black/Silver",
                        "type", "Over-ear",
                        "connectivity", "Bluetooth 5.0"
                ))
                .imageUrls(Arrays.asList("/images/headphones1.jpg", "/images/headphones2.jpg"))
                .details(ProductDetails.builder()
                        .manufacturer("AudioPro")
                        .origin("Japan")
                        .warranty("1 Year")
                        .material("Plastic, Memory Foam")
                        .specifications(Map.of(
                                "batteryLife", "30 hours",
                                "weight", "250g",
                                "chargingTime", "3 hours"
                        ))
                        .build())
                .build();
        products.add(headphones);

        // Create Smart Watch
        Product smartWatch = Product.builder()
                .name("Fitness Smart Watch")
                .description("Track your fitness and stay connected")
                .sku("WEAR-4004")
                .price(new BigDecimal("199.99"))
                .stockQuantity(150)
                .categories(Arrays.asList("Electronics", "Wearables", "Fitness"))
                .attributes(Map.of(
                        "color", "Black",
                        "waterproof", "Yes",
                        "display", "AMOLED"
                ))
                .imageUrls(Arrays.asList("/images/watch1.jpg", "/images/watch2.jpg"))
                .details(ProductDetails.builder()
                        .manufacturer("FitTech")
                        .origin("USA")
                        .warranty("1 Year")
                        .material("Silicone, Aluminum")
                        .specifications(Map.of(
                                "batteryLife", "7 days",
                                "sensors", "Heart rate, GPS, Accelerometer",
                                "waterResistance", "50m"
                        ))
                        .build())
                .build();
        products.add(smartWatch);

        return products;
    }

    private List<Order> createSampleOrders(List<Product> products) {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        // Create 20 random orders over the last week
        for (int i = 0; i < 20; i++) {
            List<OrderItem> items = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            // Add 1-3 products to each order
            int numberOfItems = random.nextInt(3) + 1;
            for (int j = 0; j < numberOfItems; j++) {
                // Select a random product
                Product product = products.get(random.nextInt(products.size()));
                int quantity = random.nextInt(3) + 1;
                BigDecimal price = product.getPrice();
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

                OrderItem item = OrderItem.builder()
                        .productId(product.getId())
                        .quantity(quantity)
                        .price(price)
                        .subtotal(subtotal)
                        .build();

                items.add(item);
                totalAmount = totalAmount.add(subtotal);
            }

            // Create random order date within the last 7 days
            LocalDateTime orderDate = now.minusDays(random.nextInt(7))
                    .minusHours(random.nextInt(24))
                    .minusMinutes(random.nextInt(60));

            Order.OrderStatus[] statuses = Order.OrderStatus.values();
            Order.OrderStatus status;
            int statusRandom = random.nextInt(10);
            if (statusRandom < 7) {
                // 70% chance of DELIVERED or SHIPPED
                status = (statusRandom < 4) ? Order.OrderStatus.DELIVERED : Order.OrderStatus.SHIPPED;
            } else {
                status = statuses[random.nextInt(statuses.length)];
            }

            Order order = Order.builder()
                    .orderNumber("ORD-" + (1000 + i))
                    .customerId(1L + random.nextInt(10)) // Random customer ID
                    .orderDate(orderDate)
                    .status(status)
                    .totalAmount(totalAmount)
                    .items(items)
                    .build();

            orders.add(order);
        }

        return orders;
    }
}