package com.integration.poc.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.utils.Util;

@Service
public class FtpApiExecutorImpl implements IApiExecutor {

  private static final Logger LOGGER = LogManager.getLogger(FtpApiExecutorImpl.class);

  @Value("${local.file.path}")
  private String localFilePath;

  @Override
  public String executeApi(ApiRequestConfig apiRequest, String apiKey,Integer workFlowId) {
    switch (apiRequest.getMethodType()) {
      case "UPLOAD":
        return interpretRequestAndUpload(apiRequest);
      case "DOWNLOAD":
        return null;
      default:
        return null;
    }
  }

  private String interpretRequestAndUpload(ApiRequestConfig apiRequest) {
    String ftpServer = apiRequest.getUrl();
    Map<String, String> requestParams = Util.createMap(apiRequest.getRequestParams());
    String ftpUName = requestParams.get("ftp.username");
    String ftpPass = requestParams.get("ftp.password");
    String fileNm = requestParams.get("apiKey") + requestParams.get("processKey") + ".csv";
    String localPath = localFilePath + fileNm;
    String remoteUri = requestParams.get("ftp.remoteUri");
    return uploadFileToFtpServer(ftpServer, ftpUName, ftpPass, localPath, remoteUri);
  }

  private String uploadFileToFtpServer(String ftpServer, String ftpUName, String ftpPass,
      String localFilePath, String remoteUri) {
    FTPClient ftp = new FTPClient();
    boolean status = false;
    try {
      ftp.connect(ftpServer);
      ftp.login(ftpUName, ftpPass);
      ftp.setFileType(FTP.BINARY_FILE_TYPE);
      ftp.enterLocalPassiveMode();
      InputStream input = new FileInputStream(new File(localFilePath));
      String remoteFileDesc = remoteUri + Util.getDateEnding() + ".csv";
      status = ftp.storeFile(remoteFileDesc, input);
      if (status) {
        LOGGER.info("Upload File with Name: {} Successful", remoteFileDesc);
      } else {
        LOGGER.info("Upload File with Name: {} Failed", remoteFileDesc);
      }
      ftp.logout();
      ftp.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return String.valueOf(status);
  }

}
