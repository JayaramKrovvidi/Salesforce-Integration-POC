package com.integration.poc.dtos.internal;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRequestConfig {
  private String apiKey;
  private String url;
  private List<NameValuePair<String, String>> pathParams;
  private List<NameValuePair<String, String>> requestParams;
  private String methodType;
  private List<NameValuePair<String, String>> headers;
  private Object requestBody;
  private String retry;
  private List<String> store;
  private List<Handle> successHandlers;
  private List<String> procKeyOnSuccess;
  private List<String> procKeyOnFailure;
}
