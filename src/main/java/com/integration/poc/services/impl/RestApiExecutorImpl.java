package com.integration.poc.services.impl;

import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import com.integration.poc.utils.RequestBodyBuilderUtil;
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
  RequestBodyBuilderUtil requestBuilder;

  @Override
  public String executeApi(ApiRequestConfig apiRequest, String apiKey) {
    switch (apiRequest.getMethodType()) {
      case "GET":
        return executeGet(apiRequest, apiKey);
      case "POST":
        return executePost(apiRequest, apiKey);
      case "PUT":
        return executePut(apiRequest, apiKey);
      case "PATCH":
        return executePatch(apiRequest, apiKey);
      default:
        return null;
    }

  }

  private String executePut(ApiRequestConfig apiRequest, String apiKey) {

    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
     String apiRequestBody = requestBuilder.buildRequestody(apiRequest);
    System.out.println(url);
    String response = restTemplate.customPutForEntity(String.class, url, apiRequestBody, addHeaders(apiRequest));
    System.out.println(response);
    storeValuesFromResponse(apiRequest, apiKey, response);
    return response;
  }

  private String executeGet(ApiRequestConfig apiRequest, String apiKey) {

    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String response = restTemplate.customGetForEntity(String.class, url, addHeaders(apiRequest));
    storeValuesFromResponse(apiRequest, apiKey, response);
    return response;
  }

  private String executePost(ApiRequestConfig apiRequest, String apiKey) {

    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String apiRequestBody = requestBuilder.buildRequestody(apiRequest);
    String response =
        restTemplate.customPostForEntity(String.class, url, apiRequestBody, addHeaders(apiRequest));

    storeValuesFromResponse(apiRequest, apiKey, response);
    return response;
  }
  private String executePatch(ApiRequestConfig apiRequest, String apiKey) {

    prepareApiConfigForExecution(apiRequest);

    // Build and execute external api
    String url = urlBuilder.buildUrl(apiRequest);
    String apiRequestBody = requestBuilder.buildRequestody(apiRequest);
    String response =
        restTemplate.customPatchForEntity(String.class, url, apiRequestBody, addHeaders(apiRequest));

    storeValuesFromResponse(apiRequest, apiKey, response);
    return response;
  }


  // -------------- Helper Methods Start Here -----------------

  private void prepareApiConfigForExecution(ApiRequestConfig apiRequest) {
    // Replace RunTime Parameters
    Util.replaceParamsAtRuntime(mapBuilder, apiRequest.getHeaders());
    Util.replaceParamsAtRuntime(mapBuilder, apiRequest.getRequestParams());
  }

  private void storeValuesFromResponse(ApiRequestConfig apiRequest, String apiKey, String response) {
    List<String> storeIds = apiRequest.getStore();
    if (!CollectionUtils.isEmpty(storeIds)) {
        for (String storageId : storeIds) {
            if (storageId.startsWith("*")) {
                int indexOf = storageId.indexOf('.');
                String substring = storageId.substring(indexOf + 1);
                mapBuilder.putMap(apiKey, substring, response);
            } else {
                String value = parseData(apiRequest, response, storageId); 
                mapBuilder.putMap(apiKey, storageId, value);
            }

        }
    }
}

private String parseData(ApiRequestConfig apiRequest, String response, String storageId) {  
    List<NameValuePair<String, String>> headers  = apiRequest.getHeaders();
    for (NameValuePair<String, String> header:  headers ) {
        if(header.getName().equals("Accept") && header.getValue().equals("application/json")) {
            return parseJSON(response, storageId);
        }
    }
    return xmlParser.parsedata(response, storageId);
}

private String parseJSON(String response, String storageId) {
  JSONParser parser = new JSONParser();
    JSONObject jsonObj;
    try {
      jsonObj = (JSONObject) parser.parse(response);
      return (String) jsonObj.get(storageId);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
}


  private HttpHeaders addHeaders(ApiRequestConfig apiRequest) {
    HttpHeaders headers = new HttpHeaders();
    for (NameValuePair<String, String> header : apiRequest.getHeaders()) {
      if (header.getName()
          .equals("Authorization")) {
        headers.add(header.getName(), "Bearer " + header.getValue());
      } else {
        headers.add(header.getName(), header.getValue());
      }
    }
    return headers;
  }

}
