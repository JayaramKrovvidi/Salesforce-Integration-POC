package com.integration.poc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

@Service
public class FTPClientUtil {

  public void upload(String filePath, String fileName) {
    FTPClient ftp = new FTPClient();
    try {
      ftp.connect(System.getProperty("ftp.server"));
      ftp.login(System.getProperty("ftp.username"), System.getProperty("ftp.password"));
      ftp.setFileType(FTP.BINARY_FILE_TYPE);
      ftp.enterLocalPassiveMode();
      InputStream input = new FileInputStream(new File(filePath));
      boolean status = ftp.storeFile("/solo/asset/" + fileName, input);
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

  }
}
