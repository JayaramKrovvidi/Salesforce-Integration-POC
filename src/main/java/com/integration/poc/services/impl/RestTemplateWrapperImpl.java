package com.integration.poc.services.impl;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.integration.poc.exceptions.Error;
import com.integration.poc.exceptions.GenericError;
import com.integration.poc.exceptions.GenericException;
import com.integration.poc.services.IRestTemplateWrapper;

@Service
public class RestTemplateWrapperImpl implements IRestTemplateWrapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateWrapperImpl.class);
  private static final String NO_DATA_FOUND_MSG = "No data found {}";

  private RestTemplate restTemplate;
  private ObjectMapper objectMapper;

  @Autowired
  public RestTemplateWrapperImpl(RestTemplateBuilder restTemplateBuilder,
      ObjectMapper objectMapper) {
    this.restTemplate = restTemplateBuilder.build();
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> T getForEntity(Class<T> clazz, String url, Object... uriVariables) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
      JavaType javaType = objectMapper.getTypeFactory()
          .constructType(clazz);
      return readValue(response, javaType);
    } catch (HttpClientErrorException exception) {
      if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
        LOGGER.info(NO_DATA_FOUND_MSG, url);
      } else {
        LOGGER.info("rest client exception: {}", exception.getMessage());
      }
      throw new GenericException(
          new GenericError(Error.REST_CLIENT.getErrorCode(), Error.REST_CLIENT.getErrorMsg()));
    }
  }

  @Override
  public <T> List<T> getForList(Class<T> clazz, String url, Object... uriVariables) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
      CollectionType collectionType = objectMapper.getTypeFactory()
          .constructCollectionType(List.class, clazz);
      return readValue(response, collectionType);
    } catch (HttpClientErrorException exception) {
      if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
        LOGGER.info(NO_DATA_FOUND_MSG, url);
      } else {
        LOGGER.info("rest client exception: {}", exception.getMessage());
      }
      throw new GenericException(
          new GenericError(Error.REST_CLIENT.getErrorCode(), Error.REST_CLIENT.getErrorMsg()));
    }
  }

  @Override
  public <T, R> T postForEntity(Class<T> clazz, String url, R body, HttpHeaders headers,
      Object... uriVariables) {
    HttpEntity<R> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> response =
        restTemplate.postForEntity(url, request, String.class, uriVariables);
    JavaType javaType = objectMapper.getTypeFactory()
        .constructType(clazz);
    return readValue(response, javaType);
  }

  @Override
  public <T, R> T putForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
    HttpEntity<R> request = new HttpEntity<>(body);
    ResponseEntity<String> response =
        restTemplate.exchange(url, HttpMethod.PUT, request, String.class, uriVariables);
    JavaType javaType = objectMapper.getTypeFactory()
        .constructType(clazz);
    return readValue(response, javaType);
  }

  @Override
  public void delete(String url, Object... uriVariables) {
    try {
      restTemplate.delete(url, uriVariables);
    } catch (RestClientException exception) {
      LOGGER.info(exception.getMessage());
    }
  }

  private <T> T readValue(ResponseEntity<String> response, JavaType javaType) {
    T result = null;
    if (response.getStatusCode() == HttpStatus.OK
        || response.getStatusCode() == HttpStatus.CREATED) {
      try {
        result = objectMapper.readValue(response.getBody(), javaType);
      } catch (IOException e) {
        LOGGER.info(e.getMessage());
      }
    } else {
      LOGGER.info(NO_DATA_FOUND_MSG, response.getStatusCode());
    }
    return result;
  }

  @Override
  public <T, R> String customPostForEntity(Class<T> clazz, String url, R body, HttpHeaders headers,
      Object... uriVariables) {
    HttpEntity<R> entity = new HttpEntity<>(body, headers);
    ResponseEntity<String> response =
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    return response.getBody();
  }


}
