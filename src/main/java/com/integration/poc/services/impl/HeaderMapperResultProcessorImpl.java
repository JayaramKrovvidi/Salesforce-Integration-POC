package com.integration.poc.services.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.ObjectMapper;
import com.integration.poc.services.IObjectMapperRefactor;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.FTPClientUtil;

@Service("SalesforceBatchResultProcessor")
public class HeaderMapperResultProcessorImpl implements IResultProcessor {

  @Autowired
  FTPClientUtil ftpClient;

  @Autowired
  IObjectMapperRefactor objectMapperRefactor;


  @Override
  public void process(ApiRequestConfig apiRequest, String apiKey, String response,
      ObjectMapper mapper) {
    String[] rows = response.split("\\r?\\n");
    List<String> newRows = objectMapperRefactor.run(rows, mapper);

    String fileName = "asset_" + System.currentTimeMillis() + ".csv";
    String filePath = "D:\\Salesforce POC\\" + fileName;
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
        e.printStackTrace();
      }
    }

    ftpClient.upload(filePath, fileName);

  }



}


