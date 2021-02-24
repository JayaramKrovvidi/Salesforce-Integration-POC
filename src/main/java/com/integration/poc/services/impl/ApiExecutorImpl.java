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
public class ApiExecutorImpl implements IApiExecutor {

  @Autowired
  UrlBuilderUtil urlBuilder;

  @Autowired
  IRestTemplateWrapper restTemplate;

  @Autowired
  IXMLParser xmlParser;

  @Autowired
  IMapBuilder mapBuilder;

  @Override
  public void executeApi(ApiRequestConfig apiRequest) {
    switch (apiRequest.getMethodType()) {
      case "GET":
        executeGet(apiRequest);
        return;
      case "POST":
        executePost(apiRequest);
        return;
      default:
        return;
    }

  }

  private void executeGet(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customGetForEntity(String.class, url, addHeaders(apiRequest));

    storeResponseInMap(apiRequest, apiKey, response);
  }

  private void executePost(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customPostForEntity(String.class, url,
        apiRequest.getRequestBody(), addHeaders(apiRequest));

    storeResponseInMap(apiRequest, apiKey, response);
  }

  // -------------- Helper Methods Start Here -----------------

  private void prepareApiConfigForExecution(ApiRequestConfig apiRequest) {
    // Replace RunTime Parameters
    Util.replaceParamsAtRuntime(mapBuilder, apiRequest.getHeaders());
    Util.replaceParamsAtRuntime(mapBuilder, apiRequest.getRequestParams());
  }

  private void storeResponseInMap(ApiRequestConfig apiRequest, String apiKey, String response) {
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
