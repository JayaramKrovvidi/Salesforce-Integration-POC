package com.integration.poc.services.impl;

import java.io.FileReader;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.IRestTemplateWrapper;
import com.integration.poc.utils.UrlBuilderUtil;
import com.integration.poc.utils.Util;

@Service
public class IngestApiExecutorImpl implements IApiExecutor {

  @Autowired
  IRestTemplateWrapper restTemplate;


  @Autowired
  UrlBuilderUtil urlBuilder;

  @Value("${local.file.path}")
  private String localFilePath;

  @Override
  public String executeApi(ApiRequestConfig apiRequest, String apiKey, Integer workFlowId) {
    Map<String, String> requestParams = Util.createMap(apiRequest.getRequestParams());
    String path =
        localFilePath + requestParams.get("apiKey") + requestParams.get("processKey") + ".json";

    try {
      JSONParser jsonParser = new JSONParser();
      FileReader reader = new FileReader(path);
      JSONArray ingestJson = (JSONArray) jsonParser.parse(reader);
      String url = urlBuilder.buildUrl(apiRequest);
      for (int i = 0; i < ingestJson.size(); i++) {
        String in = getIngestJson((JSONObject) ingestJson.get(i));
        String response =
            restTemplate.customPostForEntity(String.class, url, in, addHeaders(apiRequest));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private String getIngestJson(JSONObject obj) {
    JSONParser parser = new JSONParser();
    String ingestFormat =
        "{\"specversion\":\"string\",\"type\":\"string\",\"source\":\"string\",\"subject\":\"string\",\"datacontenttype\":\"string\",\"id\":\"string\"}";
    JSONObject json;
    try {
      json = (JSONObject) parser.parse(ingestFormat);
      json.put("time", "2021-04-09T1352.876010800");
      json.put("data", obj);
      return json.toString();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private HttpHeaders addHeaders(ApiRequestConfig apiRequest) {
    HttpHeaders headers = new HttpHeaders();
    for (NameValuePair<String, String> header : apiRequest.getHeaders()) {
      headers.add(header.getName(), header.getValue());
    }
    return headers;
  }
}