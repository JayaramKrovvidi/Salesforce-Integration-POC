package com.integration.poc.services.impl;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import com.integration.poc.services.IPostProcessService;

public class SaveFilePostProcessImpl implements IPostProcessService {

  private static final Logger LOGGER = LogManager.getLogger(SaveFilePostProcessImpl.class);

  @Value("${local.file.path}")
  private String localFilePath;
  
  
  @Override
  public void process() {
   
////    String fileName = apiKey + processKey + ".csv";
////    String filePath = localFilePath + fileName;
//
//    FileWriter fileWriter = null;
//    try {
//      fileWriter = new FileWriter(filePath);
////      fileWriter.append(processedResponse);
//    } catch (IOException e) {
//      LOGGER.error("IO Exception while trying to save file locally");
//    } finally {
//      try {
//        if (fileWriter != null) {
//          fileWriter.close();
//        }
//      } catch (IOException e) {
//        LOGGER.error("IO Exception while trying to close fileWriter resources");
//      }
//    }
//    
  }

}
