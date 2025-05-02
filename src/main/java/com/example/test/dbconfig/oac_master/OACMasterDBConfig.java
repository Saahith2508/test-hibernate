package com.example.test.dbconfig.oac_master;

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
 * @author vipothamse
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "oacMasterEntityManagerFactory", basePackages = { "com.example.test.repository.oac_master" },
        transactionManagerRef = "oacMasterTransactionManager")
public class OACMasterDBConfig
{
    @Value("${spring.datasource.slow-query-log-threshold:60000}")
    Long slowQueryThresholdMs;

    @Value("${spring.datasource.slow-query-log:true}")
    boolean slowQueryLog = true;

    @Bean
    @ConfigurationProperties(prefix = "spring.oac-master-datasource")
    public HikariConfig oacMasterDataSourceProperties()
    {
        return new HikariConfig();
    }

    @Bean(name = "oacMasterDataSource")
    public DataSource oacMasterDataSource()
    {
        HikariConfig config = oacMasterDataSourceProperties();

        // Slow query log handling (Hikari does not have built-in slow query logging like Tomcat)
        if (slowQueryLog) {
            config.addDataSourceProperty("slowQueryThresholdMs", slowQueryThresholdMs);
            config.addDataSourceProperty("logSlowQueries", true);
        }

        return new HikariDataSource(config);
    }

    @PersistenceContext(unitName = "oacMaster")
    @Bean(name = "oacMasterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oacMasterEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("oacMasterDataSource") DataSource dataSource)
    {
        return builder.dataSource(dataSource).packages("com.example.test.model.entity").persistenceUnit("oacMaster").build();
    }

    @Bean(name = "oacMasterTransactionManager")
    public PlatformTransactionManager oacMasterTransactionManager(@Qualifier("oacMasterEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}