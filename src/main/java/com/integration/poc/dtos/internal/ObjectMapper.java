package com.integration.poc.dtos.internal;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectMapper {

  private List<String> apiKey;
  private String mappingKey;
  private List<NameValuePair<String, String>> mappers;

}
