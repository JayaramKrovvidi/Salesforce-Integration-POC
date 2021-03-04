package com.integration.poc.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import com.integration.poc.constants.CommonConstants;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.utils.Util;

@Service
public class FtpApiExecutorImpl implements IApiExecutor {

  @Override
  public String executeApi(ApiRequestConfig apiRequest) {
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
    String localPath = CommonConstants.LOCAL_FILE_PATH + fileNm;
    String remotePath = requestParams.get("ftp.remotePath");
    return uploadFileToFtpServer(ftpServer, ftpUName, ftpPass, fileNm, localPath, remotePath);
  }

  private String uploadFileToFtpServer(String ftpServer, String ftpUName, String ftpPass,
      String fileNm, String localFilePath, String remoteFilePath) {
    FTPClient ftp = new FTPClient();
    boolean status = false;
    try {
      ftp.connect(ftpServer);
      ftp.login(ftpUName, ftpPass);
      ftp.setFileType(FTP.BINARY_FILE_TYPE);
      ftp.enterLocalPassiveMode();
      InputStream input = new FileInputStream(new File(localFilePath));
      status = ftp.storeFile(remoteFilePath + fileNm, input);
      if (status) {
        System.out.println("Upload Successful");
      } else {
        System.out.println("Upload failed");
      }
      ftp.logout();
      ftp.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return String.valueOf(status);
  }

}
