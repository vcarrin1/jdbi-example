package com.vcarrin87.jdbi_example.config;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.vcarrin87.jdbi_example.models.Customer;
import com.vcarrin87.jdbi_example.models.Inventory;
import com.vcarrin87.jdbi_example.models.OrderItems;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.models.Payments;
import com.vcarrin87.jdbi_example.models.Products;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
@Slf4j
public class DatasourceConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource driverManagerDataSource() {
        return new DriverManagerDataSource();
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);
        return txManager;
    }
    

    @Bean
    public Jdbi jdbi(DataSource dataSource) throws SQLException {
        log.info("Creating Jdbi instance: {}", dataSource.getConnection().getMetaData().getURL());
        return Jdbi.create(dataSource)
            .installPlugin(new SqlObjectPlugin())
            .installPlugin(new PostgresPlugin())
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
            .registerRowMapper(ConstructorMapper.factory(Customer.class, "c"))
            .registerRowMapper(ConstructorMapper.factory(Products.class, "p"))
            .registerRowMapper(ConstructorMapper.factory(Payments.class, "pay"))
            .registerRowMapper(ConstructorMapper.factory(Orders.class, "o"))
            .registerRowMapper(ConstructorMapper.factory(OrderItems.class, "oi"))
            .registerRowMapper(ConstructorMapper.factory(Inventory.class, "i"));
    }

}
