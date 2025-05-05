package com.deb.multi_datasource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@Configuration
@EnableMongoRepositories(
        basePackages = "com.deb.multi_datasource.repository.mongodb",
        mongoTemplateRef = "mongoTemplate"
)
public class MongoConfig {
	@Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://" + host + ":" + port);
    }

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }
}
