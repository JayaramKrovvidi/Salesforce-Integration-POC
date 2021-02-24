package com.integration.poc.dtos.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameValuePair<K, V> {
  private K name;
  private V value;
}
