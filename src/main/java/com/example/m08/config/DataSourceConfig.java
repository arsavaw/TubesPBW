package com.example.m08.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        // Gantilah URL, username, dan password sesuai dengan konfigurasi database PostgreSQL Anda
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tubes");
        dataSource.setUsername("postgres");
        dataSource.setPassword("ayamgoreng");
        
        return dataSource;
    }
}