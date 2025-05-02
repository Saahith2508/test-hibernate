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
import org.hibernate.Interceptor;
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
import java.util.Collections;

/**
 * @author vvpally
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "oacArchiveEntityManagerFactory", basePackages = { "com.example.test.repository.oac_archive" },
        transactionManagerRef = "oacArchiveTransactionManager", repositoryBaseClass = OACBaseJPARepositoryImpl.class)
public class OACMemberArchiveDBConfig
{
    @Value("${spring.datasource.slow-query-log-threshold:60000}")
    Long slowQueryThresholdMs;

    @Value("${spring.datasource.slow-query-log:true}")
    boolean slowQueryLog = true;


    @Bean
    @ConfigurationProperties(prefix = "spring.oac-member-archive-datasource")
    public HikariConfig oacMemberArchiveDataSourceProperties()
    {
        return new HikariConfig();
    }

    @Bean(name = "oacMemberArchiveDataSource")
    public DataSource oacMemberArchiveDataSource()
    {
        HikariConfig config = oacMemberArchiveDataSourceProperties();

        // Slow query log handling (Hikari does not have built-in slow query logging like Tomcat)
        if (slowQueryLog) {
            config.addDataSourceProperty("slowQueryThresholdMs", slowQueryThresholdMs);
            config.addDataSourceProperty("logSlowQueries", true);
        }

        return new HikariDataSource(config);
    }



    @Bean(name = "oacArchiveEntityManagerFactory")
    @PersistenceContext(unitName = "oacArchiveMember")
    public LocalContainerEntityManagerFactoryBean oacEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("oacMemberArchiveDataSource") DataSource dataSource)
    {
        return builder.dataSource(dataSource).packages("com.copart.member.billing.model.entity").persistenceUnit("oacArchiveMember").build();
    }


    @Bean(name = "oacArchiveTransactionManager")
    public PlatformTransactionManager oacTransactionManager(@Qualifier("oacArchiveEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
