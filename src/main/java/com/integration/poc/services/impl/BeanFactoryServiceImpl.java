package com.integration.poc.services.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class BeanFactoryServiceImpl implements ApplicationContextAware {

  /** The app application context. */
  ApplicationContext appApplicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.appApplicationContext = applicationContext;
  }

  public <T> T getBeanForClass(Class<T> beanClass) {
    return appApplicationContext.getBean(beanClass);
  }

}
