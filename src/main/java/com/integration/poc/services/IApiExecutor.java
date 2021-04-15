package com.integration.poc.services;

import com.integration.poc.dtos.internal.ApiRequestConfig;

public interface IApiExecutor {
  public String executeApi(ApiRequestConfig apiRequest, String apiKey,Integer workFlowId);

}
