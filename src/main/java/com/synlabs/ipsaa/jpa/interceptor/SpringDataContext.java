/*
 * See the file license.txt in the main project folder
 * for license and other legal information.
 */

package com.synlabs.ipsaa.jpa.interceptor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringDataContext implements ApplicationContextAware
{
  private static ApplicationContext CONTEXT;

  public void setApplicationContext(final ApplicationContext context) throws BeansException
  {
    CONTEXT = context;
  }

  public static <T> T getBean(Class<T> clazz)
  {
    return CONTEXT.getBean(clazz);
  }
}
