package com.integration.poc.enums;

import com.integration.poc.services.IMediator;
import com.integration.poc.services.impl.CSVMediatorImpl;
import com.integration.poc.services.impl.JSONMediatorImpl;

public enum FormatterEnum {

  CSV_FORMATTER("csv", CSVMediatorImpl.class),
  JSON_FORMATTER("json", JSONMediatorImpl.class);

  private final String key;
  private final Class<? extends IMediator> formatter;

  private FormatterEnum(String key, Class<? extends IMediator> formatter) {
    this.key = key;
    this.formatter = formatter;
  }

  public static Class<? extends IMediator> getFormatterByKey(String customKey) {
    for (FormatterEnum type : values()) {
      if (type.key.equals(customKey)) {
        return type.formatter;
      }
    }
    return null;
  }

}
