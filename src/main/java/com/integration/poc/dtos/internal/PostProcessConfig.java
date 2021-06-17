package com.integration.poc.dtos.internal;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostProcessConfig {
  private List<ConvConfig> mappers;

  public PostProcessConfig(List<ConvConfig> mappers) {
    super();
    this.mappers = mappers;
  }

}
