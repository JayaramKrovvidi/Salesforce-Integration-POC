package com.integration.poc.dtos.external;

import java.util.List;

import com.integration.poc.dtos.internal.GenericApiRequest;
import com.integration.poc.dtos.internal.PostProcessConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompositeApiRequest {
  private List<GenericApiRequest> requestList;
  private List<PostProcessConfig> objectMapper;
}
