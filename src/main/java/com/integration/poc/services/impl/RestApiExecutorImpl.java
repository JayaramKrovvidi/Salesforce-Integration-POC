package com.integration.poc.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.IMapBuilder;
import com.integration.poc.services.IRestTemplateWrapper;
import com.integration.poc.services.IXMLParser;
import com.integration.poc.utils.UrlBuilderUtil;
import com.integration.poc.utils.Util;

@Service
public class RestApiExecutorImpl implements IApiExecutor {

  @Autowired
  UrlBuilderUtil urlBuilder;

  @Autowired
  IRestTemplateWrapper restTemplate;

  @Autowired
  IXMLParser xmlParser;

  @Autowired
  IMapBuilder mapBuilder;

  @Override
  public String executeApi(ApiRequestConfig apiRequest) {
    switch (apiRequest.getMethodType()) {
      case "GET":
        return executeGet(apiRequest);
      case "POST":
        return executePost(apiRequest);
      case "PUT":
        return executePut(apiRequest);
      default:
        return null;
    }

  }

  private String executePut(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.putForEntity(String.class, url, apiRequest.getRequestBody());

    storeValuesFromResponse(apiRequest, apiKey, response);
    return response;
  }

  private String executeGet(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customGetForEntity(String.class, url, addHeaders(apiRequest));

    storeValuesFromResponse(apiRequest, apiKey, response);
    return response;
  }

  private String executePost(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customPostForEntity(String.class, url,
        apiRequest.getRequestBody(), addHeaders(apiRequest));

    storeValuesFromResponse(apiRequest, apiKey, response);
    return response;
  }

  // -------------- Helper Methods Start Here -----------------

  private void prepareApiConfigForExecution(ApiRequestConfig apiRequest) {
    // Replace RunTime Parameters
    Util.replaceParamsAtRuntime(mapBuilder, apiRequest.getHeaders());
    Util.replaceParamsAtRuntime(mapBuilder, apiRequest.getRequestParams());
  }

  private void storeValuesFromResponse(ApiRequestConfig apiRequest, String apiKey,
      String response) {
    List<String> storeIds = apiRequest.getStore();
    if (!CollectionUtils.isEmpty(storeIds)) {
      for (String storageId : storeIds) {
        String value = xmlParser.parsedata(response, storageId);
        mapBuilder.putMap(apiKey, storageId, value);
      }
    }
  }

  private HttpHeaders addHeaders(ApiRequestConfig apiRequest) {
    HttpHeaders headers = new HttpHeaders();
    for (NameValuePair<String, String> header : apiRequest.getHeaders()) {
      headers.add(header.getName(), header.getValue());
    }
    return headers;
  }

}
