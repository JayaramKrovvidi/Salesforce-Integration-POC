package com.integration.poc.services;

import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.GenericApiRequest;

public interface IApiExecutor {
  public String executeApi(ApiRequestConfig apiRequest , String apiKey);

}
