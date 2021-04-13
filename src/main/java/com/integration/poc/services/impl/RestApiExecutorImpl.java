package com.integration.poc.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.models.RuntimeVariables;
import com.integration.poc.repositories.IRuntimeVariablesRepository;
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
  
  @Autowired
  IRuntimeVariablesRepository runTimeRepo;
  
  

  @Override
  public String executeApi(ApiRequestConfig apiRequest, String apiKey) {
    Integer wfId = null;
    switch (apiRequest.getMethodType()) {
      case "GET":
        return executeGet(apiRequest, apiKey,wfId);
      case "POST":
        return executePost(apiRequest, apiKey,wfId);
      case "PUT":
        return executePut(apiRequest, apiKey,wfId);
      default:
        return null;
    }

  }

  private String executePut(ApiRequestConfig apiRequest, String apiKey,Integer wfId) {

    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.putForEntity(String.class, url, apiRequest.getRequestBody());

    storeValuesFromResponse(apiRequest, apiKey, response,wfId);
    return response;
  }

  private String executeGet(ApiRequestConfig apiRequest, String apiKey,Integer wfId) {

    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customGetForEntity(String.class, url, addHeaders(apiRequest));
    storeValuesFromResponse(apiRequest, apiKey, response,wfId);
    return response;
  }

  private String executePost(ApiRequestConfig apiRequest, String apiKey,Integer wfId) {

    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customPostForEntity(String.class, url,
        apiRequest.getRequestBody(), addHeaders(apiRequest));

    storeValuesFromResponse(apiRequest, apiKey, response,wfId);
    return response;
  }

  // -------------- Helper Methods Start Here -----------------

  private void prepareApiConfigForExecution(ApiRequestConfig apiRequest) {
    Integer wfId = null;
    Optional<RuntimeVariables> runTime=runTimeRepo.findByWfId(wfId);
    Map<String, Object> map =new HashMap<>();

    if(runTime.isPresent()) {
    RuntimeVariables  runTimeVariables=runTime.get();
    map = runTimeVariables.getValue();
    Util.replaceParamsAtRuntime(map, apiRequest.getHeaders());
    Util.replaceParamsAtRuntime(map, apiRequest.getRequestParams());

    }   
  }

  private void storeValuesFromResponse(ApiRequestConfig apiRequest, String apiKey,
      String response,Integer wfId) {
    Optional<RuntimeVariables> runTime=runTimeRepo.findByWfId(wfId);
    RuntimeVariables runTimeVariables;
    Map<String, Object> map =new HashMap<>();
    if(runTime.isPresent()) {
      runTimeVariables=runTime.get();
    map = runTimeVariables.getValue();
    List<String> storeIds = apiRequest.getStore();
    if (!CollectionUtils.isEmpty(storeIds)) {
      for (String storageId : storeIds) {
        String value = xmlParser.parsedata(response, storageId);
        map = mapBuilder.putMap(map,apiKey, storageId, value);
      }
    }
    runTimeVariables.setValue(map);
    runTimeRepo.save(runTimeVariables);
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
