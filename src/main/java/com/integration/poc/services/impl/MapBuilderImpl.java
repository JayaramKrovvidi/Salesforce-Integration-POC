package com.integration.poc.services.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import com.integration.poc.services.IMapBuilder;


@Service
@RequestScope
public class MapBuilderImpl implements IMapBuilder {

//  Map<String, Map<String, Object>> mapBuilder = new HashMap<>();

  @Override
  public Map<String,  Object> putMap(Map<String, Object> mapBuilder,String apiKey, String id, Object obj) {
    mapBuilder.putIfAbsent(apiKey, new HashMap<>());
    Map<String,Object> map2=(Map<String, Object>) mapBuilder.get(apiKey);
       map2.putIfAbsent(id, obj);
        mapBuilder.put(apiKey, map2);
    return mapBuilder;
  }

  @Override
  public Object getMap(Map<String,Object> mapBuilder,String apiKey, String id) {
     
        Map<String,Object> innerMap = (Map<String, Object>) mapBuilder.get(apiKey);
        return innerMap.get(id);
        
  }

  
}
