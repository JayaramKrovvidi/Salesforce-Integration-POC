package com.integration.poc.services.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.Node;
import com.integration.poc.dtos.internal.PostProcessConfig;
import com.integration.poc.enums.PostProcessEnum;
import com.integration.poc.services.IMediator;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.FTPClientUtil;

@Service
public class GenericResultProcessorImpl implements IResultProcessor {

  @Autowired
  FTPClientUtil ftpClient;

  @Autowired
  BeanFactoryServiceImpl factory;

  @Autowired
  FileManagerServiceImpl fileManager;

  @Override
  public void process(ApiRequestConfig apiRequest, String response, boolean success) {
    List<String> keys = success ? apiRequest.getProcKeyOnSuccess() : apiRequest.getProcKeyOnFailure();
    if (null == keys) {
      return;
    }
   
    for(String key: keys) {
   // Get Necessary Details and execute Post Processing
      IMediator inFormatter = factory.getBeanForClass(PostProcessEnum.getInputFormatterByKey(key));
      IMediator outFormatter = factory.getBeanForClass(PostProcessEnum.getOutputFormatterByKey(key));
      String configFilePath = PostProcessEnum.getConfigFilePath(key);
      PostProcessConfig postProcessConfig = fileManager.getConfigFromResource(configFilePath);

      
      List<Node> nodes = inFormatter.from(response);
      List<Node> processedNodes = outFormatter.process(nodes, postProcessConfig);
      String processedResponse = outFormatter.to(processedNodes);

      Node.printNodes(nodes);
      System.out.println(" --------------- After Processing -------------------");
      Node.printNodes(processedNodes);
      upload(processedResponse);
      
    }
    

  }

  private void upload(String processedResponse) {
    String fileName = "asset_" + System.currentTimeMillis() + ".csv";
    String filePath = "D:\\Salesforce POC\\" + fileName;
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(filePath);
      fileWriter.append(processedResponse);
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // ftpClient.upload(filePath, fileName);
  }



}


