package com.integration.poc.services.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IApiExecutor;

@Service
public class DatabaseExecutorImpl implements IApiExecutor {

  private static String USERNAME;
  private static String PASSWORD;
  private static String DRIVER;
  private static String QUERY;
  private static String DBURL;

  @Override
  public String executeApi(ApiRequestConfig apiRequest) {
    extractData(apiRequest);
    switch (apiRequest.getMethodType()) {
      case "SELECT":
        return executeSelect(apiRequest);
      default:
        return null;
    } 
  }
  
  private String executeSelect(ApiRequestConfig apiRequest) {   
    createConnection();
    return null;
  }
  
  private void extractData(ApiRequestConfig apiRequest){
    for(NameValuePair eachPair : apiRequest.getRequestParams()) {
      if(eachPair.getName().equals("username")) {
        USERNAME = (String) eachPair.getValue();
      }
      else if (eachPair.getName().equals("password")) {
        PASSWORD = (String) eachPair.getValue();
      }
      else if (eachPair.getName().equals("driver")) {
        DRIVER = (String) eachPair.getValue();
      }
      else if (eachPair.getName().equals("dburl")) {
        DBURL = (String) eachPair.getValue();
      }
    }  
    QUERY = (String) apiRequest.getRequestBody();
  }
  
  private void createConnection() {
    
    try{  
      Class.forName(DRIVER);  
      Connection con=DriverManager.getConnection(DBURL,USERNAME,PASSWORD);  
      Statement stmt=con.createStatement();  
      ResultSet rs=stmt.executeQuery(QUERY);    
      while(rs.next())  
      System.out.println(rs.getInt(1)+"  "+rs.getString(2));  
      con.close();  
      }catch(Exception e){ System.out.println(e);}  
      }  

}
