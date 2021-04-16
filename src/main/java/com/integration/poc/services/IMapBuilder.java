package com.integration.poc.services;

import java.util.Map;

public interface IMapBuilder {


  public Object getValue(Integer wfId, String apiKey, String id);


  public void putValue(Integer wfId, String apiKey, String id, Object obj);
}
