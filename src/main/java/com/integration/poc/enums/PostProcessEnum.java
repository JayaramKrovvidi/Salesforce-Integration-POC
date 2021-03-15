package com.integration.poc.enums;

import com.integration.poc.services.IMediator;
import com.integration.poc.services.impl.CSVMediatorImpl;

public enum PostProcessEnum {

  ASSET_OBJ_CONVERSION("AssetConversion", "src\\main\\resources\\lib\\asset-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  CONTACT_OBJ_CONVERSION("ContactConversion", "src\\main\\resources\\lib\\contact-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  COMMUNICATION_CHANNEL_OBJ_CONVERSION("CommunicationChannelConversion", "src\\main\\resources\\lib\\communication-channel-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  INVOICE_OBJ_CONVERSION("InvoiceConversion", "src\\main\\resources\\lib\\invoice-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  INVOICE_LINES_OBJ_CONVERSION("InvoiceLinesConversion", "src\\main\\resources\\lib\\invoice-lines-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  PAYMENT_CONVERSION("PaymentConversion", "src\\main\\resources\\lib\\payment-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  LOCATION_OBJ_CONVERSION("LocationConversion", "src\\main\\resources\\lib\\location-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  GROUP_OBJ_CONVERSION("GroupConversion", "src\\main\\resources\\lib\\group-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  CONTACT_INFO_OBJ_CONVERSION("ContactInfoConversion", "src\\main\\resources\\lib\\contact-info-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  FACILITY_OBJ_CONVERSION("FacilityConversion", "src\\main\\resources\\lib\\facility-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_CUSTOMER_OBJ_CONVERSION("AccountCustomerConversion", "src\\main\\resources\\lib\\account-customer-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_COMM_CHANNEL_OBJ_CONVERSION("AccountCommChannelConversion", "src\\main\\resources\\lib\\account-comm-channel-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class);

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
