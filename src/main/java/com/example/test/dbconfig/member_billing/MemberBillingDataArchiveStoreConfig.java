package com.example.test.dbconfig.member_billing;


import com.example.test.dbconfig.HibernateArchiveInterceptor;
import com.example.test.repository.BDSBaseJPARepositoryFactoryBean;
import com.example.test.repository.BDSBaseJPARepositoryImpl;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "memberBillingEntityArchiveManagerFactory", basePackages = { "com.example.test.repository.member_billing_archive" },
        transactionManagerRef = "memberBillingArchiveTransactionManager", repositoryBaseClass = BDSBaseJPARepositoryImpl.class,repositoryFactoryBeanClass = BDSBaseJPARepositoryFactoryBean.class)
public class MemberBillingDataArchiveStoreConfig
{
    @Value("${spring.datasource.slow-query-log-threshold:60000}")
    Long slowQueryThresholdMs;

    @Value("${spring.datasource.slow-query-log:true}")
    boolean slowQueryLog = true;

    @Bean
    @ConfigurationProperties(prefix = "spring.member-billing-archive-datasource")
    public HikariConfig memberBillingArchiveDataSourceProperties()
    {
        return new HikariConfig();
    }

    @Bean(name = "memberBillingArchiveDataSource")
    public DataSource memberBillingArchiveDataSource()
    {
        HikariConfig config = memberBillingArchiveDataSourceProperties();

        // Slow query log handling (Hikari does not have built-in slow query logging like Tomcat)
        if (slowQueryLog) {
            config.addDataSourceProperty("slowQueryThresholdMs", slowQueryThresholdMs);
            config.addDataSourceProperty("logSlowQueries", true);
        }

        return new HikariDataSource(config);
    }



    @PersistenceContext(unitName = "memberBillingArchive")
    @Bean(name = "memberBillingEntityArchiveManagerFactory")
    public LocalContainerEntityManagerFactoryBean memberBillingEntityArchiveManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("memberBillingArchiveDataSource") DataSource dataSource)
    {
        return builder.dataSource(dataSource).packages("com.example.test.model.entity").persistenceUnit("memberBillingArchive").build();
    }


    @Bean(name = "memberBillingArchiveTransactionManager")
    public PlatformTransactionManager memberBillingArchiveTransactionManager(@Qualifier("memberBillingEntityArchiveManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name = "HibernateArchiveInterceptor")
    public HibernateArchiveInterceptor memberBillingHibernateInterceptor()
    {
        return new HibernateArchiveInterceptor();
    }

}