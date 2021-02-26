package com.integration.poc.services;

import java.util.List;
import java.util.Map;

public interface IObjectMapperRefactor {

	public void buildHeader(String[] rows);

	public void buildContent(String[] rows);
	
	public void alterMapper1();

	public void alterMapper2();
	
	public List<String> getHeaderClone();
	
	public List<List <String>> getContentClone();
	
	public void createMapper();
}
