package com.integration.poc.utils;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;

public class UrlBuilderUtil {

  private UrlBuilderUtil() {}

  private static final String EMPTY_STRING = "";

  public static String buildUrl(ApiRequestConfig apiConfig) {
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(apiConfig.getUrl());
    urlBuilder.append(addPathParams(apiConfig.getPathParams()));
    urlBuilder.append(addRequestParams(apiConfig.getRequestParams()));
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
