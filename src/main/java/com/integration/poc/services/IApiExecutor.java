package com.integration.poc.services;

import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.ObjectMapper;

public interface IApiExecutor {
  public void executeApi(ApiRequestConfig apiRequest, ObjectMapper mapper);
}
