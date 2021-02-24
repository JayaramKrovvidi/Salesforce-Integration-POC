package com.integration.poc.services;

public interface IMapBuilder {
	public void putMap(String apiKey,String id,Object obj);
	public Object getMap(String apiKey,String id);
}
