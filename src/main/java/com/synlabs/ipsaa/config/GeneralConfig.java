package com.synlabs.ipsaa.config;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Created by sushil on 07-04-2017.
 */
@Configuration
public class GeneralConfig implements AsyncConfigurer
{

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Value("${spring.mail.port}")
  private int port;

  @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
  private boolean sslEnable;

  @Bean
  public EventBus eventBus()
  {
    return new EventBus();
  }

  @Bean
  public JavaMailSender mailSender()
  {
    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setHost(host);
    sender.setPort(port);
    sender.setUsername(username);
    sender.setPassword(password);
    sender.setJavaMailProperties(getMailProperties());
    return sender;
  }

  private Properties getMailProperties()
  {
    Properties properties = new Properties();
    properties.setProperty("mail.transport.protocol", "smtp");
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.starttls.enable", sslEnable ? "true" : "false");
    properties.setProperty("mail.debug", "false");
    return properties;
  }

  @Bean
  public SimpleMailMessage templateMessage()
  {
    return new SimpleMailMessage();
  }

  @Override
  public Executor getAsyncExecutor()
  {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(50);
    executor.initialize();
    return executor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
  {
    return new AsyncUncaughtExceptionHandler()
    {

      private final Logger logger = LoggerFactory.getLogger(AsyncUncaughtExceptionHandler.class);

      @Override
      public void handleUncaughtException(Throwable throwable, Method method, Object... objects)
      {
        logger.error("Error in async:", throwable);
      }
    };
  }

  @Bean
  public TaskExecutor threadPoolTaskExecutor()
  {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(4);
    executor.setThreadNamePrefix("default_task_executor_thread");
    executor.initialize();
    return executor;
  }
}
