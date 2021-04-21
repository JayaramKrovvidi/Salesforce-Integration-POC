package com.integration.poc.enums;

import com.integration.poc.services.IMediator;
import com.integration.poc.services.impl.CSVMediatorImpl;
import com.integration.poc.services.impl.JSONMediatorImpl;

public enum PostProcessEnum {

  ASSET_OBJ_CONVERSION("AssetConversion", "src\\main\\resources\\lib\\asset-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  CONTACT_OBJ_CONVERSION("ContactConversion", "src\\main\\resources\\lib\\contact-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  COMMUNICATION_CHANNEL_OBJ_CONVERSION("CommunicationChannelConversion", "src\\main\\resources\\lib\\communication-channel-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  INVOICE_OBJ_CONVERSION("InvoiceConversion", "src\\main\\resources\\lib\\invoice-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  INVOICE_LINES_OBJ_CONVERSION("InvoiceLinesConversion", "src\\main\\resources\\lib\\invoice-lines-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  PAYMENT_CONVERSION("PaymentConversion", "src\\main\\resources\\lib\\payment-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  LOCATION_OBJ_CONVERSION("LocationConversion", "src\\main\\resources\\lib\\location-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  GROUP_OBJ_CONVERSION("GroupConversion", "src\\main\\resources\\lib\\location-group-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  CONTACT_INFO_OBJ_CONVERSION("ContactInfoConversion", "src\\main\\resources\\lib\\contact-info-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  FACILITY_OBJ_CONVERSION("FacilityConversion", "src\\main\\resources\\lib\\facility-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_CUSTOMER_OBJ_CONVERSION("AccountCustomerConversion", "src\\main\\resources\\lib\\account-customer-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_COMM_CHANNEL_OBJ_CONVERSION("AccountCommChannelConversion", "src\\main\\resources\\lib\\account-comm-channel-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_USER_COMM_OBJ_CONVERSION("AccountUserCommisionConversion", "src\\main\\resources\\lib\\account-user-commissions-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_DISTRIBUTION_OBJ_CONVERSION("AccountDistributionConversion", "src\\main\\resources\\lib\\account-distribution-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_SETTINGS_CONVERSION("AccountSettingsConversion", "src\\main\\resources\\lib\\account-settings-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_GROUP_OBJ_CONVERSION("AccountGroupConversion", "src\\main\\resources\\lib\\account-group-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_EXTERNAL_IDS_CONVERSION("AccountExternalIdsConversion", "src\\main\\resources\\lib\\account-externalIds-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ACCOUNT_BILLING_CONVERSION("AccountBillingConversion", "src\\main\\resources\\lib\\account-billing-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_CONVERSION("ShipmentConversion", "src\\main\\resources\\lib\\shipment-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_ROUTE_CONVERSION("ShipmentRouteConversion", "src\\main\\resources\\lib\\shipment-route-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_CUSTOMER_ORDER_CONVERSION("ShipmentCustomerOrderConversion", "src\\main\\resources\\lib\\shipment-customerOrder-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_CARRIER_ORDER_CONVERSION("ShipmentCarrierOrderConversion", "src\\main\\resources\\lib\\shipment-carrierOrder-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_EQUIPMENT_CONVERSION("ShipmentEquipmentConversion", "src\\main\\resources\\lib\\shipment-equipment-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_EXTERNALID_CONVERSION("ShipmentExternalIdConversion", "src\\main\\resources\\lib\\shipment-externalId-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_CONTACT_CONVERSION("ShipmentContactConversion", "src\\main\\resources\\lib\\shipment-contact-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_ITEM_CONVERSION("ShipmentItemConversion", "src\\main\\resources\\lib\\shipment-item-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_COST_CONVERSION("ShipmentCostConversion", "src\\main\\resources\\lib\\shipment-cost-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_INVOICE_CONVERSION("ShipmentInvoiceConversion", "src\\main\\resources\\lib\\shipment-invoice-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_PAYMENT_CONVERSION("ShipmentPaymentConversion", "src\\main\\resources\\lib\\shipment-payment-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  SHIPMENT_DEDUCTION_CONVERSION("ShipmentDeductionConversion", "src\\main\\resources\\lib\\shipment-deduction-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  USER_CONVERSION("UserConversion", "src\\main\\resources\\lib\\user-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  USER_COMM_CHANNEL_CONVERSION("UserCommChannelConversion", "src\\main\\resources\\lib\\user-communication-channel-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  USER_GROUP_CONVERSION("UserGroupConversion", "src\\main\\resources\\lib\\user-group-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  ORDER_CONVERSION("OrderConversion", "src\\main\\resources\\lib\\order-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  GROUP_CONVERSION("GroupObjectConversion", "src\\main\\resources\\lib\\group-conversion-config.json", CSVMediatorImpl.class, CSVMediatorImpl.class),
  CUSTOMER_INGEST_CONVERSION("CustomerIngestConversion","",CSVMediatorImpl.class, JSONMediatorImpl.class),
  ORDER_INGEST_CONVERSION("OrderIngestConversion","",CSVMediatorImpl.class, JSONMediatorImpl.class),
  SHIPMENT_INGEST_CONVERSION("ShipmentIngestConversion","",CSVMediatorImpl.class, JSONMediatorImpl.class),
  LOCATION_INGEST_CONVERSION("LocationIngestConversion","",CSVMediatorImpl.class, JSONMediatorImpl.class),
  ASSET_INGEST_CONVERSION("AssetIngestConversion","",CSVMediatorImpl.class, JSONMediatorImpl.class);



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
