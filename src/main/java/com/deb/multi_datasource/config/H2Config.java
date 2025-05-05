package com.deb.multi_datasource.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "h2EntityManagerFactory",
        transactionManagerRef = "h2TransactionManager",
        basePackages = {"com.deb.multi_datasource.repository.h2"}
)
public class H2Config {
	@Bean(name = "h2DataSource")
    @ConfigurationProperties(prefix = "h2.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("h2DataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.deb.multi_datasource.model.h2")
                .persistenceUnit("h2")
                .build();
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("h2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
