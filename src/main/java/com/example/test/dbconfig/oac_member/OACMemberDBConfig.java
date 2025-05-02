/*
 * Name: AccoutingAndFinanceDBConfig.java
 * Date: Feb 4, 2023
 *
 *
 * Copyright (C) 2023 Copart, Inc. All rights reserved.
 */
package com.example.test.dbconfig.oac_member;


import com.example.test.repository.OACBaseJPARepositoryImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author vvpally
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "oacEntityManagerFactory", basePackages = { "com.example.test.repository.oac_member" },
        transactionManagerRef = "oacTransactionManager", repositoryBaseClass = OACBaseJPARepositoryImpl.class)
public class OACMemberDBConfig
{
    @Value("${spring.datasource.slow-query-log-threshold:60000}")
    Long slowQueryThresholdMs;

    @Value("${spring.datasource.slow-query-log:true}")
    boolean slowQueryLog = true;

    @Bean
    @ConfigurationProperties(prefix = "spring.oac-datasource")
    public HikariConfig oacDataSourceProperties()
    {
        return new HikariConfig();
    }

    @Bean(name = "oacDataSource")
    public DataSource oacDataSource()
    {
        HikariConfig config = oacDataSourceProperties();


        // Slow query log handling (Hikari does not have built-in slow query logging like Tomcat)
        if (slowQueryLog) {
            config.addDataSourceProperty("slowQueryThresholdMs", slowQueryThresholdMs);
            config.addDataSourceProperty("logSlowQueries", true);
        }

        return new HikariDataSource(config);
    }


    @Bean(name = "oacEntityManagerFactory")
    @PersistenceContext(unitName = "oacMember")
    public LocalContainerEntityManagerFactoryBean oacEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("oacDataSource") DataSource dataSource)
    {

        return builder.dataSource(dataSource).packages("com.example.test.model.entity").persistenceUnit("oacMember").build();
    }


    @Bean(name = "oacTransactionManager")
    public PlatformTransactionManager oacTransactionManager(@Qualifier("oacEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}