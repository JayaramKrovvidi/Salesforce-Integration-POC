package com.integration.poc.services;

import java.util.Map;

public interface IMapBuilder {
  public Map<String, Object> putMap(Map<String,Object> mapBuilder,String apiKey, String id, Object obj);

  public Object getMap(Map<String,  Object> mapBuilder,String apiKey, String id);
}
