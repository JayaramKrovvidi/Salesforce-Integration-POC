package com.integration.poc.enums;

public enum Error {
  NO_DATA_FOUND(1, "No Data Found Error"),
  REST_CLIENT(2, "Rest Client Exception");

  private final int errorCode;
  private final String errorMsg;

  private Error(int errorCode, String errorMsg) {
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }

  public int getErrorCode() {
    return this.errorCode;
  }

  public String getErrorMsg() {
    return this.errorMsg;
  }
}
