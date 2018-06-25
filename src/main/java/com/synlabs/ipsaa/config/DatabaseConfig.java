package com.synlabs.ipsaa.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig
{

  @Value("${spring.datasource.driverClassName}")
  private String databaseDriverClassName;

  @Value("${spring.datasource.url}")
  private String datasourceUrl;

  @Value("${spring.datasource.username}")
  private String databaseUsername;

  @Value("${spring.datasource.password}")
  private String databasePassword;

  @Value("${spring.datasource.max-active}")
  private int maximumPoolSize;

  @Primary
  @Bean(name = "dataSource")
  public DataSource dataSource()
  {

    final HikariDataSource ds = new HikariDataSource();
    ds.setMaximumPoolSize(maximumPoolSize);
    ds.setDriverClassName(databaseDriverClassName);
    ds.setJdbcUrl(datasourceUrl);
    ds.setUsername(databaseUsername);
    ds.setPassword(databasePassword);
    return ds;
  }

//  @Primary
//  @Bean(name = "entityManagerFactory")
//  public LocalContainerEntityManagerFactoryBean
//  entityManagerFactory(
//      EntityManagerFactoryBuilder builder,
//      @Qualifier("dataSource") DataSource dataSource
//                      ) {
//    return builder
//        .dataSource(dataSource)
//        .packages("com.synlabs.ipsaa")
//        .persistenceUnit("ipsaa")
//        .build();
//  }

//  @Primary
//  @Bean(name = "transactionManager")
//  public PlatformTransactionManager transactionManager(
//      @Qualifier("entityManagerFactory") EntityManagerFactory
//          entityManagerFactory
//                                                      ) {
//    return new JpaTransactionManager(entityManagerFactory);
//  }


}
