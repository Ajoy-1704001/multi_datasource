package com.deb.multi_datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		MongoAutoConfiguration.class
})
public class MultiDatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiDatasourceApplication.class, args);
	}

}
