package com.integration.poc.exceptions;

import java.io.Serializable;

public class GenericError implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 3817672966923310311L;

  /** The error code. */
  private int errorCode;

  /** The error message. */
  private String errorMessage;

  /** Instantiates a new generic error. */
  public GenericError() {
    super();
  }

  /**
   * Instantiates a new generic error.
   *
   * @param errorCode the error code
   * @param errorMessage the error message
   */
  public GenericError(final int errorCode, final String errorMessage) {
    super();
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  /**
   * Gets the error code.
   *
   * @return the error code
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Gets the error message.
   *
   * @return the error message
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Sets the error code.
   *
   * @param errorCode the new error code
   */
  public void setErrorCode(final int errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * Sets the error message.
   *
   * @param errorMessage the new error message
   */
  public void setErrorMessage(final String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
