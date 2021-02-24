package com.integration.poc.utils;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IMapBuilder;
import com.integration.poc.services.impl.MapBuilderImpl;

public class UrlBuilderUtil {
	

  private UrlBuilderUtil() {}

  private static final String EMPTY_STRING = "";

  public static String buildUrl(ApiRequestConfig apiConfig) {
	  	
    StringBuilder urlBuilder = new StringBuilder();  
    urlBuilder.append(apiConfig.getUrl());  
    urlBuilder = new StringBuilder(buildPathParams(urlBuilder.toString()));  
   
//    urlBuilder.append(addPathParams(apiConfig.getPathParams()));
//    urlBuilder.append(addRequestParams(apiConfig.getRequestParams()));
    return urlBuilder.toString();
  }

  private static String buildPathParams(String url) {
	  IMapBuilder mapBuilder = new MapBuilderImpl();
	  
	  StringBuilder urlBuilder = new StringBuilder(url);
	   
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher ans = p.matcher(url);
		while(ans.find()) {
          String group = ans.group(1);            
          String[] split = group.split("\\.");
          String key1 = split[0];
          String key2 = split[1];
          
          String res = (String) mapBuilder.getMap(key1, key2);
          String replace = (urlBuilder+"").replace("{"+group+"}", res);
          urlBuilder=new StringBuilder(replace);      
      }
	  return urlBuilder.toString();
  }
  private static String addPathParams(List<NameValuePair<String, String>> pathParams) {
    if (CollectionUtils.isEmpty(pathParams)) {
      return EMPTY_STRING;
    }
    return "/" + pathParams.stream()
        .map(pair -> String.valueOf(pair.getValue()))
        .collect(Collectors.joining("/"));
  }

  private static String addRequestParams(List<NameValuePair<String, String>> requestParams) {
    if (CollectionUtils.isEmpty(requestParams)) {
      return EMPTY_STRING;
    }
    return "?" + requestParams.stream()
        .map(pair -> pair.getName() + "=" + pair.getValue())
        .collect(Collectors.joining("&"));
  }
}
