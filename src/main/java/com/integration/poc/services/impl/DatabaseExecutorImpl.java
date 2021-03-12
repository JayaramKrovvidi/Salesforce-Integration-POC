package com.integration.poc.services.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.services.IApiExecutor;

public class DatabaseExecutorImpl implements IApiExecutor {

  @Override
  public String executeApi(ApiRequestConfig apiRequest) {
    System.out.println("database impl called");
    switch (apiRequest.getMethodType()) {
      case "SELECT":
        return executeSelect(apiRequest);
      default:
        return null;
    }
    
  }
  
  private void createConnection() {
    
    try{  
      Class.forName("com.mysql.cj.jdbc.Driver");  
      Connection con=DriverManager.getConnection(  
      "jdbc:mysql://localhost:3306/checkdb","root","root");  
      Statement stmt=con.createStatement();  
      System.out.println("connection made");
      ResultSet rs=stmt.executeQuery("select * from checktable");  
      
      while(rs.next())  
      System.out.println(rs.getInt(1)+"  "+rs.getString(2));  
      con.close();  
      }catch(Exception e){ System.out.println(e);}  
      }  
  

  private String executeSelect(ApiRequestConfig apiRequest) {
    
    createConnection();
    return null;
  }

}
