package com.integration.poc.dtos.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvConfig {
  private String sourceId;
  private String destId;
  private String defaultValue;

  public ConvConfig(String sourceId, String destId, String defaultValue) {
    super();
    this.sourceId = sourceId;
    this.destId = destId;
    this.defaultValue = defaultValue;
  }

}
