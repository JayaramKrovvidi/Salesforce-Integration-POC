package com.integration.poc.enums;

import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.impl.DatabaseExecutorImpl;
import com.integration.poc.services.impl.FtpApiExecutorImpl;
import com.integration.poc.services.impl.RestApiExecutorImpl;

public enum AdaptersEnum {

  REST_ADAPTER("REST", RestApiExecutorImpl.class),
  FTP_ADAPTER("FTP", FtpApiExecutorImpl.class),
  DB_ADAPTER("DATABASE", DatabaseExecutorImpl.class);

  private final String requestType;
  private final Class<? extends IApiExecutor> adapter;

  private AdaptersEnum(String requestType, Class<? extends IApiExecutor> adapter) {
    this.requestType = requestType;
    this.adapter = adapter;
  }

  public static Class<? extends IApiExecutor> getAdapterByKey(String requestType) {
    for (AdaptersEnum type : values()) {
      if (type.requestType.equals(requestType)) {
        return type.adapter;
      }
    }
    return null;
  }

}
