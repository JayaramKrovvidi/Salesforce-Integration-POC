package com.integration.poc.dtos.internal;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostProcessConfig {
  private List<ConvConfig> mappers;

  public PostProcessConfig(List<ConvConfig> mappers) {
    super();
    this.mappers = mappers;
  }

}
