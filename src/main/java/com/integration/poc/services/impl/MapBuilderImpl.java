package com.integration.poc.services.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.integration.poc.services.IMapBuilder;


@Service
public class MapBuilderImpl implements IMapBuilder {

  Map<String, Map<String, Object>> mapBuilder = new HashMap<>();

  @Override
  public void putMap(String apiKey, String id, Object obj) {
    mapBuilder.putIfAbsent(apiKey, new HashMap<>());
    mapBuilder.get(apiKey)
        .putIfAbsent(id, obj);
  }

  @Override
  public Object getMap(String apiKey, String id) {
    return mapBuilder.get(apiKey)
        .get(id);
  }
}
