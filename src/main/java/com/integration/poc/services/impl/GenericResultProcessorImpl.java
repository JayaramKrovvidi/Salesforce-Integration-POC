package com.integration.poc.services.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.constants.CommonConstants;
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
    List<String> keys =
        success ? apiRequest.getProcKeyOnSuccess() : apiRequest.getProcKeyOnFailure();
    if (CollectionUtils.isEmpty(keys)) {
      return;
    }

    for (String key : keys) {
      // Get Necessary Details and execute Post Processing
      IMediator inFormatter = factory.getBeanForClass(PostProcessEnum.getInputFormatterByKey(key));
      IMediator outFormatter =
          factory.getBeanForClass(PostProcessEnum.getOutputFormatterByKey(key));
      String configFilePath = PostProcessEnum.getConfigFilePath(key);
      PostProcessConfig postProcessConfig = fileManager.getConfigFromResource(configFilePath);

      List<Node> nodes = inFormatter.from(response);
      List<Node> processedNodes = outFormatter.process(nodes, postProcessConfig);
      String processedResponse = outFormatter.to(processedNodes);
      saveFileLocally(processedResponse, apiRequest.getApiKey(), key);
    }
  }

  private void saveFileLocally(String processedResponse, String apiKey, String processKey) {
    String fileName = apiKey + processKey + ".csv";
    String filePath = CommonConstants.LOCAL_FILE_PATH + fileName;

    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(filePath);
      fileWriter.append(processedResponse);
    } catch (IOException e) {
    } finally {
      try {
        if (fileWriter != null) {
          fileWriter.close();
        }
      } catch (IOException e) {
      }
    }
  }



}


