package com.integration.poc.enums;

import com.integration.poc.services.IMediator;
import com.integration.poc.services.impl.CSVMediatorImpl;

public enum PostProcessEnum {

  ASSET_OBJ_CONVERSION("AssetConversion", "D:\\eclipse-workspace\\Turvo\\poc\\Salesforce-Integration-POC\\src\\main\\resources\\lib\\asset-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class);

  private final String key;
  private final String conversionConfig;
  private final Class<? extends IMediator> inputFormatter;
  private final Class<? extends IMediator> outputFormatter;

  private PostProcessEnum(String key, String conversionConfig,
      Class<? extends IMediator> inputFormatter, Class<? extends IMediator> outputFormatter) {
    this.key = key;
    this.conversionConfig = conversionConfig;
    this.inputFormatter = inputFormatter;
    this.outputFormatter = outputFormatter;
  }


  public static Class<? extends IMediator> getInputFormatterByKey(String customKey) {
    for (PostProcessEnum type : values()) {
      if (type.key.equals(customKey)) {
        return type.inputFormatter;
      }
    }
    return null;
  }

  public static Class<? extends IMediator> getOutputFormatterByKey(String customKey) {
    for (PostProcessEnum type : values()) {
      if (type.key.equals(customKey)) {
        return type.outputFormatter;
      }
    }
    return null;
  }

  public static String getConfigFilePath(String customKey) {
    for (PostProcessEnum type : values()) {
      if (type.key.equals(customKey)) {
        return type.conversionConfig;
      }
    }
    return null;
  }

}
