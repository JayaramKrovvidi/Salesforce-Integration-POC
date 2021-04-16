package com.integration.poc.services;

public interface IMapBuilder {


  public Object getValue(Integer wfId, String apiKey, String id);


  public void putValue(Integer wfId, String apiKey, String id, Object obj);
}
