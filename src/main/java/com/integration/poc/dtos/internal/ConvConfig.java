package com.integration.poc.dtos.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvConfig {
  private String sourceId;
  private String destId;

  public ConvConfig(String sourceId, String destId) {
    super();
    this.sourceId = sourceId;
    this.destId = destId;
  }

}
