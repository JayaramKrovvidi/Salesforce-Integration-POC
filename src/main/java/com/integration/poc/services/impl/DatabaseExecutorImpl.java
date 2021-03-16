package com.integration.poc.services.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.GenericApiRequest;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.utils.Util;

@Service
public class DatabaseExecutorImpl implements IApiExecutor {

  private static final Logger LOGGER = LogManager.getLogger(DatabaseExecutorImpl.class);

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String DRIVER = "driver";

  @Override
  public String executeApi(ApiRequestConfig apiRequest , String apiKey) {
    switch (apiRequest.getMethodType()) {
      case "SELECT":
        return extractDataAndExecute(apiRequest);
      default:
        return null;
    }
  }

  private String extractDataAndExecute(ApiRequestConfig apiRequest) {
    Map<String, String> params = Util.<String, String>createMap(apiRequest.getRequestParams());
    String dbUrl = apiRequest.getUrl();
    String userName = params.get(USERNAME);
    String pass = params.get(PASSWORD);
    String driverNm = params.get(DRIVER);
    String query = (String) apiRequest.getRequestBody();
    try {
      executeQuery(dbUrl, userName, pass, driverNm, query);
    } catch (Exception e) {
      LOGGER.error("Exception In DB Query Execution: {}", e.getMessage());
    }
    return null;
  }

  private void executeQuery(String dbUrl, String userName, String pass, String driverNm,
      String query) throws ClassNotFoundException, SQLException {
    Class.forName(driverNm);
    Connection con = DriverManager.getConnection(dbUrl, userName, pass);
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next())
      System.out.println(rs.getInt(1) + "  " + rs.getString(2));
    con.close();
  }

}
