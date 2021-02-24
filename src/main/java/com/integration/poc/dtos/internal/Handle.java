package com.integration.poc.dtos.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Handle {
  private Integer handlerId;
  private Object operand1;
  private Object operand2;
  private String operator;
}
