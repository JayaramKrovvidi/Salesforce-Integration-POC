package com.integration.poc.mediator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.services.IMediatorMapping;


@Service
public class Mediator {
  
  @Autowired
  IMediatorMapping mediatorMapping;

  public List<String> mediatorMain(String response,String ObjectMappingoOnSuccess){
   
    return mediatorMapping.run(CSVToListConverter(response), ObjectMappingoOnSuccess);
       
    //Based on response type methods can be added in future
  }
  
  private List<List<String>> CSVToListConverter(String response){
    String[] rows = response.split("\\r?\\n");
    List<List<String>> mediatorOutput=new ArrayList();
    for(int i=0;i<rows.length;i++) {
      String[] linecontent=rows[i].split(",");    
      List <String> data=new ArrayList();
      for(int j=0;j<linecontent.length;j++) {
          data.add(linecontent[j].replaceAll("^[\"']+|[\"']+$", ""));
      }
      mediatorOutput.add(data);
  }
    return mediatorOutput;
  }
}
