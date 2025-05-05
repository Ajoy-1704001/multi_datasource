package com.deb.multi_datasource.model.mongodb;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {
    private String manufacturer;
    private String origin;
    private String warranty;
    private String material;
    private Map<String, Object> specifications;
}