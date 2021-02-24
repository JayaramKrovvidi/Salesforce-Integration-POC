package com.integration.poc.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.integration.poc.services.IMapBuilder;
@Service
public class MapBuilderImpl implements IMapBuilder{

	Map<String,Map<String,String>> mapBuilder=new HashMap();
	public void putMap(String apiKey,String id,String obj) {
		
		if(mapBuilder.containsKey(apiKey)) {
		    mapBuilder.get(apiKey).put(id, obj);
		}
		else {
			Map<String,String> innerMap=new HashMap();
			innerMap.put(id,obj);
			mapBuilder.put(apiKey, innerMap);
		}	
	}
	
	public String getMap(String apiKey,String id) {
		return mapBuilder.get(apiKey).get(id);
	}
}
