package com.integration.poc.services.impl;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.ICompositeApiRunner;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.FTPClientUtil;
import com.opencsv.CSVReader;

@Service("SalesforceBatchResultProcessor")
public class HeaderMapperResultProcessorImpl implements IResultProcessor {
	
//	@Autowired
//	FTPClientUtil ftpClient;
	
	@Autowired
	ICompositeApiRunner compositeRunner;
	

	@Override
	public void process(ApiRequestConfig apiRequest, String apiKey, String response) {
	
		List<NameValuePair<String, String>> m = compositeRunner.getObjectMapper().get(0).getMappers();
        Map<String,Map<String,String>> mappers=new HashMap<String, Map<String,String>>();
        for(NameValuePair<String, String>nameValuePair:m) {
        	Map<String,String> innermap=new HashMap<String, String>();
        	innermap.put("value", nameValuePair.getValue());
        	innermap.put("visited","false");
            mappers.put(nameValuePair.getName(), innermap);
        }
        List<String> header=new ArrayList();
        List<String> headerClone=new ArrayList();
		String[] rows = response.split("\\r?\\n");
		String[] rowcontent=rows[0].split(",");		
		for(int i=0;i<rowcontent.length;i++) {
			header.add(rowcontent[i].replaceAll("^[\"']+|[\"']+$", ""));
			headerClone.add(rowcontent[i].replaceAll("^[\"']+|[\"']+$", ""));
		}
		List<List <String>> content=new ArrayList();
		List<List <String>> contentClone=new ArrayList();
		for(int i=1;i<rows.length;i++) {
			String[] linecontent=rows[i].split(",");	
			List <String> data=new ArrayList();
			for(int j=0;j<linecontent.length;j++) {
		      	data.add(linecontent[j].replaceAll("^[\"']+|[\"']+$", ""));
			}
			content.add(data);
			contentClone.add(data);
		}
//	 case 1 2
		for(String i:header) {
			//System.out.println(i);
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
	//	case 3 
		
		for(String i:mappers.keySet()) {
			if(mappers.get(i).get("visited")=="false") {
				headerClone.add(mappers.get(i).get("value"));
				int contentsize=content.size();
				for(int j=0;j<contentsize;j++) {
					contentClone.get(j).add("");
				}
			}
		}
		
		
		List<String> newRows = new ArrayList<>();
//		newRows.add(
//				"id,vehicle_type,vin,notes,account.id,license.number,license.country,license.state,asset.number,make,model,size,class,color,weight,status,sensor.provider,sensor.deviceId,sensor.customId");

		for (int i = 1; i < rows.length; i++) {
			newRows.add(rows[i]);
		}
		String fileName = "asset_" + System.currentTimeMillis() + ".csv";
		String filePath = "D:\\Salesforce POC\\"+ fileName;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filePath);
			for (String row : newRows) {
				fileWriter.append(row);
				fileWriter.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		ftpClient.upload(filePath, fileName);
		
	}
}

