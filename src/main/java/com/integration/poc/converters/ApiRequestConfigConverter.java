package com.integration.poc.converters;

import javax.persistence.AttributeConverter;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.poc.dtos.internal.ApiRequestConfig;


public class ApiRequestConfigConverter implements AttributeConverter<ApiRequestConfig, String> {

  @Override
  public String convertToDatabaseColumn(ApiRequestConfig attribute) {
    JSONObject json = new JSONObject(attribute);
    return json.toString();
  }

  @Override
  public ApiRequestConfig convertToEntityAttribute(String dbData) {

    try {
      JSONObject json = new JSONObject(dbData);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json.toString(), ApiRequestConfig.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }


}
