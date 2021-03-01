package com.integration.poc.services;

import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.ObjectMapper;

public interface IResultProcessor {
  public void process(ApiRequestConfig apiRequest, String apiKey, String response,
      ObjectMapper mapper);
}
