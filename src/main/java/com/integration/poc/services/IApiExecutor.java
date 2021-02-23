package com.integration.poc.services;

import com.integration.poc.dtos.internal.ApiRequestConfig;

public interface IApiExecutor {
  public void executeApi(ApiRequestConfig apiRequest);
}
