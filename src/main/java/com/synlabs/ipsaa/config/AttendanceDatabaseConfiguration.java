package com.synlabs.ipsaa.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AttendanceDatabaseConfiguration
{
  @Value("${ipsaa.attendance.datasource.driverClassName}")
  private String attendanceDatabaseDriverClassName;

  @Value("${ipsaa.attendance.datasource.url}")
  private String attendanceDatasourceUrl;

  @Value("${ipsaa.attendance.datasource.username}")
  private String attendanceDatabaseUsername;

  @Value("${ipsaa.attendance.datasource.password}")
  private String attendanceDatabasePassword;

  @Value("${ipsaa.attendance.datasource.max-active}")
  private int attendanceMaximumPoolSize;


  @Bean(name = "attendanceDataSource")
  public DataSource attendanceDataSource()
  {
    final HikariDataSource ds = new HikariDataSource();
    ds.setMaximumPoolSize(attendanceMaximumPoolSize);
    ds.setDriverClassName(attendanceDatabaseDriverClassName);
    ds.setJdbcUrl(attendanceDatasourceUrl);
    ds.setUsername(attendanceDatabaseUsername);
    ds.setPassword(attendanceDatabasePassword);
    return ds;
  }
}
