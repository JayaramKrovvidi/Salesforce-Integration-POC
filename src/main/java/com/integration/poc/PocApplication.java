package com.integration.poc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PocApplication {

  public static void main(String[] args) {
    SpringApplication.run(PocApplication.class, args);
  }
  
  
  //made to check if the connection is working.
  //it is working
  @PostConstruct
  public void check() {
 
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
  }

