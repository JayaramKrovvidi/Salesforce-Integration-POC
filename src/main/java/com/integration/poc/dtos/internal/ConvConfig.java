package com.integration.poc.dtos.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvConfig {
  private String sourceId;
  private String destId;
  private String defaultId;
  public ConvConfig(String sourceId, String destId,String defaultId) {
    super();
    this.sourceId = sourceId;
    this.destId = destId;
    this.defaultId=defaultId;
  }

}
