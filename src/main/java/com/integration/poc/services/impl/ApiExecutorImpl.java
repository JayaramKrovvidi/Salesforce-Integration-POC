package com.integration.poc.services.impl;

import static com.integration.poc.constants.CommonConstants.CUSTOM_PROCESS_IDENTIFIER;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.enums.ResultProcessorTypeEnum;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.IMapBuilder;
import com.integration.poc.services.IRestTemplateWrapper;
import com.integration.poc.services.IResultProcessor;
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

  @Autowired
  BeanFactoryServiceImpl beanFactoryService;

  @Override
  public void executeApi(ApiRequestConfig apiRequest) {
    switch (apiRequest.getMethodType()) {
      case "GET":
        executeGet(apiRequest);
        return;
      case "POST":
        executePost(apiRequest);
        return;
      case "PUT":
        executePut(apiRequest);
        return;
      default:
        return;
    }

  }

  private void executePut(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.putForEntity(String.class, url, apiRequest.getRequestBody());

    storeValuesFromResponse(apiRequest, apiKey, response);
  }

  private void executeGet(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customGetForEntity(String.class, url, addHeaders(apiRequest));

    storeValuesFromResponse(apiRequest, apiKey, response);
  }

  private void executePost(ApiRequestConfig apiRequest) {
    String apiKey = apiRequest.getApiKey();
    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customPostForEntity(String.class, url,
        apiRequest.getRequestBody(), addHeaders(apiRequest));

    storeValuesFromResponse(apiRequest, apiKey, response);
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
        processAndStoreValueInMap(apiRequest, apiKey, response, storageId);
      }
    }
  }

  private void processAndStoreValueInMap(ApiRequestConfig apiRequest, String apiKey,
      String response, String storageId) {
    if (storageId.startsWith(CUSTOM_PROCESS_IDENTIFIER)) {
      String classKey = storageId.substring(CUSTOM_PROCESS_IDENTIFIER.length());
      IResultProcessor resultProcessor = beanFactoryService
          .getBeanForClass(ResultProcessorTypeEnum.getResultProcessorByKey(classKey));
      resultProcessor.process(apiRequest, apiKey, response);
    } else {
      String value = xmlParser.parsedata(response, storageId);
      mapBuilder.putMap(apiKey, storageId, value);
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
