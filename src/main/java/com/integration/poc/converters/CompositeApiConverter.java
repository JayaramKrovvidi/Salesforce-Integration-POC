package com.integration.poc.converters;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.dtos.internal.GenericApiRequest;

@Converter
public class CompositeApiConverter implements AttributeConverter<CompositeApiRequest, String> {

  @Override
  public String convertToDatabaseColumn(CompositeApiRequest attribute) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("requestList", attribute.getRequestList());
    return jsonObject.toString();
  }

  @Override
  public CompositeApiRequest convertToEntityAttribute(String dbData) {

    CompositeApiRequest compositeApi = new CompositeApiRequest();
    JSONObject jsonObject = new JSONObject(dbData);
    JSONArray object = (JSONArray) jsonObject.get("requestList");
    compositeApi.setRequestList(getGenericApis(object));
    System.out.println("----------------------------------------" + compositeApi);
    return compositeApi;
  }

  private List<GenericApiRequest> getGenericApis(JSONArray requestList) {
    List<GenericApiRequest> genericApiRequests = new ArrayList<>();
    for (int i = 0; i < requestList.length(); i++) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        GenericApiRequest genericApi = objectMapper.readValue(requestList.get(i)
            .toString(), GenericApiRequest.class);
        genericApiRequests.add(genericApi);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return genericApiRequests;

  }



}


