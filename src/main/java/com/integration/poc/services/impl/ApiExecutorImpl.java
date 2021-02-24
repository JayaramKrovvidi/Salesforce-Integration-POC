package com.integration.poc.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.IMapBuilder;
import com.integration.poc.services.IRestTemplateWrapper;
import com.integration.poc.services.IXMLParser;
import com.integration.poc.utils.UrlBuilderUtil;


@Service
public class ApiExecutorImpl implements IApiExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiExecutorImpl.class);

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
        return;
      case "POST":
        executePost(apiRequest);
        return;
      default:
        return;
    }
  }

  private void executePost(ApiRequestConfig apiRequest) {

    String url = UrlBuilderUtil.buildUrl(apiRequest);
    String response = restTemplate.customPostForEntity(String.class, url, apiRequest.getRequestBody(),
        addHeaders(apiRequest));
    LOGGER.info("Response after Post: {}", response);

    String value= xmlParser.parsedata(response,apiRequest.getStore().get(0));
    mapBuilder.putMap(apiRequest.getApiKey(), apiRequest.getStore().get(0), value);
  }

  private HttpHeaders addHeaders(ApiRequestConfig apiRequest) {
    HttpHeaders headers = new HttpHeaders();
    for (NameValuePair<String, String> header : apiRequest.getHeaders()) {
      headers.add(header.getName(), header.getValue());
      
    }
    return headers;
  }

}
