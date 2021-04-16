package com.integration.poc.services.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.Node;
import com.integration.poc.dtos.internal.PostProcessConfig;
import com.integration.poc.dtos.internal.ProcKeyDTO;
import com.integration.poc.enums.FormatterEnum;
import com.integration.poc.enums.PostProcessEnum;
import com.integration.poc.services.IMediator;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.FTPClientUtil;

@Service
public class GenericResultProcessorImpl implements IResultProcessor {

  private static final Logger LOGGER = LogManager.getLogger(GenericResultProcessorImpl.class);

  @Value("${local.file.path}")
  private String localFilePath;

  @Autowired
  FTPClientUtil ftpClient;

  @Autowired
  BeanFactoryServiceImpl factory;

  @Autowired
  FileManagerServiceImpl fileManager;

  @Override
  public void process(ApiRequestConfig request, String apiKey, String response, boolean success) {
   if (CollectionUtils.isEmpty(request.getProcKeyOnSuccess())&&CollectionUtils.isEmpty(request.getProcKeyOnFailure())) {
     return;
   }
    List<ProcKeyDTO> procs=new ArrayList<>();
    if(success) {
      for(int i=0;i<request.getProcKeyOnSuccess().size();i++) {
        procs.add(request.getProcKeyOnSuccess().get(i));
      }
    }
    else {
      for(int i=0;i<request.getProcKeyOnFailure().size();i++) {
        procs.add(request.getProcKeyOnFailure().get(i));
      }
    }

   
    for (ProcKeyDTO proc : procs) {
      // Get Necessary Details and execute Post Processing
      IMediator inFormatter = factory.getBeanForClass(FormatterEnum.getFormatterByKey(proc.getInFormatter()));
      IMediator outFormatter =
          factory.getBeanForClass(FormatterEnum.getFormatterByKey(proc.getOutFormatter()));
      String configFilePath = PostProcessEnum.getConfigFilePath(proc.getName());
      PostProcessConfig postProcessConfig = fileManager.getConfigFromResource(configFilePath);

      List<Node> nodes = inFormatter.from(response);

//      LOGGER.info(" ------- CSV String before Processing --------\n {} \n", response);
//       LOGGER.info(" -------- Mediator Representation before Processing --------");
//       Node.printNodes(nodes);

      List<Node> processedNodes = outFormatter.process(nodes, postProcessConfig);
      String processedResponse = outFormatter.to(processedNodes);

//       LOGGER.info(" -------- Mediator Representation after Processing --------");
//       Node.printNodes(nodes);
      LOGGER.info(" ---- CSV String after Processing ----\n {} \n", processedResponse);

      saveFileLocally(processedResponse, apiKey, proc.getName(),proc.getOutFormatter());
    }
  }

  private void saveFileLocally(String processedResponse, String apiKey, String processKey,String outFormat) {
   
    String fileName = apiKey + processKey + "."+outFormat;
    String filePath = localFilePath + fileName;

    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(filePath);
      fileWriter.append(processedResponse);
    } catch (IOException e) {
      LOGGER.error("IO Exception while trying to save file locally");
    } finally {
      try {
        if (fileWriter != null) {
          fileWriter.close();
        }
      } catch (IOException e) {
        LOGGER.error("IO Exception while trying to close fileWriter resources");
      }
    }
  }



}


