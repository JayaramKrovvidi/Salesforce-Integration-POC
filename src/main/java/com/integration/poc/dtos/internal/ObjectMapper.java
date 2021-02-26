package com.integration.poc.dtos.internal;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectMapper {
	
	private List<String>  apiKey;
	private String mappingKey;
	private List<NameValuePair<String,String >> mappers;
	@Override
	public String toString() {
		return "ObjectMapper [apiKey=" + apiKey + ", mappingKey=" + mappingKey + ", mappers=" + mappers + "]";
	}
	
	
	
	
	

}