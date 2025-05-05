package com.deb.multi_datasource.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager",
        basePackages = {"com.deb.multi_datasource.repository.postgres"}
)
public class PostgresConfig {
	@Primary
	@Bean(name = "postgresDataSource")
	@ConfigurationProperties(prefix = "postgres.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Primary
	@Bean(name = "postgresEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("postgresDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource)
				.packages("com.deb.multi_datasource.model.postgres")
				.persistenceUnit("postgres")
				.build();
	}
	
	@Primary
	@Bean(name = "postgresTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
