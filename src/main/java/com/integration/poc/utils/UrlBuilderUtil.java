package com.integration.poc.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;

public class UrlBuilderUtil {

  private UrlBuilderUtil() {}

  private static final String EMPTY_STRING = "";

  public static String buildUrl(ApiRequestConfig apiConfig) {
	  System.out.println(apiConfig.getUrl());
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(apiConfig.getUrl());
//    buildPathParams(apiConfig.getUrl());
    urlBuilder.append(addPathParams(apiConfig.getPathParams()));
    urlBuilder.append(addRequestParams(apiConfig.getRequestParams()));
    return urlBuilder.toString();
  }

  private static String buildPathParams(String url) {
	  
	  Map<String, Map<String, String>> mainMap = new HashMap<>();
		Map<String, String> insideMap1 = new HashMap<String, String>();
		insideMap1.put("sessionId", "1222222");
		Map<String, String> insideMap2 = new HashMap<String, String>();
		insideMap2.put("id", "99999");
		mainMap.put("1", insideMap1);
		mainMap.put("2", insideMap2);
	   
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher ans = p.matcher(url);
		while(ans.find()) {
          String group = ans.group(1);            
          String[] split = group.split("\\.");
          String key1 = split[0];
          String key2 = split[1];
          String res = mainMap.get(key1).get(key2);
          String replace = url.replace("{"+group+"}", res);
          System.out.println(replace);
         
      }
	  return  "";
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
