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
import com.integration.poc.services.IObjectMapperRefactor;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.FTPClientUtil;
import com.opencsv.CSVReader;

@Service("SalesforceBatchResultProcessor")
public class HeaderMapperResultProcessorImpl implements IResultProcessor {
	
//	@Autowired
//	FTPClientUtil ftpClient;
		
	@Autowired
	IObjectMapperRefactor objectMapperRefactor;
	

	@Override
	public void process(ApiRequestConfig apiRequest, String apiKey, String response) {
		String[] rows = response.split("\\r?\\n");
		objectMapperRefactor.createMapper();
		objectMapperRefactor.buildHeader(rows);		
		objectMapperRefactor.buildContent(rows);
		objectMapperRefactor.alterMapper1();
		objectMapperRefactor.alterMapper2();
		List<String> headerClone = objectMapperRefactor.getHeaderClone();
		List<List<String>> contentClone = objectMapperRefactor.getContentClone();
        System.out.println(headerClone);
		String newHeader=String.join(",",headerClone);
		List<String> newRows = new ArrayList<>();
//		newRows.add(
//				"id,vehicle_type,vin,notes,account.id,license.number,license.country,license.state,asset.number,make,model,size,class,color,weight,status,sensor.provider,sensor.deviceId,sensor.customId");
        newRows.add(newHeader);
		for (int i = 1; i < contentClone.size(); i++) {
			String newContent=String.join(",", contentClone.get(i));
			newRows.add(newContent);
		}
		
//		String fileName = "asset_" + System.currentTimeMillis() + ".csv";
//		String filePath = "D:\\Salesforce POC\\"+ fileName;
//		FileWriter fileWriter = null;
//		try {
//			fileWriter = new FileWriter(filePath);
//			for (String row : newRows) {
//				fileWriter.append(row);
//				fileWriter.append("\n");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				fileWriter.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		ftpClient.upload(filePath, fileName);
		
	}
}


