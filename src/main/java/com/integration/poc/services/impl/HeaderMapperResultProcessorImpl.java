package com.integration.poc.services.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.FTPClientUtil;

@Service("SalesforceBatchResultProcessor")
public class HeaderMapperResultProcessorImpl implements IResultProcessor {
	
	@Autowired
	FTPClientUtil ftpClient;

	@Override
	public void process(ApiRequestConfig apiRequest, String apiKey, String response) {
		System.out.println(response);
		String[] rows = response.split("\\r?\\n");

		List<String> newRows = new ArrayList<>();
		newRows.add(
				"id,vehicle_type,vin,notes,account.id,license.number,license.country,license.state,asset.number,make,model,size,class,color,weight,status,sensor.provider,sensor.deviceId,sensor.customId");

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
		
		ftpClient.upload(filePath, fileName);
		
	}
}