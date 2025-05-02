package com.example.test.dbconfig.member_billing;

import com.example.test.repository.BDSBaseJPARepositoryFactoryBean;
import com.example.test.repository.BDSBaseJPARepositoryImpl;
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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "memberBillingEntityManagerFactory", basePackages = { "com.example.test.repository.member_billing" },
        transactionManagerRef = "memberBillingTransactionManager", repositoryBaseClass = BDSBaseJPARepositoryImpl.class,repositoryFactoryBeanClass = BDSBaseJPARepositoryFactoryBean.class)
public class MemberBillingDataStoreConfig
{
    @Value("${spring.datasource.slow-query-log-threshold:60000}")
    Long slowQueryThresholdMs;

    @Value("${spring.datasource.slow-query-log:true}")
    boolean slowQueryLog = true;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariConfig memberBillingDataSourceProperties() {
        return new HikariConfig();
    }

    @Bean(name = "memberBillingDataSource")
    @Primary
    public DataSource memberBillingDataSource() {
        HikariConfig config = memberBillingDataSourceProperties();

        // Slow query log handling (Hikari does not have built-in slow query logging like Tomcat)
        if (slowQueryLog) {
            config.addDataSourceProperty("slowQueryThresholdMs", slowQueryThresholdMs);
            config.addDataSourceProperty("logSlowQueries", true);
        }

        return new HikariDataSource(config);
    }

    @Primary
    @PersistenceContext(unitName = "memberBilling")
    @Bean(name = "memberBillingEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean memberBillingEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("memberBillingDataSource") DataSource dataSource)
    {
        return builder.dataSource(dataSource).packages("com.example.test.model.entity").persistenceUnit("memberBilling").build();
    }

    @Primary
    @Bean(name = "memberBillingTransactionManager")
    public PlatformTransactionManager memberBillingTransactionManager(@Qualifier("memberBillingEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }


}