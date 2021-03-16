package com.integration.poc.dtos.internal;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericApiRequest {
  private ApiRequestConfig apiRequest;
  private List<String> onSuccess;
  private List<String> onFailure;
  private String retry;
  private String apiKey;
}
