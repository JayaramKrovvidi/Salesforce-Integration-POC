package com.integration.poc.exceptions;

import com.integration.poc.enums.Error;

public class GenericException extends RuntimeException {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The generic error. */
  private final GenericError genericError;

  /**
   * Instantiates a new generic exception.
   *
   * @param e the e
   */
  public GenericException(GenericError e) {
    this.genericError = e;
  }

  public GenericException(Error e, String errorMessage) {
    this.genericError = new GenericError(e.getErrorCode(), e.getErrorMsg() + " : " + errorMessage);
  }

  /**
   * Gets the generic error.
   *
   * @return the generic error
   */
  public GenericError getGenericError() {
    return genericError;
  }

}
