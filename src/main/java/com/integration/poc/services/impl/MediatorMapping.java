package com.integration.poc.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.dtos.internal.ObjectMapper;
import com.integration.poc.services.IMediatorMapping;
import com.integration.poc.services.IObjectMapperRefactor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@Service
public class MediatorMapping implements IMediatorMapping {
  
  @Autowired
  IObjectMapperRefactor objectMapperRefactor;
  
  public List<String> run(List<List<String>> mediatorOutput,String ObjectMappingOnSuccess){
   
      JSONArray mappersArray=getConfigObject(ObjectMappingOnSuccess);
      ObjectMapper objectMapper=getObjectMapper(ObjectMappingOnSuccess, mappersArray);
      return objectMapperRefactor.run(mediatorOutput, objectMapper);
      
  }
  
  private JSONArray getConfigObject(String objectMappingOnSuccess) {
    
    JSONParser jsonParser = new JSONParser();
    
    try (FileReader reader = new FileReader("C:\\Users\\nandhini.r\\Desktop\\Projects\\Salesforce Integration\\Salesforce-Integration-POC\\src\\main\\resources\\lib\\objectMapper.json"))
    {
        Object obj = jsonParser.parse(reader);

        JSONObject object = (JSONObject) obj;
        
        JSONArray objectMapper = (JSONArray)object.get("objectMapper");
        for(int i=0;i<objectMapper.size();i++) {
          JSONObject obj1 = (JSONObject) objectMapper.get(i);
         
          if(obj1.get("mappingKey").equals(objectMappingOnSuccess)) {
            
               return (JSONArray) obj1.get("mappers");
          }
        }     
  }catch (FileNotFoundException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
} catch (ParseException e) {
    e.printStackTrace();
}
    return null;
  }
   
  private ObjectMapper getObjectMapper(String ObjectMappingOnSuccess,JSONArray mapperArray) {
    List<NameValuePair<String, String>> nameValuePair=new ArrayList<NameValuePair<String,String>>();
    ObjectMapper objectmapper=new ObjectMapper();
    for(int i=0; i<mapperArray.size();i++) {
      JSONObject i1 = (JSONObject)mapperArray.get(i);
      NameValuePair<String,String> pair=new NameValuePair<String, String>();
      pair.setName((String) i1.get("name"));
      pair.setValue((String) i1.get("value"));
     nameValuePair.add(pair);
    }
    objectmapper.setMappingKey(ObjectMappingOnSuccess);
    objectmapper.setMappers(nameValuePair);
   
    
    
    return objectmapper;
  }

}
