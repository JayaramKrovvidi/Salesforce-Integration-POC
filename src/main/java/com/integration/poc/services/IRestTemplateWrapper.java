package com.integration.poc.services;

import java.util.List;

public interface IRestTemplateWrapper {
  public <T> T getForEntity(Class<T> clazz, String url, Object... uriVariables);

  public <T> List<T> getForList(Class<T> clazz, String url, Object... uriVariables);

  public <T, R> T postForEntity(Class<T> clazz, String url, R body, Object... uriVariables);

  public <T, R> T putForEntity(Class<T> clazz, String url, R body, Object... uriVariables);

  public void delete(String url, Object... uriVariables);
}
