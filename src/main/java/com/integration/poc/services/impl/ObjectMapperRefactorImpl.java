package com.integration.poc.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.ICompositeApiRunner;
import com.integration.poc.services.IObjectMapperRefactor;

@Service
public class ObjectMapperRefactorImpl implements IObjectMapperRefactor {
	
	private static String REGEX = "^[\"']+|[\"']+$";
	List<String> header=new ArrayList<String>();
    List<String> headerClone=new ArrayList<String>();
    List<List <String>> content=new ArrayList<List<String>>();
	List<List <String>> contentClone=new ArrayList<List<String>>();
    Map<String,Map<String,String>> mappers=new HashMap<String, Map<String,String>>();
    
    @Autowired
	ICompositeApiRunner compositeRunner;
    
    public void createMapper() {
		List<NameValuePair<String, String>> m = compositeRunner.getObjectMapper().get(0).getMappers();
    	 for(NameValuePair<String, String>nameValuePair:m) {
         	Map<String,String> innermap=new HashMap<String, String>();
         	innermap.put("value", nameValuePair.getValue());
         	innermap.put("visited","false");
             mappers.put(nameValuePair.getName(), innermap);
         }
    }

	public void buildHeader(String[] rows){
		String[] rowcontent=rows[0].split(",");	
		for(int i=0;i<rowcontent.length;i++) {
			header.add(rowcontent[i].replaceAll(REGEX, ""));
			headerClone.add(rowcontent[i].replaceAll(REGEX, ""));
		}
	}
	
	public void buildContent(String[] rows) {
		for(int i=1;i<rows.length;i++) {
			String[] linecontent=rows[i].split(",");	
			List <String> data=new ArrayList<String>();
			for(int j=0;j<linecontent.length;j++) {
		      	data.add(linecontent[j].replaceAll(REGEX, ""));
			}
			content.add(data);
			contentClone.add(data);
		}	
	}

	//if all name,value present in response and if extra header present in resposne
	public void alterMapper1() {
		for(String i:header) {
			if(mappers.containsKey(i)) {
			   mappers.get(i).put("visited","true");
			   headerClone.set(header.indexOf(i),mappers.get(i).get("value"));		   
			}
			else {
				int removeIndex=headerClone.indexOf(i);
				headerClone.remove(i);
				int contentsize=content.size();				
				for(int j=0;j<contentsize;j++) {
					contentClone.get(j).remove(removeIndex);					
				}			
			}
		}
	}
	
	// if configuration contains extra name value which are not present in response
	public void alterMapper2() {
		for(String i:mappers.keySet()) {
			if(mappers.get(i).get("visited")=="false") {
				headerClone.add(mappers.get(i).get("value"));
				int contentsize=content.size();
				for(int j=0;j<contentsize;j++) {
					contentClone.get(j).add("");
				}
			}
		}
	}

	@Override
	public List<String> getHeaderClone() {
		return headerClone;
	}

	@Override
	public List<List<String>> getContentClone() {
		return contentClone;
	}
}
