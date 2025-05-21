package com.vcarrin87.jdbi_example.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.h2.H2DatabasePlugin;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.vcarrin87.jdbi_example.models.Customer;
import com.vcarrin87.jdbi_example.models.Inventory;
import com.vcarrin87.jdbi_example.models.OrderItems;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.models.Payments;
import com.vcarrin87.jdbi_example.models.Products;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Profile("test")
@Slf4j
public class H2JdbiConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DriverManagerDataSource();
    }

    @Bean
    public Jdbi jdbi(DataSource dataSource) throws SQLException {
        log.info("Creating Jdbi TEST instance: {}", dataSource.getConnection().getMetaData().getURL());
        return Jdbi.create(new TransactionAwareDataSourceProxy(dataSource))
            .installPlugin(new SqlObjectPlugin())
            .installPlugin(new H2DatabasePlugin())
            .registerColumnMapper(java.util.Date.class, (rs, columnNumber, ctx) -> 
                rs.getTimestamp(columnNumber) != null 
                    ? new java.util.Date(rs.getTimestamp(columnNumber).getTime()) 
                    : null
            )
            .registerColumnMapper(java.sql.Timestamp.class, (rs, columnNumber, ctx) -> 
                rs.getTimestamp(columnNumber) != null 
                    ? new java.sql.Timestamp(rs.getTimestamp(columnNumber).getTime()) 
                    : null
            )
            .registerRowMapper(ConstructorMapper.factory(Customer.class))
            .registerRowMapper(ConstructorMapper.factory(Products.class))
            .registerRowMapper(ConstructorMapper.factory(Payments.class))
            .registerRowMapper(ConstructorMapper.factory(Orders.class))
            .registerRowMapper(ConstructorMapper.factory(OrderItems.class))
            .registerRowMapper(ConstructorMapper.factory(Inventory.class));
    }
}