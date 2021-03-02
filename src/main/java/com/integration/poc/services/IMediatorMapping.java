package com.integration.poc.services;

import java.util.List;

public interface IMediatorMapping {
  public List<String> run(List<List<String>> mediatorOutput,String objectMappingOnSuccess);
}
