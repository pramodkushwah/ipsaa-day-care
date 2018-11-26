package com.synlabs.ipsaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class IPSAAServer extends SpringBootServletInitializer
{
  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(IPSAAServer.class, args);
  }
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(IPSAAServer.class);
  }

  @PostConstruct
  public void init(){
    // Setting Spring Boot SetTimeZone
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
  }

}
