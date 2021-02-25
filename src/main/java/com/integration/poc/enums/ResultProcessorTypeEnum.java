package com.integration.poc.enums;

import com.integration.poc.services.IResultProcessor;
import com.integration.poc.services.impl.HeaderMapperResultProcessorImpl;
import lombok.Getter;

@Getter
public enum ResultProcessorTypeEnum {

  HEADERS_MAPPER("mapHeaders", HeaderMapperResultProcessorImpl.class);

  private final String key;
  private final Class<? extends IResultProcessor> resultProcessor;

  private ResultProcessorTypeEnum(String key, Class<? extends IResultProcessor> resultProcessor) {
    this.key = key;
    this.resultProcessor = resultProcessor;
  }

  public static Class<? extends IResultProcessor> getResultProcessorByKey(String processorType) {
    for (ResultProcessorTypeEnum type : values()) {
      if (type.key.equals(processorType)) {
        return type.resultProcessor;
      }
    }
    return null;
  }

}
